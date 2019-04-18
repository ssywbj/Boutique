package com.wbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ScrollView;

public class ChangedScrollView extends ScrollView {

    private OnScrollListener mOnScrollListener;

    public ChangedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChangedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * @param x    变化后的X轴位置
     * @param y    变化后的Y轴的位置
     * @param oldX 原先的X轴的位置
     * @param oldY 原先的Y轴的位置
     */
    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        Log.d("WBJ", "scrollY = " + getScrollY() + ", height = " + getHeight()
                + ", computeVerticalScrollRange = " + computeVerticalScrollRange());
        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(y, (getScrollY() + getHeight()) == computeVerticalScrollRange());
        }
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.mOnScrollListener = listener;
    }

    public interface OnScrollListener {
        void onScroll(int scrollY, boolean isToBottom);
    }

}
