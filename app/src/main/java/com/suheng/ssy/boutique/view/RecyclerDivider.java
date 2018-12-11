package com.suheng.ssy.boutique.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerDivider extends RecyclerView.ItemDecoration {
    private final int[] ATTRS = new int[]{android.R.attr.listDivider};
    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL = LinearLayoutManager.VERTICAL;
    /**
     * 用于绘制间隔样式
     */
    private Drawable mDivider;
    /**
     * 列表的方向：水平/竖直
     */
    private int mOrientation;

    public RecyclerDivider(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS); //获取默认主题的属性
        mDivider = a.getDrawable(0);
        a.recycle();

        mOrientation = (orientation == HORIZONTAL ? HORIZONTAL : VERTICAL);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL) {//绘制间隔
            this.drawVertical(c, parent);
        } else {
            this.drawHorizontal(c, parent);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }

    /**
     * 绘制竖直间隔
     */
    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        View child;
        int top, bottom;
        RecyclerView.LayoutParams params;
        for (int i = 0; i < childCount; i++) {
            child = parent.getChildAt(i);
            params = (RecyclerView.LayoutParams) child.getLayoutParams();
            top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
            bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 绘制水平间隔
     */
    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        View child;
        RecyclerView.LayoutParams params;
        int left, right;
        for (int i = 0; i < childCount; i++) {
            child = parent.getChildAt(i);
            params = (RecyclerView.LayoutParams) child.getLayoutParams();
            left = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));
            right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
