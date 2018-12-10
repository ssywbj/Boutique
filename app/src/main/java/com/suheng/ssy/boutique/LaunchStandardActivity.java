package com.suheng.ssy.boutique;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.suheng.ssy.boutique.dagger.DaggerSingletonComponent;
import com.suheng.ssy.boutique.dagger.SingletonTestEntity;

import javax.inject.Inject;

public class LaunchStandardActivity extends LaunchTypeActivity {

    @Inject
    SingletonTestEntity mTestEntity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_standard);
        mTextInfo = findViewById(R.id.text_standard);
        //DaggerSingletonComponent.builder().build().inject(this);
        DaggerSingletonComponent.getInstance().inject(this);//单例的实例化方式
        mTextInfo.setText(mTextInfo.getText().toString() + ", " + hashCode() + ": " + mTestEntity);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mTextInfo.setText(mTextInfo.getText().toString() + ", onNewIntent: " + hashCode());
    }


}
