package com.wbj.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

public class SmoothProgressBar extends ProgressBar {
    public static final int UPDATE_ANIM_DURATION = 100;
    private ValueAnimator mProgressUpdateAnim;

    public SmoothProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmoothProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public synchronized void setProgress(int progress) {
        if (mProgressUpdateAnim != null) {
            mProgressUpdateAnim.cancel();
        }
        if (mProgressUpdateAnim == null) {
            mProgressUpdateAnim = ValueAnimator.ofInt(getProgress(), progress);
            mProgressUpdateAnim.setDuration(UPDATE_ANIM_DURATION);
            mProgressUpdateAnim.setInterpolator(new LinearInterpolator());
            mProgressUpdateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    SmoothProgressBar.super.setProgress((Integer) animation.getAnimatedValue());
                }
            });
        } else {
            mProgressUpdateAnim.setIntValues(getProgress(), progress);
        }
        mProgressUpdateAnim.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mProgressUpdateAnim != null) {
            mProgressUpdateAnim.cancel();
        }
    }

}
