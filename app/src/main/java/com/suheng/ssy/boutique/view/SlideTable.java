package com.suheng.ssy.boutique.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SlideTable extends View {
    private Paint mTextPaint;      //绘制文字的画笔
    private Paint mTextSelPaint;
    private Paint mLinePaint;      //绘制基准线的画笔
    private Paint mCirclePaint;    //绘制基准线上灰色圆圈的画笔
    private Paint mCircleSelPaint; //绘制被选中位置的蓝色圆圈的画笔

    int mColorTextDef = Color.GRAY;
    int mColorSelected = Color.BLUE;
    int mColorDef = Color.argb(255, 234, 234, 234);   //#EAEAEA
    int mTextSize = 20;

    int mLineHight = 5;
    int mCircleHight = 20;
    int mCircleSelStroke = 10;
    int mMarginTop = 50;//文字与基准线的距离

    List<IDataBean> textList = new ArrayList<IDataBean>();
    private ArrayList<Rect> mBounds;
    private int selectIndex = -1;//当前选中的index
    private int circleCount;
    private int dividWidth;//每段线的长度
    private int defaultHeight;

    public SlideTable(Context context) {
        this(context, null);
    }

    public SlideTable(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideTable(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        measureText();
        measureHeight();
    }

    private void initPaint() {
        mLinePaint = new Paint();
        mCirclePaint = new Paint();
        mTextPaint = new Paint();
        mCircleSelPaint = new Paint();
        mTextSelPaint = new Paint();

        mLinePaint.setColor(mColorDef);
        mLinePaint.setStyle(Paint.Style.FILL);//设置填充
        mLinePaint.setStrokeWidth(mLineHight);//笔宽像素
        mLinePaint.setAntiAlias(true);//锯齿不显示

        mCirclePaint.setColor(mColorDef);
        mCirclePaint.setStyle(Paint.Style.FILL);//设置填充
        mCirclePaint.setStrokeWidth(1);//笔宽像素
        mCirclePaint.setAntiAlias(true);//锯齿不显示

        mCircleSelPaint.setColor(mColorSelected);
        mCircleSelPaint.setStyle(Paint.Style.STROKE);    //空心圆圈
        mCircleSelPaint.setStrokeWidth(mCircleSelStroke);
        mCircleSelPaint.setAntiAlias(true);

        mTextPaint.setTextSize(mTextSize);//文本 画笔
        mTextPaint.setColor(mColorTextDef);
        mLinePaint.setAntiAlias(true);

        mTextSelPaint.setTextSize(mTextSize);//选中后的文本画笔
        mTextSelPaint.setColor(mColorSelected);
        mTextSelPaint.setAntiAlias(true);
    }

    /**
     * 测量文字的长宽
     */
    private void measureText() {//
        mBounds = new ArrayList<>();
        for (IDataBean name : textList) {
            Rect mBound = new Rect();
            mTextPaint.getTextBounds(name.getTextName(), 0, name.getTextName().length(), mBound);
            mBounds.add(mBound);
        }
    }

    private void measureHeight() {//测量view的高度
        if (mBounds != null && mBounds.size() != 0)
            defaultHeight = mCircleHight + mMarginTop + mCircleSelStroke + mBounds.get(0).height() / 2;
        else
            defaultHeight = mCircleHight + mMarginTop + mCircleSelStroke;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {//宽高都设置为wrap_content
            setMeasuredDimension(widthSpecSize, defaultHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {//宽设置为wrap_content
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {//高设置为wrap_content
            setMeasuredDimension(widthSpecSize, defaultHeight);
        } else {//宽高都设置为match_parenth或具体的dp值
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (textList == null || textList.size() == 0) return;
        canvas.drawLine(mCircleHight / 2, mCircleHight / 2 + mCircleSelStroke, getWidth() - mCircleHight / 2, mCircleHight / 2 + mCircleSelStroke, mLinePaint);
        circleCount = textList.size();//画灰色园的个数
        dividWidth = (getWidth() - mCircleHight) / (circleCount - 1);//每个园相隔的距离
        for (int i = 0; i <= circleCount; i++) {
            if (i == selectIndex) {
                canvas.drawCircle(mCircleHight / 2 + i * dividWidth, mCircleHight / 2 + mCircleSelStroke, mCircleHight / 2, mCircleSelPaint);
            } else {
                canvas.drawCircle(mCircleHight / 2 + i * dividWidth, mCircleHight / 2 + mCircleSelStroke, mCircleHight / 2, mCirclePaint);
            }
        }

        for (int i = 0; i < textList.size(); i++) {
            int currentTextWidth = mBounds.get(i).width();
            if (i == 0) {
                if (i == selectIndex) {
                    canvas.drawText(textList.get(i).getTextName(), 0, mCircleHight + mMarginTop + mCircleSelStroke + mBounds.get(i).height() / 2, mTextSelPaint);
                } else {
                    canvas.drawText(textList.get(i).getTextName(), 0, mCircleHight + mMarginTop + mCircleSelStroke + mBounds.get(i).height() / 2, mTextPaint);
                }
            } else if (i == textList.size() - 1) {
                if (i == selectIndex) {
                    canvas.drawText(textList.get(i).getTextName(), getWidth() - currentTextWidth, mCircleHight + mMarginTop + mCircleSelStroke + mBounds.get(i).height() / 2, mTextSelPaint);
                } else {
                    canvas.drawText(textList.get(i).getTextName(), getWidth() - currentTextWidth, mCircleHight + mMarginTop + mCircleSelStroke + mBounds.get(i).height() / 2, mTextPaint);
                }
            } else {
                if (i == selectIndex) {
                    canvas.drawText(textList.get(i).getTextName(), mCircleHight / 2 + i * dividWidth - currentTextWidth / 2, mCircleHight + mMarginTop + mCircleSelStroke + mBounds.get(i).height() / 2, mTextSelPaint);
                } else {
                    canvas.drawText(textList.get(i).getTextName(), mCircleHight / 2 + i * dividWidth - currentTextWidth / 2, mCircleHight + mMarginTop + mCircleSelStroke + mBounds.get(i).height() / 2, mTextPaint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX;
        float eventY;
        int i = event.getAction();
        if (i == MotionEvent.ACTION_DOWN) {
            Log.e("onTouchEvent", "ACTION_DOWN");
        } else if (i == MotionEvent.ACTION_MOVE) {
            Log.e("onTouchEvent", "ACTION_MOVE");
        } else if (i == MotionEvent.ACTION_UP) {
            Log.e("onTouchEvent", "ACTION_UP");
            eventX = event.getX();
            eventY = event.getY();
            float select = eventX / dividWidth; //计算选中的index
            float xs = select - (int) (select);
            selectIndex = (int) select + (xs > 0.5 ? 1 : 0);
        }
        invalidate();
        return true;
    }

    public void setData(List<IDataBean> t) {
        textList = t;
        measureText();
        measureHeight();
        invalidate();
    }

}
