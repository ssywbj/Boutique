package com.suheng.upload;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 圆角ImageView
 */
public class RoundImageView extends AppCompatImageView {

    public RoundImageView(Context context) {
        super(context);
        this.setBackground();
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setBackground();
    }

    /**
     * 必须要设置背景，免得不会执行draw(Canvas)方法
     */
    private void setBackground() {
        setBackgroundResource(R.color.vertical_progress_bar);
    }

    @Override
    public void draw(Canvas canvas) {
        int radiusLen = getResources().getDimensionPixelOffset(R.dimen.vertical_progress_bar_radius);
        Path path = new Path();
        RectF rectF = new RectF(0, 0, getWidth(), getHeight());
        path.addRoundRect(rectF, radiusLen, radiusLen, Path.Direction.CCW);
        canvas.clipPath(path);
        super.draw(canvas);
    }

}
