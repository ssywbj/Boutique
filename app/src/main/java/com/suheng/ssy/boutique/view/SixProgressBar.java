package com.suheng.ssy.boutique.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.suheng.ssy.boutique.R;

import java.util.ArrayList;
import java.util.List;

public class SixProgressBar extends View {

    private float[] pathLength = new float[13];
    /********************六边形进度条*********************/
    private Path mProgressPath;
    private Paint mHexagonPaint;
    /*进度条是否圆角*/
    private boolean isRound = true;
    /*圆角半径*/
    private int radius = 1;
    /*六边形进度颜色*/
    private int mHexagonColor;
    /*六边形线条宽度*/
    private int mHexagonWidth = 20;
    /*六边形进度*/
    private float progress = 35;
    /*六边形总进度*/
    private float max = 100;
    /*动画执行期间进度*/
    private float scaleProgress;
    /*进度渲染器*/
    private Shader shader;
    /*六边形高度*/
    private float sh;
    /*六边形边长*/
    private float sideLength;
    /*是否使用动画*/
    private boolean useAnimation = false;
    /*动画执行时间：毫秒*/
    private int duration = 1200;
    /******************六边形****************/
    private Path mHexagonSecondPath;
    private Paint mHexagonSecondPaint;
    private int mHexagonSecondColor;

    private float cos30 = (float) Math.cos(Math.toRadians(30));
    private float sin30 = (float) Math.sin(Math.toRadians(30));
    private float tan30 = (float) Math.tan(Math.toRadians(30));

    public SixProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SixProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mHexagonColor = Color.parseColor("#20D18C");
        mHexagonSecondColor = Color.parseColor("#DDDDDD");

        if (attrs == null) {
            return;
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SixProgressBar);
        radius = (int) typedArray.getDimension(R.styleable.SixProgressBar_radius, radius);
        mHexagonColor = typedArray.getColor(R.styleable.SixProgressBar_sexangleColor, mHexagonColor);
        mHexagonSecondColor = typedArray.getColor(R.styleable.SixProgressBar_sexangleSecondColor, mHexagonSecondColor);
        mHexagonWidth = (int) typedArray.getDimension(R.styleable.SixProgressBar_sexangleWidth, mHexagonWidth);
        progress = typedArray.getFloat(R.styleable.SixProgressBar_progress, 100);
        max = typedArray.getFloat(R.styleable.SixProgressBar_max, 100);
        isRound = typedArray.getBoolean(R.styleable.SixProgressBar_isRound, true);
        useAnimation = typedArray.getBoolean(R.styleable.SixProgressBar_useAnimation, true);
        duration = typedArray.getInteger(R.styleable.SixProgressBar_duration, duration);
        typedArray.recycle();

        if (progress < 0) {
            progress = 0;
        }
        if (max < 0) {
            max = 100;
        }
        if (progress > max) {
            progress = max;
        }
        this.scaleProgress = progress;

        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        int viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        int width = dip2px(getContext(), 130);
        int height = dip2px(getContext(), 130);
        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(width, height);
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(width, viewWidth);
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(height, viewHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        /*计算六边形所需要的属性值*/
        calculateSize();
        initPath();
        initPoint();
    }

    private void calculateSize() {
        /*绘制六边形区域宽度*/
        float viewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        /*绘制六边形区域高度*/
        float viewHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        /*使六边形完全在view内部绘制，减去线条宽度*/
        viewWidth = viewWidth - mHexagonWidth;
        viewHeight = (float) (viewHeight - mHexagonWidth / Math.cos(Math.toRadians(30)));

        //垂直方向
        if (viewWidth / viewHeight > Math.cos(Math.toRadians(30))) {
            //绘制区域宽度过长
            sideLength = viewHeight / 2;
            sh = 2 * sideLength;
        } else {
            //绘制区域高度过长
            sideLength = (float) (viewWidth / 2 / Math.cos(Math.toRadians(30)));
            sh = 2 * sideLength;
        }
    }

    private void initPaint() {
        mHexagonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHexagonPaint.setStrokeWidth(mHexagonWidth);
        mHexagonPaint.setStrokeCap(isRound ? Paint.Cap.ROUND : Paint.Cap.BUTT);
        mHexagonPaint.setStyle(Paint.Style.STROKE);
        mHexagonPaint.setColor(mHexagonColor);
        shader = new SweepGradient(0, 0, new int[]{Color.parseColor("#34E8A6"),
                Color.parseColor("#06C1AE"), Color.parseColor("#34E8A6")}, null);
        mHexagonPaint.setShader(shader);

        mHexagonSecondPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHexagonSecondPaint.setStrokeWidth(mHexagonWidth);
        mHexagonSecondPaint.setStyle(Paint.Style.STROKE);
        mHexagonSecondPaint.setColor(mHexagonSecondColor);
    }

    private void initPath() {
        mProgressPath = new Path();
        mHexagonSecondPath = new Path();
    }

