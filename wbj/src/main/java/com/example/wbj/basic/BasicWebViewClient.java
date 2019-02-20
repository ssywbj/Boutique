package com.example.wbj.basic;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.ref.WeakReference;

public class BasicWebViewClient extends WebViewClient {

    public static final String TAG = BasicWebViewClient.class.getSimpleName();
    private static final int MSG_TIMEOUT = 1;
    private WebView mWebView;
    private WeakHandler mHandler;
    private int mTimeout;

    public BasicWebViewClient() {
        mHandler = new WeakHandler(this);
    }

    public BasicWebViewClient(int timeout) {
        this();
        mTimeout = timeout;
    }

    @Override
    public void onPageStarted(final WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (mWebView == null) {
            mWebView = view;
        }
        if (mTimeout > 0) {
            mHandler.sendEmptyMessageDelayed(MSG_TIMEOUT, mTimeout);
        }
        Log.i(TAG, "-->start-->" + url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        this.removeTimeoutMsg();
        Log.i(TAG, "-->finish-->" + url + ", " + view.getProgress());
    }

    private void removeTimeoutMsg() {
        if (mHandler.hasMessages(MSG_TIMEOUT)) {
            mHandler.removeMessages(MSG_TIMEOUT);
        }
    }

    @Override//在Android 6.0及以上调用
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        Log.e(TAG, "-->onReceivedError Error-->" + error.getErrorCode() + ", " + error.getDescription());
        this.loadError();
    }

    @Override//在Android 6.0及以下调用
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        Log.e(TAG, "-->onReceivedError Deprecated-->" + errorCode + ", " + description);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return;//避免Android 6.0重复调用显示错误页
        }
        this.loadError();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        Log.d(TAG, "-->shouldOverrideUrlLoading-->" + view.getUrl());
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override//post请求不会回调这个函数，可比较shouldInterceptRequest方法
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.d(TAG, "-->shouldOverrideUrlLoading Deprecated-->" + url);
        /*if (url.equals("onething://closeWebview/")) {//"onething://closeWebview/"为自定义伪协议
            return true;//返回true，WebView将失去重定向功能，我们可以在此自定义自己的操作
        }*/
        return false;//返回false，WebView将会自动加载新url，再次执行各个回调函数
    }

    private void loadError() {
        Log.e(TAG, "-->load error-->");
        mWebView.loadUrl("file:///android_asset/error/sorry.html");
    }

    private void loadTimeout() {
        if (mWebView.getProgress() < 100) {
            Log.e(TAG, "-->load timeout-->" + mWebView.getProgress());
            this.loadError();
        }
    }

    private static class WeakHandler extends Handler {

        private WeakReference<BasicWebViewClient> mWeakReference;

        private WeakHandler(BasicWebViewClient instance) {
            mWeakReference = new WeakReference<>(instance);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BasicWebViewClient instance = mWeakReference.get();
            if (instance == null) {
                return;
            }

            switch (msg.what) {
                case MSG_TIMEOUT:
                    instance.loadTimeout();
                    break;
                default:
                    break;
            }
        }
    }

}