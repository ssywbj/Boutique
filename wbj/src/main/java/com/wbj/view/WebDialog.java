package com.wbj.view;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.example.wbj.R;
import com.example.wbj.basic.BasicDialog;
import com.example.wbj.basic.BasicWebViewClient;

public class WebDialog extends BasicDialog {
    private WebView mWebView;

    public WebDialog(Context context, String url) {
        super(context);
        setCancelable(true);
        this.loadUrl(url);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_web;
    }

    @Override
    protected void init() {
        mWebView = (WebView) findViewById(R.id.dialog_web_view);
        mWebView.setBackgroundColor(0);//设置透明背景
        mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(false);
    }

    private void loadUrl(String url) {
        mWebView.setWebViewClient(new DialogWebClient());
        mWebView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Log.i(BasicWebViewClient.TAG, "onBackPressed: onBackPressed");
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public void dismiss() {
        super.dismiss();
        mWebView.stopLoading();
        mWebView.destroy();
    }*/

    private class DialogWebClient extends BasicWebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.i(BasicWebViewClient.TAG, "dialog, onPageFinished: ");
            show();

            String js = "Array.prototype.slice.call(document.getElementsByTagName('a')).forEach(function(a) {\n" +
                    "\tvar href = a.href;\n" +
                    "\tvar thref = a.getAttribute('thunderhref');\n" +
                    "\tvar reg = /^(thunder|magnet|ed2k|ftp):/i;\n" +
                    "\tvar doit = function(o,src) {\n" +
                    "\t\to.href = src;\n" +
                    "\t\to.style.color='red';\n" +
                    "\t\to.style.backgroundColor='yellow';\n" +
                    "\t\to.style.fontSize='2em';\n" +
                    "\t\to.removeAttribute('onclick');\n" +
                    "\t\to.removeAttribute('oncontextmenu');\n" +
                    "\t\to.removeAttribute('ontouchstart');\n" +
                    "\t\to.removeAttribute('ontouchend');\n" +
                    "\t};\n" +
                    "\tvar disableLink = function(o) {\n" +
                    "\t\treturn;\n" +
                    "\t\to.style.pointerEvents='none';\n" +
                    "\t};\n" +
                    "\treg.test(thref) ? doit(a,thref) : (reg.test(href) ? doit(a,href) : disableLink(a));\n" +
                    "\t;\n" +
                    "});";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                view.evaluateJavascript(js, null);
            } else {
                view.loadUrl(js);
            }
        }
    }

}
