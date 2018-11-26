package com.suheng.ssy.boutique;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class SingleTaskActivity extends LaunchTypeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_standard);
        mTextInfo = findViewById(R.id.text_single_task);
        mTextInfo.setText(mTextInfo.getText().toString() + ", " + hashCode());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //SingleTask模式启动的Activity只在该栈具有唯一性
        mTextInfo.setText(mTextInfo.getText().toString() + ", 位于栈中属同一个Activity-onNewIntent:" + hashCode());
    }

}
