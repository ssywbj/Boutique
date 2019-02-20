package com.wbj.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.wbj.R;

public class UploadView extends FrameLayout {
    private static final String TAG = UploadView.class.getSimpleName();
    public static final int UPDATE_ANIM_DURATION = 100;
    private static final int MAX_PROGRESS = 100;
    private ProgressBar mProgressBar;
    private ImageView mImageView;

    public UploadView(Context context) {
        this(context, null);
    }

    public UploadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UploadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.upload_view, this);

        mImageView = (ImageView) findViewById(R.id.upload_image);

        mProgressBar = (ProgressBar) findViewById(R.id.upload_progress);
        mProgressBar.setMax(MAX_PROGRESS);
        this.resetProgress();

        post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                Log.i(TAG, "width = " + layoutParams.width + ",height = " + layoutParams.height);
                if (layoutParams.width <= 0 || layoutParams.height <= 0) {
                    layoutParams.width = 288;
                    layoutParams.height = 360;
                    setLayoutParams(layoutParams);
                }

                ViewGroup.LayoutParams pbLParams = mProgressBar.getLayoutParams();
                pbLParams.height = layoutParams.width;
                pbLParams.width = layoutParams.height;
                mProgressBar.setLayoutParams(pbLParams);
                mProgressBar.setRotation(90);
            }
        });
    }

    public void resetProgress() {
        mProgressBar.setProgress(MAX_PROGRESS);
    }

    public void updateProgress(double progress) {
        this.updateProgress((int) (progress * MAX_PROGRESS));
    }

    public void updateProgress(int progress) {
        if (progress <= 0) {
            return;
        }
        if (progress >= MAX_PROGRESS) {
            progress = MAX_PROGRESS;
        }
        if (mProgressBar.getProgress() > 0) {
            this.smoothUpdateProgress(MAX_PROGRESS - progress);
        }
    }

    private void smoothUpdateProgress(int progress) {
        if (Build.VERSION.SDK_INT >= 11) {//平滑更新进度条(比起更新时一跳一跳的效果好很多)
            ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", progress);
            animation.setDuration(UPDATE_ANIM_DURATION);
            animation.setInterpolator(new LinearInterpolator());
            animation.start();
        } else {
            mProgressBar.setProgress(progress);
        }
    }

    public ImageView getImageView() {
        return mImageView;
    }

}
