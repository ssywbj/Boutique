package com.example.wbj;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.wbj.view.SmoothProgressBar;
import com.wbj.view.UploadView;

import java.lang.ref.WeakReference;

public class ProgressBarActivity extends AppCompatActivity {
    public static final String TAG = ProgressBarActivity.class.getSimpleName();
    private static final int TIME_UPDATE_PROGRESS = UploadView.UPDATE_ANIM_DURATION;
    private static final int MSG_UPDATE_PROGRESS = 1;
    private static final int MSG_UPDATE_PROGRESS_ENDS = 2;
    private UploadView mUploadView;
    private WeakHandler mHandler = new WeakHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_bar_aty);

        mUploadView = findViewById(R.id.upload_view);

        mBthEnds =  findViewById(R.id.btn_ends);
        mProgressBarEnds = (SmoothProgressBar) findViewById(R.id.pb_both_ends);
        mProgressBarEnds.setMax(MAX_PROGRESS);
        mProgressBarEnds.setProgress(MAX_PROGRESS);
        mProgressBar1 = findViewById(R.id.progress_bar1);
        mProgressBar1.setMax(MAX_PROGRESS);
        mProgressBar1.setProgress(23);

        mDrawableEndsNote = getResources().getDrawable(R.drawable.ends_progress_note);
        mDrawableEndsNormal = getResources().getDrawable(R.drawable.ends_progress_normal);
        mProgressBarEnds.setProgressDrawable(mDrawableEndsNormal);

        /*CommonDialog commonDialog = new CommonDialog(this, "标题", "内容内容内容内容", "取消", "确认");
        commonDialog.setOnClickDialog(new CommonDialog.OnClickDialog() {
            @Override
            public void clickLeftBtn() {
                Toast.makeText(ProgressBarActivity.this, "Left Button", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void clickRightBtn() {
                finish();
            }
        });
        commonDialog.show();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler.hasMessages(MSG_UPDATE_PROGRESS)) {
            mHandler.removeMessages(MSG_UPDATE_PROGRESS);
        }
        if (mHandler.hasMessages(MSG_UPDATE_PROGRESS_ENDS)) {
            mHandler.removeMessages(MSG_UPDATE_PROGRESS_ENDS);
        }
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

    private static final int MAX_PROGRESS = 100;
    private Button mBthEnds;
    private ProgressBar mProgressBarEnds, mProgressBar1;
    private Drawable mDrawableEndsNormal, mDrawableEndsNote;
    private int mCurrentProgress;
    private int mTakeTime = TIME_UPDATE_PROGRESS;

    public void reduceBothEnds(View view) {
        mTakeTime = TIME_UPDATE_PROGRESS;
        mCurrentProgress = MAX_PROGRESS;

        mProgressBar1.setProgress(79);

        mProgressBarEnds.setProgress(mCurrentProgress);
        mProgressBarEnds.setProgressDrawable(mDrawableEndsNormal);
        if (mHandler.hasMessages(MSG_UPDATE_PROGRESS_ENDS)) {
            mHandler.removeMessages(MSG_UPDATE_PROGRESS_ENDS);
        }
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_PROGRESS_ENDS, TIME_UPDATE_PROGRESS);
    }

    private void updateProgressEnds() {
        mTakeTime += TIME_UPDATE_PROGRESS;
        mCurrentProgress -= 6;
        if (mCurrentProgress <= 0) {
            mCurrentProgress = 0;
            mHandler.removeMessages(MSG_UPDATE_PROGRESS_ENDS);
        } else {
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_PROGRESS_ENDS, TIME_UPDATE_PROGRESS);
        }

        mBthEnds.setText(1.0f * mTakeTime / 1000 + "s");

        if (mCurrentProgress <= 27) {
            if (mProgressBarEnds.getProgressDrawable() == mDrawableEndsNormal) {
                mProgressBarEnds.setProgressDrawable(mDrawableEndsNote);
            }
        }
        mProgressBarEnds.setProgress(mCurrentProgress);
    }

    private static class WeakHandler extends Handler {
        private WeakReference<ProgressBarActivity> mWeakReference;

        private WeakHandler(ProgressBarActivity instance) {
            mWeakReference = new WeakReference<>(instance);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ProgressBarActivity instance = mWeakReference.get();
            if (instance == null) {
                return;
            }

            switch (msg.what) {
                case MSG_UPDATE_PROGRESS:
                    instance.updateProgress((Double) msg.obj);
                    break;
                case MSG_UPDATE_PROGRESS_ENDS:
                    instance.updateProgressEnds();
                    break;
                default:
                    break;
            }
        }
    }

}
