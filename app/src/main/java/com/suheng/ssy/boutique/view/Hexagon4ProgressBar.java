package com.suheng.ssy.boutique.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Hexagon4ProgressBar extends View {
    private static final String TAG = Hexagon4ProgressBar.class.getSimpleName();
    private Path mPath;
    private Paint mPaint;

    public Hexagon4ProgressBar(Context context) {
        super(context);
        this.initView();

    }

    public Hexagon4ProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initView();
    }

    private void initView() {
        mPaint = this.getPaint();
        mPath = new Path();

        this.initPoints();
    }

    private void initPoints() {
        float cos30 = (float) Math.cos(Math.toRadians(30));
        float sin30 = (float) Math.sin(Math.toRadians(30));
        float borderLen = 150f, translate = 0f;
        float xTrans = translate * sin30, yTrans = translate * cos30;
        float xBorder = borderLen * cos30, yBorder = borderLen * sin30;

        PointF point1Start = new PointF();
        point1Start.x = (0f + xTrans);
        point1Start.y = (0f - yTrans);
        PointF point1End = new PointF();
        point1End.x = (xBorder + xTrans);
        point1End.y = (yBorder - yTrans);
        mPath.moveTo(point1Start.x, point1Start.y);
        mPath.lineTo(point1End.x, point1End.y);//第1条线
        Log.d(TAG, "point1Start.x = " + point1Start.x + ", point1Start.y = " + point1Start.y
                + ", point1End.y = " + point1End.x + ", point1End.y = " + point1End.y);

        PointF point2Start = new PointF();
        point2Start.x = (xBorder + translate);
        point2Start.y = yBorder;
        PointF point2End = new PointF();
        point2End.x = point2Start.x;
        point2End.y = (yBorder + borderLen);
        mPath.moveTo(point2Start.x, point2Start.y);
        mPath.lineTo(point2End.x, point2End.y);//第2条线
        Log.d(TAG, "point2Start.x = " + point2Start.x + ", point2Start.y = " + point2Start.y
                + ", point2End.y = " + point2End.x + ", point2End.y = " + point2End.y);

        PointF point3Start = new PointF();
        point3Start.x = point1End.x;
        point3Start.y = (point2End.y + yTrans);
        PointF point3End = new PointF();
        point3End.x = point1Start.x;
        point3End.y = (2 * borderLen + yTrans);
        mPath.moveTo(point3Start.x, point3Start.y);
        mPath.lineTo(point3End.x, point3End.y);//第3条线

        PointF point4Start = new PointF();
        point4Start.x = -point3End.x;
        point4Start.y = point3End.y;
        PointF point4End = new PointF();
        point4End.x = -point3Start.x;
        point4End.y = point3Start.y;
        mPath.moveTo(point4Start.x, point4Start.y);
        mPath.lineTo(point4End.x, point4End.y);//第4条线

        PointF point5Start = new PointF();
        point5Start.x = -point2End.x;
        point5Start.y = point2End.y;
        PointF point5End = new PointF();
        point5End.x = point5Start.x;
        point5End.y = point2Start.y;
        mPath.moveTo(point5Start.x, point5Start.y);
        mPath.lineTo(point5End.x, point5End.y);//第5条线

        PointF point6Start = new PointF();
        point6Start.x = -point1End.x;
        point6Start.y = point1End.y;
        PointF point6End = new PointF();
        point6End.x = -point1Start.x;
        point6End.y = point1Start.y;
        mPath.moveTo(point6Start.x, point6Start.y);
        mPath.lineTo(point6End.x, point6End.y);//第6条线

        Log.d(TAG, "=======================================================================");

        List<float[]> lineList = new ArrayList<>();
        Matrix matrix = new Matrix();
        float[] src = {0f + xTrans, 0f - yTrans};
        float[] dst = new float[2];
        matrix.postTranslate(xBorder + xTrans, yBorder - yTrans);
        matrix.mapPoints(dst, src);
        float[] firstLine = {src[0], src[1], dst[0], dst[1]};
        lineList.add(new float[]{src[0], src[1], dst[0], dst[1]});//1
        Log.d(TAG, "point1Start.x = " + firstLine[0] + ", point1Start.y = " + firstLine[1]
                + ", point1End.y = " + firstLine[2] + ", point1End.y = " + firstLine[3]);

        float[] linePoints = new float[firstLine.length];
        matrix = new Matrix();
        matrix.postRotate(60);
        matrix.mapPoints(linePoints, firstLine);
        lineList.add(new float[]{linePoints[0], linePoints[1], linePoints[2], linePoints[3]});//2
        Log.d(TAG, "point2Start.x = " + linePoints[0] + ", point2Start.y = " + linePoints[1]
                + ", point2End.y = " + linePoints[2] + ", point2End.y = " + linePoints[3]);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(1.0f * getWidth() / 2, 1.0f * getHeight() / 2);
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
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
        return this.getPaint(Color.BLUE);
    }
}
