package com.suheng.ssy.boutique.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.suheng.ssy.boutique.R;

import java.lang.ref.WeakReference;

public class ScoreChangeView extends View {
    private static final String[] SCORES = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private Paint.FontMetricsInt mScoreFontMetrics;
    private Rect mRectDraw;
    private Paint mScorePaint;
    private Rect mScoreRect;

    private int mScoreSingleBitsX;
    private int mScoreTenBitsX;
    private int mScrollY;
    private int mDifferenceValue;
    private int mScoreX;
    private int mScoreWidth;
    private int mScoreHeight;
    private int mScoreBaseLine;

    private int mSingleBits = 0;
    private int mTenBits;
    private int mHundredBits;
    private int mCurrentScore = 100;
    private int mOrientation = 1;
    private int mScoreSize;
    private int mScoreColor;
    private int mNextX;
    private int mNextTenBitsX;
    private int mSize;
    private float mOneBitScoreWidth;
    private long mScoreSpeed = 5;
    private ScoreChangeListener mScoreChangeListener;
    private Handler mHandler = new WeakHandler(this);

    public ScoreChangeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScoreChangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.getAttributes(context, attrs);
        this.initPaint();

        mRectDraw = new Rect();
        mScoreRect = new Rect();
        mScorePaint.getTextBounds("000", 0, "000".length(), new Rect());
        mOneBitScoreWidth = mScorePaint.measureText("0");
        mScoreWidth = (int) (3 * mOneBitScoreWidth);
    }

    public void setScoreChangeListener(ScoreChangeListener scoreChangeListener) {
        this.mScoreChangeListener = scoreChangeListener;
    }

    private void whenScoreChange() {
        mScrollY = mScrollY + mOrientation * mScoreHeight / 10;
        if (mOrientation * mScrollY >= mScoreHeight) {
            this.updateOrientation();
            mScrollY = 0;
            if (mOrientation == 1) {
                this.scoreIncrease();
            } else {
                this.scoreSubtract();
            }
            mDifferenceValue -= mOrientation;

            if (mScoreChangeListener != null) {
                mScoreChangeListener.onScoreChange(Integer.parseInt(SCORES[mHundredBits]
                        + SCORES[mTenBits] + SCORES[mSingleBits]) + mOrientation);
            }
        }
        if (mDifferenceValue != 0) {
            mHandler.sendEmptyMessageDelayed(-1, mScoreSpeed);
        }

        invalidate();
    }

    private void scoreSubtract() {
        if (mSingleBits > 0) {
            mSingleBits--;
        } else if (mTenBits > 0) {
            mSingleBits = SCORES.length - 1;
            mTenBits--;
        } else if (mHundredBits >= 0) {
            mTenBits = SCORES.length - 1;
            mSingleBits = SCORES.length - 1;
            mHundredBits--;
        }
    }

    private void scoreIncrease() {
        if (mSingleBits < SCORES.length - 1) {
            mSingleBits++;
        } else if (mSingleBits >= SCORES.length - 1 && mTenBits < SCORES.length - 1) {
            mTenBits++;
            mSingleBits = 0;
        } else if (mTenBits >= SCORES.length - 1) {
            mHundredBits++;
            mSingleBits = 0;
            mTenBits = 0;
        }
    }

    private void getAttributes(Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScoreChangeView);
        mScoreSpeed = typedArray.getInt(R.styleable.ScoreChangeView_number_scroll_speed, 10);
        mScoreSize = typedArray.getDimensionPixelSize(R.styleable.ScoreChangeView_number_size, UnitUtil.sp2px(context, 50f));
        mScoreColor = typedArray.getColor(R.styleable.ScoreChangeView_number_color, Color.GREEN);

        mSize = typedArray.getDimensionPixelSize(R.styleable.ScoreChangeView_size, (int) UnitUtil.dip2px(context, 160f));

        typedArray.recycle();
    }

    private void initPaint() {
        mScorePaint = new Paint();
        mScorePaint.setAntiAlias(true);
        mScorePaint.setTextSize(mScoreSize);
        mScorePaint.setColor(mScoreColor);
        mScorePaint.setTypeface(Typeface.create("sans-serif-thin", Typeface.BOLD));
        mScoreFontMetrics = mScorePaint.getFontMetricsInt();
    }

    private void calculateCoordinator() {
        mRectDraw.left = (getMeasuredWidth() - mSize) / 2;
        mRectDraw.top = (getMeasuredHeight() - mSize) / 2;
        mRectDraw.right = (getMeasuredWidth() + mSize) / 2;
        mRectDraw.bottom = (getMeasuredHeight() + mSize) / 2;

        mScoreX = mRectDraw.centerX() - mScoreWidth / 2;
        int scoreY = (getMeasuredHeight() - mScoreHeight) / 2;

        mScoreHeight = mScoreFontMetrics.descent - mScoreFontMetrics.ascent;
        mScoreBaseLine = scoreY - mScoreFontMetrics.ascent;
        mScoreRect.set(mScoreX, scoreY, (mScoreX + mScoreWidth), scoreY + mScoreHeight);
        mScoreSingleBitsX = mScoreX + (int) (2 * mOneBitScoreWidth);
        mScoreTenBitsX = mScoreX + (int) mOneBitScoreWidth;
        this.setNextNumCoordinatorX();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = this.measureSize(widthMeasureSpec, true);
        int height = this.measureSize(heightMeasureSpec, false);
        setMeasuredDimension(width, height);
    }

    private int measureSize(int measureSpec, boolean isWidth) {
        int size = 0;
        int measureMode = MeasureSpec.getMode(measureSpec);
        int measureSize = MeasureSpec.getSize(measureSpec);

        if (measureMode == MeasureSpec.EXACTLY) {
            size = measureSize;
        } else if (measureMode == MeasureSpec.AT_MOST && isWidth) {
            size = mSize;
        } else if (measureMode == MeasureSpec.AT_MOST) {
            size = mSize;
        }
        return size;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.calculateCoordinator();
        this.drawScore(canvas);
    }

    private void drawScore(Canvas canvas) {
        canvas.save();
        canvas.clipRect(mScoreRect);
        //draw 个位
        canvas.drawText(SCORES[mSingleBits], mScoreSingleBitsX, mScoreBaseLine + mScrollY, mScorePaint);
        if ((mOrientation == 1 && mSingleBits < SCORES.length - 1) || (mOrientation == -1 && mSingleBits > 0)) {
            canvas.drawText(SCORES[mSingleBits + mOrientation], mNextX, mScoreBaseLine + mScrollY -
                    mOrientation * mScoreHeight, mScorePaint);
        } else {
            canvas.drawText(mOrientation == 1 ? SCORES[0] : SCORES[SCORES.length - 1],
                    mNextX, mScoreBaseLine + mScrollY - mOrientation * mScoreHeight, mScorePaint);
        }

        //draw 十位
        if ((mOrientation == 1 && mSingleBits == 9) || (mOrientation == -1 && mSingleBits == 0)) {
            if (mHundredBits != 0 || mTenBits != 0) {
                canvas.drawText(SCORES[mTenBits], mScoreTenBitsX, mScoreBaseLine + mScrollY, mScorePaint);
            }
            if ((mOrientation == 1 && mTenBits < SCORES.length - 1) || (mOrientation == -1 && mTenBits > 0)) {
                if (mTenBits + mOrientation != 0) {

                    canvas.drawText(SCORES[mTenBits + mOrientation], mNextTenBitsX, mScoreBaseLine +
                            mScrollY - mOrientation * mScoreHeight, mScorePaint);
                }
            } else {
                canvas.drawText(mOrientation == 1 ? SCORES[0] : SCORES[SCORES.length - 1],
                        mNextTenBitsX, mScoreBaseLine + mScrollY - mOrientation * mScoreHeight, mScorePaint);
            }
        } else {
            if (mHundredBits != 0 || mTenBits != 0) {
                canvas.drawText(SCORES[mTenBits], mScoreTenBitsX, mScoreBaseLine, mScorePaint);
            }
        }

        //draw 百位
        if ((mOrientation == 1 && mTenBits == 9 && mSingleBits == 9) || (mOrientation == -1 &&
                mTenBits == 0 && mSingleBits == 0)) {
            if (mHundredBits != 0) {
                canvas.drawText(SCORES[mHundredBits], mScoreX, mScoreBaseLine + mScrollY, mScorePaint);
            }
            if (mHundredBits + mOrientation > 0) {
                canvas.drawText(SCORES[mHundredBits + mOrientation], mScoreX
                        , mScoreBaseLine + mScrollY - mScoreHeight, mScorePaint);
            }
        } else if (mHundredBits != 0) {
            canvas.drawText(SCORES[mHundredBits], mScoreX, mScoreBaseLine, mScorePaint);
        }

        canvas.restore();
    }

    private void setNextNumCoordinatorX() {
        if (mHundredBits > 0) {
            mScoreTenBitsX = (int) (mScoreX + mOneBitScoreWidth);
            mNextTenBitsX = (int) ((getMeasuredWidth() - 2 * mOneBitScoreWidth) / 2);
        } else if (mTenBits > 0) {
            mScoreTenBitsX = (int) ((getMeasuredWidth() - 2 * mOneBitScoreWidth) / 2);

            if (mOrientation == 1 && mTenBits == 9 && mSingleBits == 9) {
                mNextX = (int) (mScoreX + 2 * mOneBitScoreWidth);
                mNextTenBitsX = (int) (mScoreX + mOneBitScoreWidth);
            } else {
                mNextTenBitsX = (int) ((getMeasuredWidth() - 2 * mOneBitScoreWidth) / 2);
            }
            if (mOrientation == -1 && mTenBits == 1 && mSingleBits == 0) {
                mNextX = (int) ((getMeasuredWidth() - mOneBitScoreWidth) / 2);
                return;
            }
        } else {
            mNextX = (int) ((getMeasuredWidth() - mOneBitScoreWidth) / 2);
            mScoreSingleBitsX = (int) ((getMeasuredWidth() - mOneBitScoreWidth) / 2);
            if (mOrientation == 1 && mSingleBits == 9) {
                mNextX = (int) ((getMeasuredWidth() - 2 * mOneBitScoreWidth) / 2 + mOneBitScoreWidth);
            }
            return;
        }
        mNextX = (int) (mNextTenBitsX + mOneBitScoreWidth);
        mScoreSingleBitsX = (int) (mScoreTenBitsX + mOneBitScoreWidth);
    }

    public synchronized void scoreChange(int change) throws Exception {
        if (change == 0) {
            return;
        }
        mDifferenceValue += change;
        mCurrentScore += change;
        if (mCurrentScore < 0 || mCurrentScore > 100) {
            throw new Exception("score more than range");
        }
        if (mDifferenceValue == change) {
            this.updateOrientation();
            mHandler.sendEmptyMessageDelayed(-1, mScoreSpeed);
        }
    }

    private void updateOrientation() {
        if (mDifferenceValue > 0) {
            mOrientation = 1;
        } else {
            mOrientation = -1;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void setDefaultScore(int score) {
        mHundredBits = score / 100;
        mTenBits = score % 100 / 10;
        mSingleBits = score % 100 % 10;

        mCurrentScore = score;
        /*mDifferenceValue = 0;

        this.updateOrientation();
        mHandler.sendEmptyMessageDelayed(-1, mScoreSpeed);*/
    }

    public void setScoreColor(int color) {
        mScoreColor = color;
        mScorePaint.setColor(mScoreColor);

        invalidate();
    }

    public interface ScoreChangeListener {
        void onScoreChange(int score);
    }

    private static class WeakHandler extends Handler {
        WeakReference<ScoreChangeView> mWeakReference;

        WeakHandler(ScoreChangeView scoreChangeView) {
            mWeakReference = new WeakReference<>(scoreChangeView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mWeakReference.get() != null) {
                mWeakReference.get().whenScoreChange();
            }
        }
    }
}
