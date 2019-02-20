package com.wbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Scroller;

import java.lang.reflect.Field;

public class WbjViewPager extends ViewPager {
    private boolean mNoScroll;
    private int mDuration = 500;//页面滑动切换时间

    public WbjViewPager(Context context) {
        this(context, null);
    }

    public WbjViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (mNoScroll) {
            return false;
        } else {
            return super.onTouchEvent(motionEvent);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (mNoScroll) {
            return false;
        } else {
            return super.onInterceptTouchEvent(motionEvent);
        }
    }

    public void setNoScroll(boolean noScroll) {
        mNoScroll = noScroll;
    }

    public void setDuration(int duration) {
        if (duration <= 100) {
            return;
        }
        mDuration = duration;

        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            field.set(this, new SpeedScroller(getContext()));//设置新加速器
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SpeedScroller extends Scroller {

        public SpeedScroller(Context context) {
            super(context);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }

}
