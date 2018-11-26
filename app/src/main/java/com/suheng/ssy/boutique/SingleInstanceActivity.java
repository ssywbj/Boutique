package com.suheng.ssy.boutique;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

public class SingleInstanceActivity extends LaunchTypeActivity {

    private TextView mTextInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_standard);
        mTextInfo = findViewById(R.id.text_single_instance);
        mTextInfo.setText(mTextInfo.getText().toString() + ", " + hashCode());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //SingleInstance模式启动的Activity在系统中具有全局唯一性
        mTextInfo.setText(mTextInfo.getText().toString() + ", 已有实例不管是不是在同一个栈中Activity-onNewIntent:" + hashCode());
    }

}
