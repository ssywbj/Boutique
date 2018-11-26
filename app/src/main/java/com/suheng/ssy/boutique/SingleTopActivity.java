package com.suheng.ssy.boutique;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

public class SingleTopActivity extends LaunchTypeActivity {

    private TextView mTextInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_standard);
        mTextInfo = findViewById(R.id.text_single_top);
        mTextInfo.setText(mTextInfo.getText().toString() + ", " + hashCode());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mTextInfo.setText(mTextInfo.getText().toString() + ", 位于栈顶端属同一个Activity-onNewIntent:" + hashCode());
    }

}
