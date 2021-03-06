package com.example.wbj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.wbj.view.WebDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WebViewActivity extends AppCompatActivity {
    public static final String TAG = WebViewActivity.class.getSimpleName();
    private WebView mWebXML;
    private ContactService mContactService;

    private ViewGroup mWebLayout;
    private WebView mWebAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_aty);

        mContactService = new ContactService();
        mWebXML = (WebView) findViewById(R.id.main_web_xml);
        mWebXML.getSettings().setJavaScriptEnabled(true);//设置webView可以使用JavaScript
        mWebXML.getSettings().setSupportZoom(false);//设置是否支持缩放，默认为true
        mWebXML.getSettings().setBuiltInZoomControls(false);//设置是否显示缩放工具，默认为false
        //mWebView.getSettings().setDefaultFontSize(18);//设置字体大小，默认为16，有效值区间在1-72之间

        //mWebView.addJavascriptInterface(new ContactPlugin(), "contact");//将插件类实例绑定到javascript中
        //mWebView.loadUrl("file:///android_asset/index.html");//加载位于assets目录下html

        //设置透明背景--start
        mWebXML.setBackgroundColor(0);
        mWebXML.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //设置透明背景--end

        //mWebXML.addJavascriptInterface(new JsInteration(), "control");
        //mWebXML.loadUrl("file:///android_asset/test.html");//加载位于assets目录下html
        //mWebXML.loadUrl("http://act.miuapp.com/?r=h5/guide");
        //mWebXML.loadUrl("http://10.10.30.108/?r=h5/tacit1");
        //mWebXML.loadUrl("http://www.google.com");//加载远程网页
        //mWebXML.loadUrl("http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2013/1010/1569.html");//加载远程网页

        WebSettings setting = mWebXML.getSettings();
        //支持Js
        setting.setJavaScriptEnabled(true);
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        //缓存模式
        setting.setCacheMode(WebSettings.LOAD_DEFAULT);
        //支持内容重新布局
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //将图片调整到适合webview的大小
        setting.setUseWideViewPort(true);
        setting.setLoadWithOverviewMode(true);
        //设置可以访问文件
        setting.setAllowFileAccess(true);
        //支持自动加载图片
        setting.setLoadsImagesAutomatically(true);
        try {
            setting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
        }
        mWebXML.loadUrl("https://h5-phonecloud-test.onethingpcs.com/help/feedback");//加载远程网页

        //加载html的片段内容
        //String body ="示例：这里有个img标签，地址是相对路径<img src='/uploads/allimg/130923/1FP02V7-0.png' />";
        //mWebView.loadDataWithBaseURL("http://www.jcodecraeer.com", body, "text/html", "utf-8",null);

        mWebXML.setWebChromeClient(new WebChromeClient() {
            // For Android 5.0+
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "Image Chooser"), 0);
                return true;
            }
        });//保证能弹出网页的alert对话框
        mWebXML.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(TAG, "-->page load start-->" + url + ", " + Thread.currentThread().getName());
            }

            //如果网页的js代码没有加载完成，就调用js方法，那么会出现“Uncaught ReferenceError: functionName
            //is not defined”异常。注：网页加载失败还是会调用该方法，但调用之前会先调用onReceivedError方法

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d(TAG, "page load finish-->" + url);
                //testMethod(view);
            }

            //网页加载失败会调用该方法
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                //Log.d(TAG, "---Received Error---" + error.getErrorCode() + ", " + error.getDescription());
            }
        });

        mWebXML.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && mWebXML.canGoBack()) {
                    mWebXML.goBack();
                    return true;
                }
                return false;
            }
        });

        //mWebLayout = (ViewGroup) findViewById(R.id.web_layout);
    }

    public void callJS(View view) {
        //this.testMethod(mWebView);

        //Android4.4(API19)之后可以使用evaluateJavascript直接调用JS方法
//        mWebView.evaluateJavascript("getGreetings()", new ValueCallback<String>() {//该方法必须在主线程中调用
//            @Override
//            public void onReceiveValue(String value) {//获取getGreetings()的返回值
//                Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
//            }
//        });
//        mWebView.evaluateJavascript("sayHello()", null);
//        mWebView.evaluateJavascript("alertMessage('" + "ssywbj" + "')", null);
    }

    private void testMethod(WebView webView) {
        String call = "javascript:sayHello()";
        call = "javascript:alertMessage(\"" + "888" + "\")";
        String msg = "109163389";
        call = "javascript:alertMessage(\"" + msg + "\")";
        call = "javascript:toastMessage(\"" + "666" + "\")";
        call = "javascript:toastMessage('" + "单引号" + "')";
        //call = "javascript:sumToJava(1,2)";
        webView.loadUrl(call);
    }

    public class JsInteration {

        //如果将targetSdkVersion设置成17或更高，则需要引入@JavascriptInterface注释
        @JavascriptInterface
        public void toastMessage(String message) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void onSumResult(int result) {
            Log.i(TAG, "onSumResult result=" + result);
            Toast.makeText(getApplicationContext(), "result = " + result, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mWebXML.destroy();

        this.destroyWebView();
    }

    //实体类
    public class Contact {
        public Integer id;
        public String name;
        public String mobile;

        public Contact(Integer id, String name, String mobile) {
            this.id = id;
            this.name = name;
            this.mobile = mobile;
        }
    }

    //业务逻辑 模拟从服务端获取数据
    public class ContactService {
        public List<Contact> getContacts() {
            List<Contact> contacts = new ArrayList<>();
            contacts.add(new Contact(34, "张三", "15170013856"));
            contacts.add(new Contact(39, "李四", "15170013856"));
            contacts.add(new Contact(67, "王五", "15170013856"));
            return contacts;
        }
    }

    //插件类
    private final class ContactPlugin {

        @JavascriptInterface
        public void getContacts() {
            List<Contact> contacts = mContactService.getContacts();
            try {
                JSONArray array = new JSONArray();
                for (Contact contact : contacts) {
                    JSONObject item = new JSONObject();
                    item.put("id", contact.id);
                    item.put("name", contact.name);
                    item.put("mobile", contact.mobile);
                    array.put(item);
                }
                String json = array.toString();
                Log.e(TAG, "json = " + json);
                //mWebXML.loadUrl("javascript:show('" + json + "')");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @JavascriptInterface
        public void call(String phoneNum) {
            Log.e(TAG, "photo number-->" + phoneNum);
        }
    }

    public void beginLoad(View view) {
        Log.d(TAG, "---beginLoad---");
        //mWebView.loadUrl("http://10.10.30.108/?r=h5/tacit1");//（注：此处用于对比）

        //this.createWebView("http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2013/1010/1569.html");
        //this.createWebView("http://10.10.30.108/?r=h5/gamecenter");
        //this.createWebView("http://10.10.30.108/index.php?r=h5/rashambo&opentype=2&flowernum=3&roundid=631&peeruid=109163392");
        //new WebDialog(this, "http://www.idyjy.com/").show();
        String url = "http://www.dytt8.net/html/gndy/dyzz/20170725/54589.html";
        new WebDialog(this, url).show();
    }

    public void stopLoad(View view) {
        Log.d(TAG, "---stopLoad---");
        //mWebView.stopLoading();//貌似调用后也不能让WebView停止加载，还有会回调onPageFinished，onReceivedError等方法（注：此处用于对比）

        this.destroyWebView();//动态添加和删除WebView，解决stopLoading()方法无响应问题
    }

    private void createWebView(String url) {
        this.destroyWebView();

        mWebAdd = new WebView(this);

        mWebAdd.getSettings().setSupportZoom(true);//支持缩放
        mWebAdd.getSettings().setBuiltInZoomControls(true); //支持缩放，显示缩放按钮
        mWebAdd.getSettings().setDisplayZoomControls(false);//支持缩放(设置此项后，缩放按钮不见了)
        mWebAdd.getSettings().setUseWideViewPort(true);//自适应屏幕宽度
        mWebAdd.getSettings().setLoadWithOverviewMode(true);//自适应屏幕宽度

        mWebAdd.getSettings().setJavaScriptEnabled(true);
        mWebAdd.getSettings().setSupportZoom(false);
        mWebAdd.setBackgroundColor(0);
        mWebAdd.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebAdd.loadUrl(url);
        mWebLayout.addView(mWebAdd);
        mWebAdd.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mIsLoadError = false;
                Log.e(TAG, "-->2222222start-->" + url + ", " + Thread.currentThread().getName());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                Log.e(TAG, "2222222finish-->" + url + ", " + view.getProgress() + ", " + view.getHandler());
                if (mIsLoadError) {
                    return;
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                //Log.e(TAG, "---2222222Error---" + error.getErrorCode() + ", " + error.getDescription());
                view.loadUrl("file:///android_asset/error/sorry.html");
                if (!mIsLoadError) {
                    mIsLoadError = true;
                    Toast.makeText(WebViewActivity.this, "load fail", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mWebAdd.setWebViewClient(new MyWebViewClient());
    }

    private boolean mIsLoadError;

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(final WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.e(TAG, "-->start-->" + url);
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "time over-->" + Thread.currentThread().getName() + ", " + view.getProgress());
                    if (view.getProgress() < 100) {
                        view.loadUrl("file:///android_asset/error/sorry.html");
                    }
                }
            }, 10000);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e(TAG, "-->finish-->" + url + ", " + view.getProgress());
        }

        @Override//在Android 6.0及以上调用
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            view.loadUrl("file:///android_asset/error/sorry.html");
            //Log.e(TAG, "-->Error-->" + error.getErrorCode() + ", " + error.getDescription());
        }

        @Override//在Android 6.0及以下调用
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return;//避免Android 6.0及以上重复调用显示错误页
            }
            Log.e(TAG, "-->Error Deprecated-->" + errorCode + ", " + description);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.e(TAG, "-->shouldOverrideUrlLoading-->" + view.getUrl());
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e(TAG, "-->shouldOverrideUrlLoading Deprecated-->" + url);
            return super.shouldOverrideUrlLoading(view, url);
        }

    }

    private void destroyWebView() {
        if (mWebAdd != null) {
            mWebLayout.removeView(mWebAdd);
            mWebAdd.destroy();//让WebVie停止响应的主要是这个方法
            mWebAdd = null;
        }
    }

    /*private WebView wView; //web展示地图
    private ValueCallback<Uri> mUploadMessage;//回调图片选择，4.4以下
    private ValueCallback<Uri[]> mUploadCallbackAboveL;//回调图片选择，5.0以上

    private final static int FILE_CHOOSER_RESULT_CODE = 4; //图片
    private final static int FILE_CAMERA_RESULT_CODE = 5;//拍照
    private String cameraFielPath; //拍照图片路径
    private static  final  String[] stocks = {"拍照", "相册"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_aty);
        initPower();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        readWebMap();
    }

    public void readWebMap(){
        wView = findViewById(R.id.main_web_xml);
        wView.loadUrl("file:///android_asset/test1.html");
        WebSettings settings = wView.getSettings();

        settings.setJavaScriptEnabled(true);    //设置webview支持javascript
        settings.setLoadsImagesAutomatically(true);    //支持自动加载图片
        settings.setUseWideViewPort(true);    //设置webview推荐使用的窗口，使html界面自适应屏幕
        settings.setLoadWithOverviewMode(true);
        settings.setSaveFormData(true);    //设置webview保存表单数据

        settings.setSupportMultipleWindows(true);
        settings.setAppCacheEnabled(true); //设置APP可以缓存
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);//返回上个界面不刷新  允许本地缓存
        settings.setAllowFileAccess(true);// 设置可以访问文件

        wView.setLongClickable(true);
        wView.setScrollbarFadingEnabled(true);
        wView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wView.setDrawingCacheEnabled(true);

        //添加客户端支持
        wView.setWebChromeClient(new MyWebChromeClient() );
    }

    *//**
     *  注入权限
     *  @author: zxf
     *  @time: 2018/9/4 20:06
     *//*
    private  void initPower(){
        int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int locations = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int storgeWrite =ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int storgeRead =ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 6);
        }

        if(locations != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 7);
        }
        if(storgeRead != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 8);
        }
        if(storgeWrite != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 8);
        }
    }

    private class MyWebChromeClient extends WebChromeClient {

        // For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            mUploadMessage = uploadMsg;
            openImageChooserActivity();
        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            mUploadMessage = uploadMsg;
            openImageChooserActivity();
        }

        // For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadMessage = uploadMsg;
            openImageChooserActivity();
        }

        // For Android 5.0+
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            mUploadCallbackAboveL = filePathCallback;
            openImageChooserActivity();
            return true;
        }
    }

    ///选择拍照还是相册
    public void openImageChooserActivity() {
        new MaterialDialog.Builder(this)
                .items(stocks)
                .positiveText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (mUploadCallbackAboveL != null) {
                            mUploadCallbackAboveL.onReceiveValue(null);
                            mUploadCallbackAboveL = null;
                        }
                        if (mUploadMessage != null) {
                            mUploadMessage.onReceiveValue(null);
                            mUploadMessage = null;
                        }
                        dialog.dismiss();
                    }
                })
                .cancelable(false)
                .canceledOnTouchOutside(false)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                        if (position == 0) {
                            takeCamera();
                        } else if (position == 1) {
                            takePhoto();
                        }
                    }
                }).show();
    }

    //拍照
    private void takeCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        cameraFielPath = Environment.getExternalStorageDirectory() + "//" + System.currentTimeMillis() + ".jpg";
        File outputImage = new File(cameraFielPath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputImage));
        startActivityForResult(intent, FILE_CAMERA_RESULT_CODE);
    }

    //选择图片
    private void takePhoto() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  super.onActivityResult(requestCode, resultCode, resultData);
        if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
        if (resultCode != RESULT_OK) {//同上所说需要回调onReceiveValue方法防止下次无法响应js方法
            if (mUploadCallbackAboveL != null) {
                mUploadCallbackAboveL.onReceiveValue(null);
                mUploadCallbackAboveL = null;
            }
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(null);
                mUploadMessage = null;
            }
            return;
        }
        Uri result = null;
        if (requestCode == FILE_CAMERA_RESULT_CODE) {
            if (null != data && null != data.getData()) {
                result = data.getData();
            }
            if (result == null && hasFile(cameraFielPath)) {
                result = Uri.fromFile(new File(cameraFielPath));
            }
            if (mUploadCallbackAboveL != null) {
                mUploadCallbackAboveL.onReceiveValue(new Uri[]{result});
                mUploadCallbackAboveL = null;
            } else if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        } else if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (data != null) {
                result = data.getData();
            }
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(data);
            } else if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
    }

    public static boolean hasFile(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            Log.i("error", e.toString());
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(Intent intent) {
        Uri[] results = null;
        if (intent != null) {
            String dataString = intent.getDataString();
            ClipData clipData = intent.getClipData();
            if (clipData != null) {
                results = new Uri[clipData.getItemCount()];
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    ClipData.Item item = clipData.getItemAt(i);
                    results[i] = item.getUri();
                }
            }
            if (dataString != null)
                results = new Uri[]{Uri.parse(dataString)};
        }
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
    }*/
}
