package com.suheng.ssy.boutique.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;


public class FaceEngine extends View {
    private static final float POINT_RADIUS = 5.0f;//圆点半径
    private static final int POINTS = 60;//绘制60个点
    private static double RADIANS = Math.toRadians(1.0f * 360 / POINTS);//弧度值，Math.toRadians：度换算成弧度
    private Paint mPaintText;
    private Paint mPaintPoint;
    private Rect mRect = new Rect();
    private Paint mPaint;
    private float mCenterX = 1.0f * getWidth() / 2;//圆心X坐标
    private float mCenterY = 1.0f * getHeight() / 2;//圆心Y坐标
    private float mMaxRadius;

    private float mHourPointWidth = 4;//时针宽度
    private float mMinutePointWidth = 3;//分针宽度
    private float mSecondPointWidth = 2;//秒针宽度
    private int mHourPointColor = Color.RED; //时针的颜色
    private int mMinutePointColor = Color.BLACK;//分针的颜色
    private int mSecondPointColor = Color.WHITE;//秒针的颜色
    private float mPointRadius = 2;//指针圆角
    private float mPointEndLength = 20;//指针末尾长度

    public FaceEngine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initPaint();
    }

    private void initPaint() {
        mPaintText = new Paint();
        mPaintText.setColor(Color.WHITE);
        mPaintText.setAntiAlias(true);
        mPaintText.setTextSize(50f);
        mPaintText.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));

        mPaintPoint = new Paint();
        mPaintPoint.setColor(Color.WHITE);
        mPaintPoint.setAntiAlias(true);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        mCenterX = 1.0f * getWidth() / 2;//圆心X坐标
        mCenterY = 1.0f * getHeight() / 2;//圆心Y坐标
        mMaxRadius = Math.min(mCenterX, mCenterY);

        int hour = Calendar.getInstance().get(Calendar.HOUR);// 时
        int minute = Calendar.getInstance().get(Calendar.MINUTE);// 分
        int second = Calendar.getInstance().get(Calendar.SECOND);// 秒

        canvas.drawColor(Color.parseColor("#00A3E5"));//画面背景
        this.paintPointer(canvas, hour, minute, second);
        this.paintScaleNumber(canvas);

        postInvalidateDelayed(1000);
    }

    private void paintScaleNumber(Canvas canvas) {
        canvas.save();

        mPaintText.setTextSize(26f);
        mPaintText.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        String digit = "0";
        mPaintText.getTextBounds(digit, 0, digit.length(), mRect);

        mPaintText.setStyle(Paint.Style.STROKE);
        float radius = mMaxRadius - 30;
        float radiusText = radius - mRect.height() - 4;

        mPaintText.setStyle(Paint.Style.FILL);
        float cxPoint, cyPoint, cxText, cyText;
        double sinValue, cosValue;
        for (int index = 0; index < POINTS; index++) {
            sinValue = Math.sin(RADIANS * index);
            cosValue = Math.cos(RADIANS * index);
            cxPoint = (float) (mCenterX + radius * sinValue);
            cyPoint = (float) (mCenterY - radius * cosValue);
            cxText = (float) (mCenterX - radiusText * sinValue);
            cyText = (float) (mCenterY - radiusText * cosValue);
            if (index % 5 == 0) {
                canvas.drawCircle(cxPoint, cyPoint, POINT_RADIUS, mPaintPoint);

                digit = String.valueOf((index / 5) == 0 ? 12 : 12 - (index / 5));
                mRect.setEmpty();
                mPaintText.getTextBounds(digit, 0, digit.length(), mRect);
                canvas.drawText(digit, cxText - 1.0f * mRect.width() / 2, cyText + 1.0f * mRect.height() / 2, mPaintText);
            } else {
                canvas.drawCircle(cxPoint, cyPoint, POINT_RADIUS / 2, mPaintPoint);
            }
        }

        canvas.restore();
    }

    private void paintPointer(Canvas canvas, int hour, int minute, int second) {
        float radius = mMaxRadius - 30;

        //转过的角度
        float angleHour = (hour + (float) minute / 60) * 360 / 12;
        float angleMinute = (minute + (float) second / 60) * 360 / 60;
        int angleSecond = second * 360 / 60;

        //绘制时针
        canvas.save();
        canvas.rotate(angleHour, mCenterX, mCenterY); // 旋转到时针的角度
        RectF rectHour = new RectF(mCenterX - mHourPointWidth / 2, mCenterY - radius * 3 / 6,
                mCenterX + mHourPointWidth / 2, mCenterY + mPointEndLength);
        mPaint.setColor(mHourPointColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mHourPointWidth);
        canvas.drawRoundRect(rectHour, mPointRadius, mPointRadius, mPaint);
        canvas.restore();
        //绘制分针
        canvas.save();
        canvas.rotate(angleMinute, mCenterX, mCenterY); //旋转到分针的角度
        RectF rectMinute = new RectF(mCenterX - mMinutePointWidth / 2, mCenterY - radius * 3.5f / 5,
                mCenterX + mMinutePointWidth / 2, mCenterY + mPointEndLength);
        mPaint.setColor(mMinutePointColor);
        mPaint.setStrokeWidth(mMinutePointWidth);
        canvas.drawRoundRect(rectMinute, mPointRadius, mPointRadius, mPaint);
        canvas.restore();
        //绘制分针
        canvas.save();
        canvas.rotate(angleSecond, mCenterX, mCenterY); //旋转到分针的角度
        RectF rectSecond = new RectF(mCenterX - mSecondPointWidth / 2, mCenterY - radius + 12,
                mCenterX + mSecondPointWidth / 2, mCenterY + mPointEndLength);
        mPaint.setStrokeWidth(mSecondPointWidth);
        mPaint.setColor(mSecondPointColor);
        canvas.drawRoundRect(rectSecond, mPointRadius, mPointRadius, mPaint);
        canvas.restore();

        //绘制原点
        canvas.save();
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCenterX, mCenterY, mSecondPointWidth * 4, mPaint);
        canvas.restore();
    }
}

