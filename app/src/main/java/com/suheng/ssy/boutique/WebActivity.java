package com.suheng.ssy.boutique;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.gyf.barlibrary.ImmersionBar;

public class WebActivity extends BasicActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ImmersionBar.with(this).navigationBarColor(R.color.colorPrimary).init();
        ImmersionBar.with(this).titleBar(R.id.toolbar)
                .keyboardEnable(true)
                .init();
        setSupportActionBar(((Toolbar) findViewById(R.id.toolbar)));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mWebView = findViewById(R.id.web_view);
        mWebView.loadUrl("http://192.168.120.169:8080/TestJSP/index.jsp");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mWebView.removeAllViews();
            mWebView.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
