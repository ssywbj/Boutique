package com.suheng.ssy.boutique.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class NodeProgressBar extends View {

    private Paint mPaint;
    //private int mBackgroundColor = Color.parseColor("#EEEEEE");
    private int mBackgroundColor = Color.parseColor("#000000");
    private int mForegroundColor = Color.parseColor("#F64C2B");
    private int mRadius = 18;
    private int mMax = 100;
    private int mProgress;
    private int mNodes = 6;

    public NodeProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NodeProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(10);
    }

    public void setProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > mMax) {
            progress = mMax;
        }
        mProgress = progress;

        invalidate();
    }

    public void setMax(int max) {
        mMax = max;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mBackgroundColor);
        canvas.drawLine(mRadius / 2, mRadius, getWidth() - mRadius / 2, mRadius, mPaint);//绘制灰色的背景线条

        if (mMax > 0) {
            mPaint.setColor(mForegroundColor);
            int progressWidth = (int) (1.0 * mProgress / mMax * (getWidth() - mRadius));
            canvas.drawLine(mRadius / 2, mRadius, progressWidth, mRadius, mPaint);

            if (mNodes > 1) {
                final int divideWidth = (getWidth() - mRadius * 2) / (mNodes - 1);

                final int divide = (int) (1.0 * mMax / (mNodes - 1));
                int selectedNotes = mProgress / divide;
                if (mProgress > 0 && (mProgress % divide) >= 0) {
                    selectedNotes++;
                }

                for (int index = 0; index < mNodes; index++) {
                    if (index < selectedNotes) {
                        mPaint.setColor(mForegroundColor);
                        canvas.drawCircle(mRadius + index * divideWidth, mRadius, mRadius, mPaint);
                        mPaint.setColor(Color.parseColor("#FFFFFF"));
                        canvas.drawCircle(mRadius + index * divideWidth, mRadius, mRadius - 8, mPaint);
                    } else {
                        mPaint.setColor(mBackgroundColor);
                        canvas.drawCircle(mRadius + index * divideWidth, mRadius, mRadius, mPaint);
                    }
                }
            }
        }
    }

}
