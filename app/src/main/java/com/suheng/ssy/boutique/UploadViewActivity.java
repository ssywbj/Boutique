package com.suheng.ssy.boutique;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.suheng.upload.UploadView;

import java.lang.ref.WeakReference;

public class UploadViewActivity extends BasicActivity {
    public static final String TAG = UploadViewActivity.class.getSimpleName();
    private static final int TIME_UPDATE_PROGRESS = 200;
    private static final int MSG_UPDATE_PROGRESS = 1;
    private UploadView mUploadView;
    private WeakHandler mHandler = new WeakHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_view_aty);

        mUploadView = findViewById(R.id.upload_view);
        mUploadView.setPicture(R.drawable.chat_picture);
        //mUploadView.setPicture(getDrawable(R.drawable.chat_picture));
        //mUploadView.setPicture(BitmapFactory.decodeResource(getResources(), R.drawable.chat_picture));
        //mUploadView.setPicture(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        //        .getAbsolutePath() + File.separator + "微信图片_20190610162100.jpg");
    }

    private void updateProgress(double percent) {
        if (percent >= 1.0) {
            percent = 1.0;
            mHandler.removeMessages(MSG_UPDATE_PROGRESS);
        } else {
            Message msg = new Message();
            msg.what = MSG_UPDATE_PROGRESS;
            msg.obj = (percent + 0.17);
            mHandler.sendMessageDelayed(msg, TIME_UPDATE_PROGRESS);
        }

        Log.d(TAG, "percent-->" + percent);
        mUploadView.updateProgress(percent);
    }

    public void upload(View view) {
        mUploadView.resetProgress();

        mHandler.removeMessages(MSG_UPDATE_PROGRESS);
        Message msg = new Message();
        msg.what = MSG_UPDATE_PROGRESS;
        msg.obj = 0.11;
        mHandler.sendMessageDelayed(msg, TIME_UPDATE_PROGRESS);
    }

    private static class WeakHandler extends Handler {
        private WeakReference<UploadViewActivity> mWeakReference;

        private WeakHandler(UploadViewActivity instance) {
            mWeakReference = new WeakReference<>(instance);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            UploadViewActivity instance = mWeakReference.get();
            if (instance == null) {
                return;
            }

            switch (msg.what) {
                case MSG_UPDATE_PROGRESS:
                    instance.updateProgress((Double) msg.obj);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler.hasMessages(MSG_UPDATE_PROGRESS)) {
            mHandler.removeMessages(MSG_UPDATE_PROGRESS);
        }
    }
}
