package com.suheng.ssy.boutique.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.suheng.ssy.boutique.R;

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
        mPaint = getPaint(38f);
        mPath = new Path();
        mShader = new LinearGradient(0, 0, 100, 100,
                new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
                        Color.LTGRAY}, null, Shader.TileMode.REPEAT);

        setMinimumHeight(mMetrics.heightPixels);
        setMinimumWidth(mMetrics.widthPixels);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String paintText = "我是画上去的";
        float paintTextWidth = mPaint.measureText(paintText);//根据画笔，测出文字的宽度
        Log.i(TAG, "paint text width = " + paintTextWidth);
        //画字 float x:X轴坐标；float y:Y轴坐标
        canvas.drawText(paintText, (mMetrics.widthPixels - paintTextWidth) / 2,
                Y_BASIC_COORDINATE, mPaint);//(mMetrics.widthPixels - paintTextWidth) / 2：水平居中

        mPaint = getPaint();
        mPaint.setStyle(Paint.Style.STROKE);//空心，默认是实心。
        //画圆 float cx:圆心的X轴坐标；float cy:圆心的Y轴坐标；float radius:半径
        canvas.drawCircle(mMetrics.widthPixels / 2, Y_BASIC_COORDINATE * 2, 36, mPaint);

        /*
         * 画线-->float startX:线条起点的X轴坐标；float startY:线条起点的Y轴坐标；float
         * stopX:线条终点的X轴坐标；float stopY:线条终点的Y轴坐标
         */
        //横线-->Y轴坐标不变
        float startY = Y_BASIC_COORDINATE * 1.0f * 3;
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

        /* 画矩形
         * float left:矩形左边所在的X坐标；float top:矩形顶边所在的Y坐标；
         * float right:矩形右边所在的X坐标；float bottom:矩形底边所在的Y坐标。
         */
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

        /* 画圆弧
         * RectF oval：确定圆弧形状和位置的矩形：
         * 1.圆弧的中心点为矩形的中心点；
         * 2.如果(right-left)=(bottom-top)，那么是圆形形状的弧形；反之，是椭圆形状的弧形
         * float startAngle：圆弧的开始角度（时钟3点的方向为0度）
         * float sweepAngle：圆弧的扫过角度（正数为顺时钟方向，负数为逆时钟方向）
         * boolean useCenter：圆弧是否与中心点连接成闭合区域
         */
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
        float point2X = point1X + 79;
        float point2Y = point1Y + 20;
        mPath.lineTo(point2X, point2Y);//第二点的坐标
        float point3X = point1X + 30;
        float point3Y = point2Y + 80;
        mPath.lineTo(point3X, point3Y);//第三点的坐标
        canvas.drawPath(mPath, mPaint);
        //画任意多边形：图形封闭
        point1X = point2X + 20;
        mPath.moveTo(point1X, point1Y);//操作的起点位置
        point2X = point1X + 79;
        mPath.lineTo(point2X, point2Y);//第二点的坐标
        point3X = point1X + 30;
        mPath.lineTo(point3X, point3Y);//第三点的坐标
        mPath.close();//设置图形封闭
        canvas.drawPath(mPath, mPaint);
        /* 画任意多边形：等边三角形
         * Math.toRadians(60)：角度换算成弧度；
         * Math.sin(Math.toRadians(60));//60度角的正弦值
         * 正弦：角的对边除以斜边；余弦：角的邻边除以斜边
         */
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
        borderLen = 100;
        float cos30 = (float) Math.cos(Math.toRadians(30));
        float sin30 = (float) Math.sin(Math.toRadians(30));
        point1X = point2X + 30;
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
        canvas.drawPath(mPath, mPaint);

        /*mPaint.setShader(mShader);
        mPaint.setStrokeWidth(2.0f);*/

        /*//*
         * 画椭圆-->若(right-left)=(bottom-top)，则是圆形，若不等才是椭圆
         *//*
        ovalTrue.set(100, 550, 250, 660);
        canvas.drawOval(ovalTrue, mPaint);
        ovalTrue.set(260, 550, 410, 700);
        canvas.drawOval(ovalTrue, mPaint);

        // 等边三角形
        path = new Path();
        path.moveTo(500, 710);
        path.lineTo(600, 536.8f);
        path.lineTo(700, 710);
        // 未设置封闭//path.close();
        canvas.drawPath(path, mPaint);
        // 折线图
        float[] y = {660, 730, 680, 550, 660};
        path = new Path();
        for (int i = 0; i < 6; i++) {
            if (i == 0) {
                path.moveTo(710, 710);
            } else {
                path.lineTo(740 + 30 * (i - 1), y[i - 1]);
            }
        }
        canvas.drawPath(path, mPaint);

        // 画圆角矩形
        ovalTrue.set(100, 720, 200, 880);
        canvas.drawRoundRect(ovalTrue, 20, 20, mPaint);// 第二个参数是x半径，第三个参数是y半径

        // 画点
        canvas.drawPoint(220, 800, getStoreWidthPaint(10));// 画一个点
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(10);
        canvas.drawPoint(240, 800, mPaint);// 画一个点
        canvas.drawPoints(new float[]{260, 800, 280, 800, 300, 800}, mPaint);// 画多个点

        // 画图片(贴图)
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.province_blue_25);
        canvas.drawBitmap(bitmap, 330, 720, getPaint());

        // 画贝塞尔曲线
        mPaint.setStyle(Paint.Style.STROKE);
        path = new Path();
        path.moveTo(500, 850);// 设置Path的起点
        path.quadTo(500, 600, 880, 800); // 设置贝塞尔曲线的控制点坐标和终点坐标
        canvas.drawPath(path, mPaint);

        // 沿着路径画字
        path = new Path();
        mPaint = getPaint(40.5f);
        path.moveTo(50, 900);
        path.lineTo(50, 1150);
        path.lineTo(50, 900);
        Rect bounds = new Rect();
        String paintTextPath = "沿着路径画字";
        mPaint.getTextBounds(paintTextPath, 0, paintTextPath.length(), bounds);
        int textWidth = bounds.width();
        int textHeiht = bounds.height();
        LogUtil.i(TAG, "textWidth = " + textWidth);
        LogUtil.i(TAG, "textHeiht = " + textHeiht);
        canvas.drawTextOnPath(paintTextPath, path, 0, 0, mPaint);*/
    }

    /**
     * 获取默认颜色的画笔
     */
    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);//抗(不显示)锯齿，让绘出来的物体更清晰
        paint.setColor(mResources.getColor(R.color.red));// 设置画笔的颜色
        return paint;
    }

    /**
     * 获取特定颜色的画笔
     */
    private Paint getPaint(int colorRes) {
        Paint paint = getPaint();
        paint.setColor(colorRes);// 设置画笔的颜色
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
