package com.suheng.ssy.boutique;

import android.os.Bundle;
import android.view.View;

import com.suheng.widget.SmoothUploadView;


public class UploadViewActivity extends BasicActivity {
    private SmoothUploadView mSmoothUploadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_view_aty);
        mSmoothUploadView = findViewById(R.id.smooth_upload_view);
        mSmoothUploadView.setMaxProgress(1000);
        mSmoothUploadView.setImageResource(R.drawable.lv_person_andy);
    }

    public void onClickUpload(View view) {
        mSmoothUploadView.resetProgress();
        //mSmoothUploadView.updateProgress(1000);
        mSmoothUploadView.updateProgress(1.0);
    }
}