    private void initPoint() {
        /*记录圆弧外切矩形坐标*/
        List<Float[]> lineList = new ArrayList<>();
        RectF[] arcRect = new RectF[6];

        if (mHexagonSecondPath != null) {
            mHexagonSecondPath.reset();
        } else {
            mHexagonSecondPath = new Path();
        }

        /*第一个外切圆的矩形坐标*/
        RectF firstRect = new RectF(-radius, -1f * (sh / 2f - (radius / cos30 - radius)), radius,
                -1f * (sh / 2f - (radius / cos30 - radius)) + 2f * radius);

        arcRect[0] = firstRect;

        //位移长度
        float xieLength = sideLength - 2 * radius * tan30;
        //x位移距离
        float translateX = cos30 * xieLength;
        //y位移距离
        float translateY = sin30 * xieLength;

        RectF rectF = new RectF();

        //https://www.cnblogs.com/mmy0925/archive/2013/01/22/2871009.html
        //https://blog.csdn.net/coderinchina/article/details/53665632
        Matrix matrix = new Matrix();
        matrix.postTranslate(translateX, translateY);
        matrix.mapRect(rectF, firstRect);
        arcRect[1] = rectF;

        rectF = new RectF();
        matrix.postTranslate(0, xieLength);
        matrix.mapRect(rectF, firstRect);
        arcRect[2] = rectF;

        rectF = new RectF();
        matrix.postTranslate(-translateX, translateY);
        matrix.mapRect(rectF, firstRect);
        arcRect[3] = rectF;

        rectF = new RectF();
        matrix.postTranslate(-translateX, -translateY);
        matrix.mapRect(rectF, firstRect);
        arcRect[4] = rectF;

        rectF = new RectF();
        matrix.postTranslate(0, -xieLength);
        matrix.mapRect(rectF, firstRect);
        arcRect[5] = rectF;

        /*第一条线段 起始坐标,共有6条线段*/
        /*第一条线段start*/
        float[] firstStartPoint = new float[2];
        firstStartPoint[0] = radius * sin30;
        firstStartPoint[1] = -1f * (sh / 2f - (radius / cos30 - radius * cos30));

        /*第一条线段end*/
        float[] firstEndPoint = new float[2];

        matrix = new Matrix();
        matrix.postTranslate(translateX, translateY);
        matrix.mapPoints(firstEndPoint, firstStartPoint);

        float[] firstLine = new float[]{firstStartPoint[0], firstStartPoint[1], firstEndPoint[0], firstEndPoint[1]};
        /*添加第1条线段*/
        lineList.add(new Float[]{firstStartPoint[0], firstStartPoint[1], firstEndPoint[0], firstEndPoint[1]});

        matrix = new Matrix();

        float[] linePoint = new float[4];
        matrix.postRotate(60);
        matrix.mapPoints(linePoint, firstLine);
        /*添加第2条线段*/
        lineList.add(new Float[]{linePoint[0], linePoint[1], linePoint[2], linePoint[3]});


        linePoint = new float[4];
        matrix.postRotate(60);
        matrix.mapPoints(linePoint, firstLine);
        /*添加第3条线段*/
        lineList.add(new Float[]{linePoint[0], linePoint[1], linePoint[2], linePoint[3]});


        linePoint = new float[4];
        matrix.postRotate(60);
        matrix.mapPoints(linePoint, firstLine);
        /*添加第4条线段*/
        lineList.add(new Float[]{linePoint[0], linePoint[1], linePoint[2], linePoint[3]});


        linePoint = new float[4];
        matrix.postRotate(60);
        matrix.mapPoints(linePoint, firstLine);
        /*添加第5条线段*/
        lineList.add(new Float[]{linePoint[0], linePoint[1], linePoint[2], linePoint[3]});

        linePoint = new float[4];
        matrix.postRotate(60);
        matrix.mapPoints(linePoint, firstLine);
        /*添加第6条线段*/
        lineList.add(new Float[]{linePoint[0], linePoint[1], linePoint[2], linePoint[3]});

        /*添加第1条弧的右半部分*/
        mHexagonSecondPath.addArc(arcRect[0], -90, 30);
        /*添加第1条线段*/
        mHexagonSecondPath.moveTo(lineList.get(0)[0], lineList.get(0)[1]);
        mHexagonSecondPath.lineTo(lineList.get(0)[2], lineList.get(0)[3]);

        /*添加第2条弧*/
        mHexagonSecondPath.addArc(arcRect[1], -60, 60);

        /*添加第2条线段*/
        mHexagonSecondPath.moveTo(lineList.get(1)[0], lineList.get(1)[1]);
        mHexagonSecondPath.lineTo(lineList.get(1)[2], lineList.get(1)[3]);

        /*添加第3条弧*/
        mHexagonSecondPath.addArc(arcRect[2], 0, 60);

        /*添加第3条线段*/
        mHexagonSecondPath.moveTo(lineList.get(2)[0], lineList.get(2)[1]);
        mHexagonSecondPath.lineTo(lineList.get(2)[2], lineList.get(2)[3]);

        /*添加第4条弧*/
        mHexagonSecondPath.addArc(arcRect[3], 60, 60);

        /*添加第4条线段*/
        mHexagonSecondPath.moveTo(lineList.get(3)[0], lineList.get(3)[1]);
        mHexagonSecondPath.lineTo(lineList.get(3)[2], lineList.get(3)[3]);

        /*添加第5条弧*/
        mHexagonSecondPath.addArc(arcRect[4], 120, 60);

        /*添加第5条线段*/
        mHexagonSecondPath.moveTo(lineList.get(4)[0], lineList.get(4)[1]);
        mHexagonSecondPath.lineTo(lineList.get(4)[2], lineList.get(4)[3]);

        /*添加第6条弧*/
        mHexagonSecondPath.addArc(arcRect[5], 180, 60);

        /*添加第6条线段*/
        mHexagonSecondPath.moveTo(lineList.get(5)[0], lineList.get(5)[1]);
        mHexagonSecondPath.lineTo(lineList.get(5)[2], lineList.get(5)[3]);

        /*添加第1条弧的左半部分*/
        mHexagonSecondPath.addArc(arcRect[0], -120, 30);

        recalculateViewProgress();
    }

