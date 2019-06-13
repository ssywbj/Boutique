package com.suheng.upload;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class UploadView extends FrameLayout {
    private static final String TAG = UploadView.class.getSimpleName();
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

        mImageView = findViewById(R.id.upload_image);

        mProgressBar = findViewById(R.id.upload_progress);
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

    private void smoothUpdateProgress(int progress) {//平滑更新进度条(比起更新时一跳一跳的效果好很多)
        ObjectAnimator animation = ObjectAnimator.ofInt(mProgressBar, "progress", progress);
        animation.setDuration(300);
        animation.setInterpolator(new LinearInterpolator());
        animation.start();
    }

    public void setPicture(Drawable drawable) {
        if (drawable != null) {
            mImageView.setImageDrawable(drawable);
        }
    }

    public void setPicture(int resId) {
        mImageView.setImageResource(resId);
    }

    public void setPicture(Bitmap bitmap) {
        if (bitmap != null) {
            mImageView.setImageBitmap(bitmap);
        }
    }

    /**
     * @param path 手机图片的完整路径。注：需要打开存储权限，否则获取到的Bitmap对象为空
     */
    public void setPicture(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        this.setPicture(BitmapFactory.decodeFile(path));
    }
}