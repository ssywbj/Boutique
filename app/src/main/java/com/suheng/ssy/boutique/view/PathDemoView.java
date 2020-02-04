package com.suheng.ssy.boutique.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

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

    public PathDemoView(Context context) {
        super(context);
        initView();
    }

    public PathDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private RectF mRectF;
    private Matrix mMatrix;

    private void initView() {
        mPathLen = new Path();
        mPathMeasureLen = new PathMeasure();

        mPaint = this.getPaint();

        mPath = new Path();
        mPathMeasure = new PathMeasure();
        mPathDst = new Path();

        //this.matrixMapPoints();
        this.matrixMapPoints2();
        //this.matrixMapPoints3();
        this.matrixMapRadius();
        this.matrixMapRect();
    }

    private void matrixMapPoints() {
        Matrix matrix = new Matrix();
        Log.d(TAG, "mMatrix = " + matrix.toString());
        //初始数据为三个点 (0, 0) (80, 100) (400, 300)
        float[] pts = {0, 0, 80, 100, 400, 300};
        matrix.setScale(0.5f, 1f);//x坐标缩放0.5
        Log.d(TAG, "before scale: " + Arrays.toString(pts));
        matrix.mapPoints(pts);//调用map方法计算
        Log.d(TAG, " after scale: " + Arrays.toString(pts));
    }

    private void matrixMapPoints2() {
        //初始数据为三个点 (0, 0) (80, 100) (400, 300)
        float[] src = {0, 0, 80, 100, 400, 300};
        float[] dst = new float[6];

        Matrix matrix = new Matrix();//构造一个matrix，x坐标缩放0.5
        matrix.setScale(0.5f, 1f);

        Log.i(TAG, "before: src=" + Arrays.toString(src));
        Log.i(TAG, "before: dst=" + Arrays.toString(dst));

        matrix.mapPoints(dst, src);

        Log.i(TAG, "after : src=" + Arrays.toString(src));
        Log.i(TAG, "after : dst=" + Arrays.toString(dst));
    }

    private void matrixMapPoints3() {
        float[] src = new float[]{0, 0, 80, 100, 400, 300};
        float[] dst = new float[6];

        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 1f);

        Log.i(TAG, "before: src=" + Arrays.toString(src));
        Log.i(TAG, "before: dst=" + Arrays.toString(dst));

        //调用map方法计算(最后一个2表示两个点，即四个数值,并非两个数值)
        matrix.mapPoints(dst, 0, src, 2, 2);

        Log.i(TAG, "after : src=" + Arrays.toString(src));
        Log.i(TAG, "after : dst=" + Arrays.toString(dst));
    }

    private void matrixMapRadius() {
        float radius = 100;
        float result;

        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 1f);
        Log.i(TAG, "mapRadius: " + radius);
        result = matrix.mapRadius(radius);
        Log.i(TAG, "mapRadius: " + result);
    }

    private void matrixMapRect() {
        mRectF = new RectF(400, 400, 1000, 800);

        mMatrix = new Matrix();
        mMatrix.setScale(0.5f, 1f);

        Log.i(TAG, "mapRect: " + mRectF.toString());
        boolean result = mMatrix.mapRect(mRectF);
        Log.i(TAG, "mapRect: " + mRectF.toString());
        Log.e(TAG, "isRect: " + result);

        mRectF = new RectF(400, 400, 1000, 800);
        mMatrix.postSkew(1, 0);
        Log.i(TAG, "mapRect: " + mRectF.toString());
        result = mMatrix.mapRect(mRectF);
        Log.i(TAG, "mapRect: " + mRectF.toString());
        Log.e(TAG, "isRect: " + result);

        mRectF = new RectF(400, 400, 1000, 800);
        mMatrix = new Matrix();
        mMatrix.postTranslate(0, 100f);
        Log.i(TAG, "mapRect: " + mRectF.toString());
        result = mMatrix.mapRect(mRectF);
        Log.i(TAG, "mapRect: " + mRectF.toString());
        Log.e(TAG, "isRect: " + result);

        mRectF = new RectF(400, 400, 1000, 800);
        mMatrix = new Matrix();
        mMatrix.setTranslate(0, 100f);
        Log.i(TAG, "mapRect: " + mRectF.toString());
        result = mMatrix.mapRect(mRectF);
        Log.i(TAG, "mapRect: " + mRectF.toString());
        Log.e(TAG, "isRect: " + result);

        mRectF = new RectF(400, 400, 1000, 800);
        mMatrix = new Matrix();
        mMatrix.postRotate(90);
        Log.i(TAG, "mapRect: " + mRectF.toString());
        result = mMatrix.mapRect(mRectF);
        Log.i(TAG, "mapRect: " + mRectF.toString());
        Log.e(TAG, "isRect: " + result);//postRotate

        mRectF = new RectF(100, 300, 200, 450);
        mMatrix = new Matrix();
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
        //Log.d(TAG, "path length = " + mPathMeasureLen.getLength() + ", closed = " + mPathMeasureLen.isClosed());
        mPathMeasureLen.setPath(mPathLen, false);
        //Log.d(TAG, "path length = " + mPathMeasureLen.getLength() + ", closed = " + mPathMeasureLen.isClosed());

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

        canvas.drawRect(mRectF, mPaint);

        mMatrix.postTranslate(0, 200f);
        mMatrix.mapRect(mRectF);
        mPaint.setColor(Color.DKGRAY);
        canvas.drawRect(mRectF, mPaint);

        mMatrix.reset();
        mMatrix.preTranslate(-20f, 20f);
        mMatrix.postScale(0.5f, 1f);
        mMatrix.mapRect(mRectF);
        mPaint.setColor(Color.GRAY);
        canvas.drawRect(mRectF, mPaint);

        /*mMatrix.reset();
        mMatrix.postRotate(90);
        mMatrix.mapRect(mRectF);
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(mRectF, mPaint);*/
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
