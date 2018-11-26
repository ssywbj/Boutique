package com.suheng.ssy.boutique;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class LaunchStandardActivity extends LaunchTypeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_standard);
        mTextInfo = findViewById(R.id.text_standard);
        mTextInfo.setText(mTextInfo.getText().toString() + ", " + hashCode());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mTextInfo.setText(mTextInfo.getText().toString() + ", onNewIntent: " + hashCode());
    }


}
