package com.suheng.ssy.boutique.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * 正六边形
 */
public class HexagonWidget extends View {

    private final float mCos30 = (float) Math.cos(Math.toRadians(30));
    private final float mSin30 = (float) Math.sin(Math.toRadians(30));
    private Path mPathHexagon;
    private Paint mPaintHexagon;
    /***边长*/
    private float mBorderLen;
    private Path mPathSmall;

    public HexagonWidget(Context context) {
        super(context);
        this.initPaint();
    }

    public HexagonWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initPaint();
    }

    public HexagonWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initPaint();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.calcBorderLen();
        this.initPath();
        this.initPoint();
    }

    private void calcBorderLen() {
        float viewWidth = getWidth() - getPaddingLeft() - getPaddingRight();//绘制区域宽度
        float viewHeight = getHeight() - getPaddingTop() - getPaddingBottom();//绘制区域高度
        if ((viewWidth / viewHeight) > mCos30) {//垂直方向
            mBorderLen = viewHeight / 2;
        } else {
            mBorderLen = viewWidth / 2 / mCos30;
        }
    }

    private void initPaint() {
        mPaintHexagon = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintHexagon.setStyle(Paint.Style.FILL);
        mPaintHexagon.setColor(Color.parseColor("#5613C6F9"));
    }

    private void initPath() {
        mPathHexagon = new Path();
        mPathSmall = new Path();
    }

    private void initPoint() {
        if (mPathHexagon == null) {
            mPathHexagon = new Path();
        } else {
            mPathHexagon.reset();
        }
        if (mPathSmall == null) {
            mPathSmall = new Path();
        } else {
            mPathSmall.reset();
        }

        this.initPoint(mPathHexagon, mBorderLen);
        this.initPoint(mPathSmall, mBorderLen * 0.6f);
    }

    private void initPoint(Path path, float borderLen) {
        float[] src = {0, -borderLen};//line1起点坐标
        float[] dst = new float[src.length];//line1终点坐标
        Matrix matrix = new Matrix();
        matrix.postTranslate(mCos30 * borderLen, mSin30 * borderLen);
        matrix.mapPoints(dst, src);
        float[] firstLine = {src[0], src[1], dst[0], dst[1]};//line1坐标
        path.moveTo(firstLine[0], firstLine[1]);
        path.lineTo(firstLine[2], firstLine[3]);//添加第一条线段
        matrix.reset();
        for (int i = 0; i < 5; i++) {
            dst = new float[firstLine.length];//用于保存线段坐标：起点及终点的X、Y轴坐标
            matrix.postRotate(60);
            matrix.mapPoints(dst, firstLine);
            path.lineTo(dst[2], dst[3]);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate((getWidth() * 1.0f - getPaddingLeft() - getPaddingRight() + 1.0f) / 2 + getPaddingLeft(),
                (getHeight() * 1.0f - getPaddingTop() - getPaddingBottom()) / 2 + getPaddingTop());
        canvas.drawPath(mPathHexagon, mPaintHexagon);
        canvas.drawPath(mPathSmall, mPaintHexagon);
        canvas.restore();
    }
}