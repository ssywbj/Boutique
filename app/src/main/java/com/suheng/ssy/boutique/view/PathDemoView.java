package com.suheng.ssy.boutique.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * PathDemo
 * https://www.jianshu.com/p/ac1250bccd3b/
 * https://www.jianshu.com/p/3efa5341abcc
 */
public class PathDemoView extends View {
    private static final String TAG = PathDemoView.class.getSimpleName();

    private Path mPathLen;
    private PathMeasure mPathMeasureLen;

    private Paint mPaint;

    private Path mPath;
    private PathMeasure mPathMeasure;
    private Path mPathDst;//用于存储PathMeasure截取片断

    private float mAnimatorValue;
    private float mCircleLength;

    public PathDemoView(Context context) {
        super(context);
        initView();
    }

    public PathDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mPathLen = new Path();
        mPathMeasureLen = new PathMeasure();

        mPaint = this.getPaint();

        mPath = new Path();
        mPathMeasure = new PathMeasure();
        mPathDst = new Path();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);//属性动画
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {//监听动画过程

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue = (float) animation.getAnimatedValue();
                invalidate();//UI刷新
            }
        });
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPathLen.moveTo(10f, 10f);
        mPathLen.lineTo(100f, 10f);
        mPathLen.lineTo(100f, 100f);
        mPathLen.lineTo(10f, 100f);
        canvas.drawPath(mPathLen, mPaint);
        //forceClosed：测量时是否闭合，只对测量时候有影响，不关乎Path的绘制。
        mPathMeasureLen.setPath(mPathLen, true);
        Log.d(TAG, "path length = " + mPathMeasureLen.getLength() + ", closed = " + mPathMeasureLen.isClosed());
        mPathMeasureLen.setPath(mPathLen, false);
        Log.d(TAG, "path length = " + mPathMeasureLen.getLength() + ", closed = " + mPathMeasureLen.isClosed());

        //为Path添加一个圆形
        float radius = 100f;//半径长度
        float cCenterX = 200f;//圆心x坐标
        float cCenterY = 150f;//圆心y坐标
        mPath.addCircle(cCenterX, cCenterY, radius, Path.Direction.CW);//加入一个半径为100的圆
        canvas.drawPath(mPath, mPaint);//把圆画出来

        //截取Path圆形的一部分
        mPathDst.reset();
        mPathDst.lineTo(0, 0);//避免使用硬件加速产生的bug
        mPathMeasure.setPath(mPath, false);
        float subArc = (float) (Math.PI * 2 * radius / 4);//截取圆的1/4，Math.PI * 2 * radius：圆的周长
        mPathMeasure.getSegment(0, subArc, mPathDst, true);
        mPaint.setColor(Color.BLUE);
        canvas.drawPath(mPathDst, mPaint);

        //截取Path圆形的一部分
        mPathDst.reset();
        mPathDst.lineTo(0, 0);//避免使用硬件加速产生的bug
        mPathDst.lineTo(cCenterX, cCenterY);//dst的起点和圆心连起来
        mPathMeasure.setPath(mPath, false);
        mPathMeasure.getSegment(subArc, subArc * 2, mPathDst, true);
        //mPathMeasure.getSegment(subArc, subArc * 2, mPathDst, false);//截取的片段和dst连接起来
        mPaint.setColor(Color.BLACK);
        canvas.drawPath(mPathDst, mPaint);

        mPathDst.reset();
        mPathDst.lineTo(0, 0);//避免使用硬件加速产生的bug
        mPaint.setColor(Color.GREEN);
        mPath.reset();
        cCenterX = cCenterX + 2 * radius + 20;//圆心x坐标
        mPath.addCircle(cCenterX, cCenterY, radius, Path.Direction.CW);//加入一个半径为100的圆
        mPathMeasure.setPath(mPath, false);
        mCircleLength = mPathMeasure.getLength();
        //canvas.drawPath(mPath, mPaint);//把圆画出来
        float stop = mCircleLength * mAnimatorValue;
        float start = (float) (stop - ((0.5 - Math.abs(mAnimatorValue - 0.5)) * mCircleLength));
        mPathMeasure.getSegment(start, stop, mPathDst, true);
        canvas.drawPath(mPathDst, mPaint);//绘制截取的片段
    }

    /**
     * 获取特定颜色的画笔
     *
     * @param color The new color (including alpha) to set in the paint.
     * @return Paint
     */
    private Paint getPaint(int color) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);//抗(不显示)锯齿，让绘出来的物体更清晰
        paint.setStyle(Paint.Style.STROKE);//空心，默认实心。
        paint.setStrokeWidth(4f);//画笔宽度
        paint.setColor(color);//画笔颜色
        return paint;
    }

    private Paint getPaint() {
        return this.getPaint(Color.RED);
    }

}
