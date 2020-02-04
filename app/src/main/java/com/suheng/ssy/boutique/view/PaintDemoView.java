package com.suheng.ssy.boutique.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * 运用Paint绘制图形
 */
public class PaintDemoView extends View {
    private static final String TAG = PaintDemoView.class.getSimpleName();
    private static final int Y_BASIC_COORDINATE = 50;
    private Resources mResources;
    private DisplayMetrics mMetrics;
    private RectF mRectF;
    private Paint mPaint;
    private Path mPath;
    private Shader mShader;//颜色渐变的着色器

    public PaintDemoView(Context context) {
        super(context);
        initView(context);
    }

    public PaintDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mResources = context.getResources();
        mMetrics = getResources().getDisplayMetrics();
        mRectF = new RectF();
        mPaint = this.getPaint();
        mPath = new Path();

        mPaint.setShader(new SweepGradient(mMetrics.widthPixels / 2, Y_BASIC_COORDINATE * 6,
                new int[]{Color.parseColor("#08E08C"),
                        Color.parseColor("#000000"), Color.parseColor("#FF0000"),
                        Color.parseColor("#00ABE6"), Color.parseColor("#08E08C")}
                , new float[]{0.5f, 0.625f, 0.75f, 0.875f, 1f}));

        /*setMinimumHeight(mMetrics.heightPixels);
        setMinimumWidth(mMetrics.widthPixels);*/
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*String paintText = "我是画上去的";
        float paintTextWidth = mPaint.measureText(paintText);//根据画笔，测出文字的宽度
        Log.i(TAG, "paint text width = " + paintTextWidth);
        //画字 float x:X轴坐标；float y:Y轴坐标
        canvas.drawText(paintText, (mMetrics.widthPixels - paintTextWidth) / 2,
                Y_BASIC_COORDINATE, mPaint);//(mMetrics.widthPixels - paintTextWidth) / 2：水平居中*/

        //画圆 float cx:圆心的X轴坐标；float cy:圆心的Y轴坐标；float radius:半径
        mPaint.setShader(new SweepGradient(mMetrics.widthPixels / 2, Y_BASIC_COORDINATE * 6,
                new int[]{Color.GREEN, Color.BLACK, Color.RED, Color.BLUE/*, Color.GREEN*/},
                null));
        /*mPaint.setShader(new SweepGradient(mMetrics.widthPixels / 2, Y_BASIC_COORDINATE * 6,
                new int[]{Color.parseColor("#6568D6"),
                        Color.parseColor("#13C6F9"), Color.parseColor("#00ABE6"),
                        Color.parseColor("#8555D3")*//*, Color.GREEN*//*},
                null));*/
        canvas.drawCircle(mMetrics.widthPixels / 2, Y_BASIC_COORDINATE * 6, 250, mPaint);

        mPaint.setShader(new SweepGradient(mMetrics.widthPixels / 2, Y_BASIC_COORDINATE * 17,
                new int[]{Color.GREEN, Color.BLACK, Color.RED, Color.BLUE/*, Color.GREEN*/},
                new float[]{0.0f, 0.333333f, 0.666666f, 1.0f}));
        canvas.drawCircle(mMetrics.widthPixels / 2, Y_BASIC_COORDINATE * 17, 250, mPaint);

