package com.suheng.ssy.boutique;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.suheng.ssy.boutique.dagger.DaggerActivityComponent;

public class SingleTopActivity extends LaunchTypeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_standard);
        mTextInfo = findViewById(R.id.text_single_top);
        mTextInfo.setText(mTextInfo.getText().toString() + ", " + hashCode());

        //注入的时候，因为依赖了AppComponent，需要用appComponent方法把AppComponent实例添加进来，以免报错
        DaggerActivityComponent.builder().appComponent(((BoutiqueApp) getApplication())
                .getAppComponent()).build().inject(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        /*singleTop的Activity如果处于栈顶，那么启动Activity时不会新开一个Activity，
        而是会调用onNewIntent方法处理Intent事件；否则就会新开一个Activity*/
        mTextInfo.setText(mTextInfo.getText().toString() + ", 位于栈顶端属同一个Activity-onNewIntent:" + hashCode());
    }

}
