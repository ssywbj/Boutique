package com.suheng.ssy.boutique;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.suheng.ssy.boutique.dagger.DaggerActivityComponent;

public class SingleTaskActivity extends LaunchTypeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_standard);
        mTextInfo = findViewById(R.id.text_single_task);
        mTextInfo.setText(mTextInfo.getText().toString() + ", " + hashCode());

        //注入的时候，因为依赖了AppComponent，需要用appComponent方法把AppComponent实例添加进来，以免报错
        DaggerActivityComponent.builder().appComponent(((BoutiqueApp) getApplication())
                .getAppComponent()).build().inject(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        /*singleTask模式启动的Activity在栈中具有唯一性，只要它处于栈中，那么启动Activity时不会新开一个Activity，
        而是会调用onNewIntent方法处理Intent事件并把处于它上方的Activity移出栈（finish掉），让自己处于栈顶。*/
        mTextInfo.setText(mTextInfo.getText().toString() + ", 位于栈中属同一个Activity-onNewIntent:" + hashCode());
    }

}
