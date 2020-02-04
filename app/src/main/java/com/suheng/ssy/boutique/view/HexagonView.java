package com.suheng.ssy.boutique.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.suheng.ssy.boutique.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 正六边形
 */
public class HexagonView extends View {
    private final float mCos30 = (float) Math.cos(Math.toRadians(30));
    private final float mSin30 = (float) Math.sin(Math.toRadians(30));
    private Path mPathHexagon;
    private Paint mPaintHexagon;
    /***圆角半径*/
    private float mRadius = 10;
    /***边长*/
    private float mBorderLen;
    /***边宽*/
    private float mBorderWidth = 20;
    private Path mPathFillColor;
    private boolean mIsFillColor;

    public HexagonView(Context context) {
        super(context);
        this.initPaint();
    }

    public HexagonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init(attrs);
    }

    public HexagonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HexagonView);
        mRadius = typedArray.getDimension(R.styleable.HexagonView_radius, mRadius);
        mBorderWidth = typedArray.getDimension(R.styleable.HexagonView_borderWidth, mBorderWidth);
        mIsFillColor = typedArray.getBoolean(R.styleable.HexagonView_isFillColor, mIsFillColor);
        typedArray.recycle();

        this.initPaint();

        setRotation(-120);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.calcBorderLen();
        this.initPath();
        this.initPoint();
    }

    /**
     * 计算六边形边长
     */
    private void calcBorderLen() {
        float viewWidth = getWidth() - getPaddingLeft() - getPaddingRight();//绘制区域宽度
        float viewHeight = getHeight() - getPaddingTop() - getPaddingBottom();//绘制区域高度
        //减去线条宽度使六边形完全在view内部绘制
        viewWidth = viewWidth - mBorderWidth;
        viewHeight = (viewHeight - mBorderWidth / mCos30);

        if ((viewWidth / viewHeight) > mCos30) {//垂直方向
            mBorderLen = viewHeight / 2;
        } else {
            mBorderLen = viewWidth / 2 / mCos30;
        }
    }

    private void initPaint() {
        mPaintHexagon = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintHexagon.setStrokeWidth(mBorderWidth);
        mPaintHexagon.setStyle(Paint.Style.STROKE);
        //8555D3、6568D6、13C6F9、00ABE6
        mPaintHexagon.setShader(new SweepGradient(0, 0, new int[]{Color.parseColor("#8555D3"),
                Color.parseColor("#6568D6"), Color.parseColor("#13C6F9"),
                Color.parseColor("#00ABE6"), Color.parseColor("#8555D3")}, null));
    }

    private void initPath() {
        mPathHexagon = new Path();

        if (mIsFillColor) {
            mPathFillColor = new Path();
        }
    }

    private void initPoint() {
        if (mPathHexagon == null) {
            mPathHexagon = new Path();
        } else {
            mPathHexagon.reset();
        }

        final float tan30 = (float) Math.tan(Math.toRadians(30));
        final float translate = mBorderLen - 2 * mRadius * tan30;//位移长度
        final float xTrans = mCos30 * translate;//X轴位移距离
        final float yTrans = mSin30 * translate;//Y轴位移距离

        RectF[] arcRect = new RectF[6];//用于保存圆弧外切矩形坐标

        final float top = -(mBorderLen - (mRadius / mCos30 - mRadius));
        RectF firstRect = new RectF(-mRadius, top, mRadius, top + 2 * mRadius);
        arcRect[0] = firstRect;//第一个外切圆矩形坐标

        RectF rectF = new RectF();
        Matrix matrix = new Matrix();
        matrix.postTranslate(xTrans, yTrans);
        matrix.mapRect(rectF, firstRect);
        arcRect[1] = rectF;

        rectF = new RectF();
        matrix.postTranslate(0, translate);
        matrix.mapRect(rectF, firstRect);
        arcRect[2] = rectF;

        rectF = new RectF();
        matrix.postTranslate(-xTrans, yTrans);
        matrix.mapRect(rectF, firstRect);
        arcRect[3] = rectF;

        rectF = new RectF();
        matrix.postTranslate(-xTrans, -yTrans);
        matrix.mapRect(rectF, firstRect);
        arcRect[4] = rectF;

        rectF = new RectF();
        matrix.postTranslate(0, -translate);
        matrix.mapRect(rectF, firstRect);
        arcRect[5] = rectF;

        List<float[]> lineList = new ArrayList<>();//用于保存线段坐标

        float[] src = {mRadius * mSin30, -(mBorderLen - (mRadius / mCos30 - mRadius * mCos30))};//line1起点坐标
        float[] dst = new float[2];//line1终点坐标
        matrix = new Matrix();
        matrix.postTranslate(xTrans, yTrans);
        matrix.mapPoints(dst, src);
        float[] firstLine = {src[0], src[1], dst[0], dst[1]};//line1坐标
        lineList.add(firstLine);//添加第一条线段

        matrix.reset();
        for (int i = 0; i < 5; i++) {
            dst = new float[4];//用于保存线段坐标：起点及终点的X、Y轴坐标
            matrix.postRotate(60);
            matrix.mapPoints(dst, firstLine);
            lineList.add(dst);
        }

        mPathHexagon.addArc(arcRect[0], -90, 30);//第1条弧的右半部分
        mPathHexagon.moveTo(lineList.get(0)[0], lineList.get(0)[1]);//line1
        mPathHexagon.lineTo(lineList.get(0)[2], lineList.get(0)[3]);
        mPathHexagon.addArc(arcRect[1], -60, 60);//第2条弧
        mPathHexagon.moveTo(lineList.get(1)[0], lineList.get(1)[1]);//line2
        mPathHexagon.lineTo(lineList.get(1)[2], lineList.get(1)[3]);
        mPathHexagon.addArc(arcRect[2], 0, 60);//第3条弧
        mPathHexagon.moveTo(lineList.get(2)[0], lineList.get(2)[1]);//line3
        mPathHexagon.lineTo(lineList.get(2)[2], lineList.get(2)[3]);
        mPathHexagon.addArc(arcRect[3], 60, 60);//第4条弧
        mPathHexagon.moveTo(lineList.get(3)[0], lineList.get(3)[1]);//line4
        mPathHexagon.lineTo(lineList.get(3)[2], lineList.get(3)[3]);
        mPathHexagon.addArc(arcRect[4], 120, 60);//第5条弧
        mPathHexagon.moveTo(lineList.get(4)[0], lineList.get(4)[1]);//line5
        mPathHexagon.lineTo(lineList.get(4)[2], lineList.get(4)[3]);
        mPathHexagon.addArc(arcRect[5], 180, 60);//第6条弧
        mPathHexagon.moveTo(lineList.get(5)[0], lineList.get(5)[1]);//line6
        mPathHexagon.lineTo(lineList.get(5)[2], lineList.get(5)[3]);
        mPathHexagon.addArc(arcRect[0], -120, 30);//第1条弧的左半部分

        if (mIsFillColor) {
            if (mPathFillColor == null) {
                mPathFillColor = new Path();
            } else {
                mPathFillColor.reset();
            }
            /*
             单独画一个正边形用于填充颜色，因为mPathHexagon不断地moveTo、lineTo，会把圆弧六边形分割成一段
             一段的,当想用颜色填充圆弧六边形时只会把一段段线条填充起来，而不是填充整个图形区域，所以单独绘制了
             一个正边形用于填充线段外的区域。
             */
            this.initPoint(mPathFillColor, top);
        }
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
        if (mIsFillColor) {
            mPaintHexagon.setStyle(Paint.Style.FILL);
            canvas.drawPath(mPathFillColor, mPaintHexagon);
        }
        mPaintHexagon.setStyle(Paint.Style.STROKE);
        canvas.drawPath(mPathHexagon, mPaintHexagon);//绘制六边形
        canvas.restore();
    }
}