    private void recalculateViewProgress() {
        if (mProgressPath != null) {
            mProgressPath.reset();
        } else {
            mProgressPath = new Path();
        }
        PathMeasure pathMeasure = new PathMeasure(mHexagonSecondPath, false);
        for (int i = 0; i < 13; i++) {
            float pathMeasureLength = pathMeasure.getLength();
            if (i == 0) {
                pathLength[i] = pathMeasureLength;
            } else {
                pathLength[i] = pathLength[i - 1] + pathMeasureLength;
            }
            pathMeasure.nextContour();
        }

        pathMeasure = new PathMeasure(mHexagonSecondPath, false);
        for (int i = 0; i < 13; i++) {
            if (scaleProgress == 0) {
                break;
            } else if (scaleProgress >= max) {
                pathMeasure.getSegment(0, pathMeasure.getLength(), mProgressPath, true);
            } else {
                if (scaleProgress / max >= pathLength[i] / pathLength[12]) {
                    pathMeasure.getSegment(0, pathMeasure.getLength(), mProgressPath, true);
                } else {
                    if (i == 0) {
                        pathMeasure.getSegment(0, scaleProgress / max * pathLength[12], mProgressPath, true);
                    } else {
                        pathMeasure.getSegment(0, (scaleProgress / max - pathLength[i - 1] / pathLength[12]) * pathLength[12], mProgressPath, true);
                    }
                    break;
                }
            }
            pathMeasure.nextContour();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate((getWidth() - getPaddingLeft() - getPaddingRight() + 1.0f) / 2 + getPaddingLeft(),
                (getHeight() - getPaddingTop() - getPaddingBottom() + 1.0f) / 2 + getPaddingTop());
        /*六边形*/
        canvas.drawPath(mHexagonSecondPath, mHexagonSecondPaint);
        /*六边形进度*/
        canvas.drawPath(mProgressPath, mHexagonPaint);
        canvas.restore();
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        setProgress(progress, useAnimation);
    }

    public void setProgress(float progress, boolean useAnimation) {
        float beforeProgress = SixProgressBar.this.scaleProgress;
        if (progress < 0) {
            progress = 0;
        }
        this.progress = progress;


        if (useAnimation) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(beforeProgress, progress);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    SixProgressBar.this.scaleProgress = (float) animation.getAnimatedValue();
                    recalculateViewProgress();
                    invalidate();
                    if (onProgressChangeInter != null) {
                        onProgressChangeInter.progress(scaleProgress, SixProgressBar.this.progress, max);
                    }
                }
            });
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.setDuration(duration);
            valueAnimator.start();
        } else {
            SixProgressBar.this.scaleProgress = this.progress;
            recalculateViewProgress();
            invalidate();
            if (onProgressChangeInter != null) {
                onProgressChangeInter.progress(scaleProgress, SixProgressBar.this.progress, max);
            }
        }
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        if (max < 0) {
            max = 100;
        }
        if (progress > max) {
            progress = max;
        }
        this.max = max;
        recalculateViewProgress();
        invalidate();
    }

    public void setShader(Shader shader) {
        this.shader = shader;
        mHexagonPaint.setShader(shader);
        invalidate();
    }

    private int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5F);
    }

    public void setOnProgressChangeInter(OnProgressChangeInter onProgressChangeInter) {
        this.onProgressChangeInter = onProgressChangeInter;
    }

    private OnProgressChangeInter onProgressChangeInter;

    public interface OnProgressChangeInter {
        void progress(float scaleProgress, float progress, float max);
    }
}
