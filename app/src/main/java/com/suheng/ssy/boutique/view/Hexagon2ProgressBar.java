package com.suheng.ssy.boutique.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Hexagon2ProgressBar extends View {
    public static final String TAG = "WBJ";
    private Paint mPaint;
    private PathMeasure mPathMeasure;
    private Path mPath;
    private ValueAnimator mValueAnimator;
    private float mAnimatorValue;
    private float mCircleLength;
    private float mRadius = 150f;

    private Matrix mMatrix = new Matrix();
    private Bitmap mBitmap;

    public Hexagon2ProgressBar(Context context) {
        super(context);
        this.initView();
    }

    public Hexagon2ProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initView();
    }

    private void initView() {
        mPaint = this.getPaint();

        mPath = new Path();
        mPath.addCircle(0, 0, mRadius, Path.Direction.CW);
        mPathMeasure = new PathMeasure(mPath, false);
        mCircleLength = mPathMeasure.getLength();
        Log.d(TAG, "circle length = " + mCircleLength);

        mBitmap = BitmapFactory.decodeResource(getResources(), android.R.drawable.arrow_down_float);

        mValueAnimator = ValueAnimator.ofFloat(0, 1);//属性动画
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {//监听动画过程
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animation.getAnimatedValue() instanceof Float) {
                    mAnimatorValue = (Float) animation.getAnimatedValue();
                    invalidate();//UI刷新
                }
            }
        });
        mValueAnimator.setDuration(1500);
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);//无限循环
        //mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.start();
    }

    //Math.atan2()方法有何用处，Matrix的用法
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*mPathMeasure.getPosTan(mCircleLength * mAnimatorValue, mPos, mTan);
        Log.d(TAG, "pst(" + mPos[0] + ", " + mPos[1] + ")");
        canvas.save();
        canvas.translate(getWidth() / 2, mRadius + 20);//坐标系平移
        canvas.drawPath(mPath, mPaint);//绘制圆形
        canvas.drawCircle(mPos[0], mPos[1], mPaint.getStrokeWidth(), mPaint);//绘制路径点上的小圆
        //canvas.rotate(-90);//画布旋转，坐标系也跟着旋转；90，顺时针旋转90度；-90，逆时针旋转90度。
        canvas.rotate((float) (Math.atan2(mTan[1], mTan[0]) * 180.0 / Math.PI));//画布旋转，让效果看起来是切线在圆上滑动；角度=弧度*180/π
        canvas.drawLine(0f, -mRadius, 120f, -mRadius, mPaint);//绘制正切线
        canvas.restore();*/

        //得到矩阵
        mPathMeasure.getMatrix(mCircleLength * mAnimatorValue, mMatrix
                , PathMeasure.POSITION_MATRIX_FLAG | PathMeasure.TANGENT_MATRIX_FLAG);
        canvas.translate(getWidth() / 2, mRadius + 20);//将坐标系移动到控件的中心位置
        canvas.drawPath(mPath, mPaint);
        mMatrix.preRotate(270);//调整箭头的朝向为正切线的方向
        mMatrix.preTranslate(-mBitmap.getWidth() / 2, -mBitmap.getHeight() / 2);//箭头的中轴线移动到圆的轨道上
        canvas.drawBitmap(mBitmap, mMatrix, null);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
            mValueAnimator = null;
        }
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
        paint.setStrokeWidth(6f);//画笔宽度
        paint.setColor(color);//画笔颜色
        return paint;
    }

    private Paint getPaint() {
        return this.getPaint(Color.BLUE);
    }
}
