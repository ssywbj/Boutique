package com.suheng.ssy.boutique.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;

import com.suheng.ssy.boutique.R;

public class ThresholdProgressBar extends ProgressBar {
    public static final int THRESHOLD_LINE_MARGIN = 8;
    private Paint mPaint;
    private String mTextThreshold;
    private Rect mBounds = new Rect();
    /*** 阈值 */
    private int mThreshold;

    public ThresholdProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ThresholdProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        mPaint = this.getPaint();
    }

    /**
     * 获取默认颜色的画笔
     */
    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(18f);
        paint.setStrokeWidth(2f);
        paint.setColor(getResources().getColor(R.color.red));
        return paint;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mThreshold > 0) {
            final int height = getHeight();
            final int radius = height / 2;
            final int width = getWidth();

            float startX = 1.0f * mThreshold / getMax() * width;
            float startY = 0, stopY = height;
            if (startX - width >= 0) {
                canvas.drawCircle(width - 2f, radius, 3, mPaint);
            } else {
                if ((radius - startX > 0)) {
                    float hArc = (float) (Math.sqrt(Math.pow(radius, 2) - Math.pow((radius - startX), 2)));
                    startY = (radius - hArc);
                    stopY = (startY + 2 * hArc);
                } else if ((width - radius) < startX) {
                    float hArc = (float) (Math.sqrt(Math.pow(radius, 2) - Math.pow((startX - (width - radius)), 2)));
                    startY = (radius - hArc);
                    stopY = (startY + 2 * hArc);
                }

                canvas.drawLine(startX, startY, startX, stopY, mPaint);
            }

            final float textWidth = mPaint.measureText(mTextThreshold);
            if (startX + textWidth + THRESHOLD_LINE_MARGIN < width) {
                startX += THRESHOLD_LINE_MARGIN;
            } else {
                startX -= (textWidth + THRESHOLD_LINE_MARGIN);
            }
            mBounds.setEmpty();
            mPaint.getTextBounds(mTextThreshold, 0, mTextThreshold.length(), mBounds);
            Log.d("Wbj", "onDraw: text width = " + textWidth + ", text size = " + mPaint.getTextSize()
                    + ", rect width = " + mBounds.width() + ", rect height = " + mBounds.height());
            canvas.drawText(mTextThreshold, startX, (height + mBounds.height()) / 2, mPaint);//文字垂直居中
            //canvas.drawLine(0, radius, width, radius, mPaint);
        }
    }

    public void setThreshold(int threshold) {
        if (threshold < 0) {
            threshold = 0;
        } else if (threshold > getMax()) {
            threshold = getMax();
        }
        mThreshold = threshold;

        mTextThreshold = (mThreshold + "M");

        invalidate();
    }

}
