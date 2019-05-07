package com.suheng.ssy.boutique;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.store.CookieStore;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

public class OkGoActivity extends PermissionApplyActivity/*BasicActivity*/ {

    //public static final String URL = "http://gank.io/api/data/福利/50/1";
    private static final String URL = "http://192.168.120.169:8080/TestJSP";
    private static final String SERVER_ADDRESS = URL + "/ServletDemo";
    private static final String PICTURE_ADDRESS = URL + "/pictures";
    private static final String VIDEO_ADDRESS = URL + "/Videos";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_go);
        this.initCookie();
    }

    public void onClickGet(View view) {
        OkGo.<String>get(URL)
                .tag(this)//请求的tag，主要用于取消对应的请求
                //.isMultipart(true)//post请求方法的属性，表示是否强制使用multipart/form-data表单上传，因为该框架在有文件的时候，无论你是否设置这个参数，默认都是multipart/form-data格式上传，但是如果参数中不包含文件，默认使用application/x-www-form-urlencoded格式上传，如果你的服务器要求无论是否有文件，都要使用表单上传，那么可以用这个参数设置为true。
                //.isSpliceUrl(true)//post请求方法的属性，表示是否强制将params的参数拼接到url后面，默认false不拼接。一般来说，post、put等有请求体的方法应该把参数都放在请求体中，不应该放在url上，但是有的服务端可能不太规范，url和请求体都需要传递参数，那么这时候就使用该参数，他会将你所有使用.params()方法传递的参数，自动拼接在url后面。
                .retryCount(3)//配置超时重连次数，也可以在全局初始化的时候设置，默认使用全局的配置，即为3次，也可以在这里为你的这个请求特殊配置一个，并不会影响全局其他请求的超时重连次数。
                .cacheKey("cacheKey")
                .cacheTime(10 * 1000)
                .headers("header1", "headValue1")//传递服务端需要的http请求头
                .headers("header2", "headValue2")
                .params("param2", "paramValue2", false)//传递键值对参数，格式也是http协议中的格式，最后一个isReplace为可选参数,默认为true，即代表相同key的时候，后添加的会覆盖先前添加的
                .params("param3", "paramValue3")
                //.params("file1", new File("filePath1"))
                //.params("file2", new File("filePath2"))
                .addUrlParams("key", Arrays.asList(new String[]{"key1,key2"}))
                //.addFileParams("key", Arrays.asList(new File[]{new File("file1"), new File("file2")}))//post请求方法的属性
                //.addFileWrapperParams("key", null)//post请求方法的属性
                .execute(new Callback<String>() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        Log.d(mTag, "11111onStart-->" + request + ", " + request.getUrl());
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            Log.d(mTag, "11111onSuccess-->response = " + response + ", code = " + response.code() + ", message = " + response.message() + ", body = " + response.body()
                                    + ", getRawResponse().body = " + response.getRawResponse().body().string());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {
                        Log.d(mTag, "11111onCacheSuccess-->" + response + ", " + response.message());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        Log.d(mTag, "11111onError-->" + response + ", " + response.message());
                    }

                    @Override
                    public void onFinish() {
                        Log.d(mTag, "11111onFinish-->");
                    }

                    @Override
                    public void uploadProgress(Progress progress) {
                        Log.d(mTag, "11111uploadProgress-->" + progress);
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        Log.d(mTag, "11111downloadProgress-->" + progress);
                    }

                    @Override
                    public String convertResponse(okhttp3.Response response) throws Throwable {
                        Log.d(mTag, "11111convertResponse-->" + response + "\n" + response.body().string());
                        return null;
                    }
                });
        /*每个请求都有一个.client()方法可以传递一个OkHttpClient对象，表示当前这个请求将使用外界传入的这个OkHttpClient对象，
        其他的请求还是使用全局的保持不变。那么至于这个OkHttpClient想怎么配置或者配置什么东西，那就随意了，改个超时时间，加个拦截器什么的统统都是可以的。
        特别注意： 如果你的当前请求使用的是你传递的OkHttpClient对象的话，那么当你调用OkGo.getInstance().cancelTag(tag)的时候
        ，是取消不了这个请求的，因为OkGo只能取消使用全局配置的请求，不知道你这个请求是用你自己的OkHttpClient的，
        如果一定要取消，可以是使用OkGo提供的重载方法*/

        OkGo.<String>get(SERVER_ADDRESS).tag(this)
                .params("username", "ssy")
                .params("password", "654321")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d(mTag, "22222Upload params, onSuccess-->" + response.code() + "-->"
                                + response.message() + "-->" + response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.d(mTag, "22222Upload params, onError-->" + response.code() + "-->" + response.message()
                                + "-->" + response.body());
                    }
                });
    }

    public void onClickCallback(View view) {
        OkGo.<String>get(URL + "/").tag(this)//如果单纯的URL后不加“/”，请求时HTTP会重定向，自动帮在URL后帮我们加上“/”
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d(mTag, "33333String, onSuccess-->" + response.code() + ", " + response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.d(mTag, "33333String, onError-->" + response.code() + ", " + response.message());
                    }
                });

        //http://ww1.sinaimg.cn/large/0065oQSqly1fs02a9b0nvj30sg10vk4z.jpg
        OkGo.<Bitmap>get(PICTURE_ADDRESS + "/0065oQSqly1fymj13tnjmj30r60zf79k.jpg").tag(this)
                .execute(new BitmapCallback() {
                    @Override
                    public void onSuccess(Response<Bitmap> response) {
                        Log.d(mTag, "44444Bitmap, onSuccess-->" + response.code() + ", " + response.body());
                        ((ImageView) findViewById(R.id.image_url)).setImageBitmap(response.body());
                    }

                    @Override
                    public void onError(Response<Bitmap> response) {
                        super.onError(response);
                        Log.d(mTag, "44444Bitmap, onError-->" + response.code() + ", " + response.body());
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        Log.d(mTag, "44444Bitmap, onFinish-->");
                    }
                });
    }

    public void onClickPost(View view) {
        OkGo.<Bitmap>post(PICTURE_ADDRESS + "/0065oQSqgy1ft4kqrmb9bj30sg10fdzq.jpg").tag(this)
                .execute(new BitmapCallback() {

                    @Override
                    public Bitmap convertResponse(okhttp3.Response response) throws Throwable {
                        Bitmap bitmap = super.convertResponse(response);
                        Log.d(mTag, "55555post, convertResponse-->" + response.code() + ", " + response.body() + ", " + bitmap);
                        return bitmap;
                    }

                    @Override
                    public void onSuccess(Response<Bitmap> response) {
                        Log.d(mTag, "55555post, onSuccess-->" + response.code() + ", " + response.body());
                        ((ImageView) findViewById(R.id.image_url)).setImageBitmap(response.body());
                    }

                    @Override
                    public void onError(Response<Bitmap> response) {
                        super.onError(response);
                        Log.d(mTag, "55555post, onError-->" + response.code() + ", " + response.message() + ", " + response.body());
                    }
                });

        OkGo.<String>post(SERVER_ADDRESS).tag(this)
                .params("username", "Wbj")
                .params("password", "123456")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d(mTag, "66666post params, onSuccess-->" + response.code() + "-->" + response.message()
                                + "-->" + response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.d(mTag, "66666post params, onError-->" + response.code() + "-->" + response.message()
                                + "-->" + response.body());
                    }
                });
    }

    public void onClickDownload(View view) {
        PermissionApplyActivityPermissionsDispatcher.requestExternalStoragePermissionWithCheck(this, 2);
    }

    private void downloadFile() {
        //OkGo.<File>get(VIDEO_ADDRESS + "/【电影家园www.idyjy.com下载】一个字头的诞生.DVD国粤双语中英双字.avi").tag(this)
        OkGo.<File>get(PICTURE_ADDRESS + "/0065oQSqly1fymj13tnjmj30r60zf79k.jpg").tag(this)
                .execute(new FileCallback() {

                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        super.onStart(request);
                        Log.d(mTag, "77777File, onStart-->" + request.getUrl());
                    }

                    @Override
                    public void onSuccess(Response<File> response) {//如果不指定下载目录,默认为sdcard/download/
                        Log.d(mTag, "77777File, onSuccess-->" + response.body().getName() + ", " + response.body().getAbsolutePath() + ", " + response.body().length());
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        Log.d(mTag, "77777File, downloadProgress-->" + progress.totalSize + ", " + progress.speed + ", " + progress.currentSize);
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        Log.d(mTag, "77777File, onError-->" + response.code() + ", " + response.message() + ", " + response.isFromCache());
                    }
                });
    }

    public void onClickUpload(View view) {
        PermissionApplyActivityPermissionsDispatcher.requestExternalStoragePermissionWithCheck(this, 1);
    }

    @Override
    public void requestExternalStoragePermission(int requestCode) {
        super.requestExternalStoragePermission(requestCode);
        if (requestCode == 1) {
            this.uploadFile();
        } else if (requestCode == 2) {
            this.downloadFile();
        }
    }

    private void uploadFile() {
        OkGo.<String>post(SERVER_ADDRESS).tag(this)
                .upString("---upload params中的参数设置是无效的---")//使用该方法时，params中的参数设置是无效的，所有参数均需要通过需要上传的文本中指定
                .params("username", "Wbj")
                .params("password", "123456")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d(mTag, "88888Upload String, onSuccess-->" + response.code() + "-->" + response.message()
                                + "-->" + response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.d(mTag, "88888Upload String, onError-->" + response.code() + "-->" + response.message()
                                + "-->" + response.body());
                    }
                });

        OkGo.<String>post(SERVER_ADDRESS).tag(this)
                .upString("username=Wbj&password=123456&data=---upload params中的参数设置是无效的---")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.d(mTag, "99999Upload params String, onSuccess-->" + response.code() + "-->" + response.message()
                                + "-->" + response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.d(mTag, "99999Upload params String, onError-->" + response.code() + "-->" + response.message()
                                + "-->" + response.body());
                    }
                });

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    , "Screenshots" + File.separator + "Screenshot_20190219-142916.png");
            if (file.exists()) {
                OkGo.<String>post(SERVER_ADDRESS).tag(this)
                        .params("username", "Wbj")
                        .params("password", "123456")
                        .upFile(file)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                Log.d(mTag, "00000Upload File, onSuccess-->" + response.code() + "-->" + response.message()
                                        + "-->" + response.body());
                            }

                            @Override
                            public void uploadProgress(Progress progress) {
                                super.uploadProgress(progress);
                                Log.d(mTag, "00000Upload File, uploadProgress-->" + progress.totalSize + ", " + progress.speed + ", " + progress.currentSize);
                            }

                            @Override
                            public void onError(Response<String> response) {
                                super.onError(response);
                                Log.d(mTag, "00000Upload File, onError-->" + response.code() + "-->" + response.message() + "-->" + response.body());
                            }
                        });

                OkGo.<String>post(SERVER_ADDRESS).tag(this)
                        .params("username", "Wbj")
                        .params("password", "123456")
                        .params("file", file)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                Log.d(mTag, "AAAAAparams File, onSuccess-->" + response.code() + "-->" + response.message()
                                        + "-->" + response.body());
                            }

                            @Override
                            public void uploadProgress(Progress progress) {
                                super.uploadProgress(progress);
                                Log.d(mTag, "AAAAAparams File, uploadProgress-->" + progress.totalSize + ", " + progress.speed + ", " + progress.currentSize);
                            }

                            @Override
                            public void onError(Response<String> response) {
                                super.onError(response);
                                Log.d(mTag, "AAAAAparams File, onError-->" + response.code() + "-->" + response.message() + "-->" + response.body());
                            }
                        });
            } else {
                Log.d(mTag, "需要上传的文件不存在");
            }
        }
    }

    private void initCookie() {
        HttpUrl httpUrl = HttpUrl.parse(URL);
        Cookie.Builder builder = new Cookie.Builder();
        Cookie cookie = builder.name("myCookieKey1").value("myCookieValue1").domain(httpUrl.host()).build();
        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
        cookieStore.saveCookie(httpUrl, cookie);

        cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
        httpUrl = HttpUrl.parse(URL);
        List<Cookie> cookies = cookieStore.getCookie(httpUrl);
        Log.d(mTag, httpUrl.host() + "对应的cookie如下：" + cookies.toString());
        cookies = cookieStore.getAllCookie();
        Log.d(mTag, "所有cookie如下：" + cookies.toString());
    }

    private void clearCookie() {
        HttpUrl httpUrl = HttpUrl.parse(URL);
        CookieStore cookieStore = OkGo.getInstance().getCookieJar().getCookieStore();
        cookieStore.removeCookie(httpUrl);
        cookieStore.removeAllCookie();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
        //OkGo.getInstance().cancelAll();
        //OkGo.cancelAll(new OkHttpClient());
        //OkGo.cancelTag(new OkHttpClient(),this);

        this.clearCookie();
    }
}