        /*
         * 画线-->float startX:线条起点的X轴坐标；float startY:线条起点的Y轴坐标；float
         * stopX:线条终点的X轴坐标；float stopY:线条终点的Y轴坐标
         */
        //横线-->Y轴坐标不变
        /*float startY = Y_BASIC_COORDINATE * 1.0f * 3;
        float startX = mMetrics.widthPixels / 4;
        float stopX = mMetrics.widthPixels / 4 * 3;
        canvas.drawLine(startX, startY, stopX, startY, mPaint);
        //竖线-->X轴坐标不变
        startX = mMetrics.widthPixels / 2;//水平居中
        float stopY = Y_BASIC_COORDINATE * 1.0f * 6;
        canvas.drawLine(startX, startY, startX, stopY, mPaint);
        //左斜线
        canvas.drawLine(startX, startY, mMetrics.widthPixels / 4, stopY, mPaint);
        //右斜线
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);
        //粗线条
        stopX += 30;
        canvas.drawLine(stopX, startY, stopX, stopY, getStoreWidthPaint(40));

        *//* 画矩形
         * float left:矩形左边所在的X坐标；float top:矩形顶边所在的Y坐标；
         * float right:矩形右边所在的X坐标；float bottom:矩形底边所在的Y坐标。
         *//*
        float left = 20;
        float right = 290;
        float top = Y_BASIC_COORDINATE * 1.0f * 7;
        float bottom = Y_BASIC_COORDINATE * 1.0f * 11;
        mRectF.set(left, top, right, bottom);
        canvas.drawRect(mRectF, getPaint());
        //空心正方形：right-left)=(bottom-top)
        left = right + 20;
        right = left + (bottom - top);
        mRectF.set(left, top, right, bottom);
        canvas.drawRect(mRectF, mPaint);

        *//* 画圆弧
         * RectF oval：确定圆弧形状和位置的矩形：
         * 1.圆弧的中心点为矩形的中心点；
         * 2.如果(right-left)=(bottom-top)，那么是圆形形状的弧形；反之，是椭圆形状的弧形
         * float startAngle：圆弧的开始角度（时钟3点的方向为0度）
         * float sweepAngle：圆弧的扫过角度（正数为顺时钟方向，负数为逆时钟方向）
         * boolean useCenter：圆弧是否与中心点连接成闭合区域
         *//*
        canvas.drawArc(mRectF, 0, 120, true, getPaint());
        left = right + 20;
        right = left + (bottom - top - 50);
        mRectF.set(left, top, right, bottom);
        canvas.drawRect(mRectF, mPaint);
        canvas.drawArc(mRectF, 0, 120, false, mPaint);
        canvas.drawArc(mRectF, 0, -120, false, getPaint());

        //画任意多边形：图形不封闭
        float point1X = 20;
        float point1Y = Y_BASIC_COORDINATE * 1.0f * 11 + 20;
        mPath.moveTo(point1X, point1Y);//操作的起点位置
        float point2X = point1X + 70;
        mPath.lineTo(point2X, point1Y);//第二点的坐标
        float point3X = point2X;
        float point3Y = point1Y + 80;
        mPath.lineTo(point3X, point3Y);//第三点的坐标
        canvas.drawPath(mPath, mPaint);
        //画任意多边形：图形封闭
        point1X = point2X + 20;
        mPath.moveTo(point1X, point1Y);//操作的起点位置
        point2X = point1X + 79;
        mPath.lineTo(point2X, point1Y);//第二点的坐标
        point3X = point1X + 30;
        mPath.lineTo(point3X, point3Y);//第三点的坐标
        mPath.close();//设置图形封闭：当绘制的路径大于等于3时，会前后连接成一个图形
        canvas.drawPath(mPath, mPaint);
        *//* 画任意多边形：等边三角形
         * Math.toRadians(60)：角度换算成弧度；
         * Math.sin(Math.toRadians(60));//60度角的正弦值
         * Math.cos(Math.toRadians(60));//60度角的余弦值
         * Math.tan(Math.toRadians(60));//60度角的正切值
         *
         * 直角三角函数中，正弦：角的对边除以斜边；余弦：角的邻边除以斜边；正切：角的对直角边除以邻直角边。
         *//*
        float sin60 = (float) Math.sin(Math.toRadians(60));
        point1X = point2X + 20;
        float borderLen = 150;
        point2X = point1X + borderLen;
        mPath.moveTo(point1X, point1Y);
        mPath.lineTo(point1X + borderLen / 2, point1Y + borderLen * sin60);
        mPath.lineTo(point2X, point1Y);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
        //画任意多边形：正六边形
        borderLen = 200;
        float cos30 = (float) Math.cos(Math.toRadians(30));
        float sin30 = (float) Math.sin(Math.toRadians(30));
        point1X = point2X + 80;
        point2X = point1X + borderLen;
        mPath.moveTo(point1X, point1Y);
        mPath.lineTo(point2X, point1Y);
        point3X = point2X + borderLen * sin30;
        point3Y = point1Y + borderLen * cos30;
        mPath.lineTo(point3X, point3Y);
        float point4Y = point3Y + borderLen * cos30;
        mPath.lineTo(point2X, point4Y);
        mPath.lineTo(point1X, point4Y);
        mPath.lineTo(point1X - borderLen * sin30, point3Y);
        mPath.lineTo(point1X, point1Y);
        mPath.close();
        canvas.drawPath(mPath, mPaint);*/
    }

    /**
     * 获取默认颜色的画笔
     */
    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);//抗(不显示)锯齿，让绘出来的物体更清晰
        //paint.setStyle(Paint.Style.STROKE);//空心，默认是实心。
        paint.setStrokeWidth(10f);
        //paint.setColor(mResources.getColor(R.color.red));//设置画笔的颜色
        return paint;
    }

    /**
     * 获取特定颜色的画笔
     */
    private Paint getPaint(int color) {
        Paint paint = this.getPaint();
        paint.setColor(color);// 设置画笔的颜色
        return paint;
    }

    /**
     * 用于绘制文字的画笔
     *
     * @param textSize 文字的大小
     */
    private Paint getPaint(float textSize) {
        Paint paint = getPaint();
        if (textSize > 0) {
            paint.setTextSize(textSize);//设置文字的大小
        }
        return paint;
    }

    /**
     * 获取具有宽度的画笔
     *
     * @param width 画笔的宽度
     */
    private Paint getStoreWidthPaint(int width) {
        Paint paint = getPaint();
        paint.setStrokeWidth(width);// 设置画笔的宽度
        return paint;
    }

}
