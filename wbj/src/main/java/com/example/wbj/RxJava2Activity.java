package com.example.wbj;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.wbj.view.MyScrollView;

public class RxJava2Activity extends AppCompatActivity implements MyScrollView.OnScrollListener {
    private static final String TAG = "WBJ";
    private ViewGroup mPrepareLayoutTitle;//固定在顶部的Layout
    /**
     * 跟随ScrollView的TabViewLayout
     */
    private ViewGroup mParentLayoutTitle;
    /**
     * 要悬浮在顶部的View的子View
     */
    private ViewGroup mChildLayoutTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rxjava2_aty);

        mParentLayoutTitle = findViewById(R.id.parent_layout_title);
        mChildLayoutTitle = findViewById(R.id.child_layout_title);
        mPrepareLayoutTitle = findViewById(R.id.prepare_layout_title);
        ((MyScrollView) findViewById(R.id.my_scrollview)).setOnScrollListener(this);//滑动监听
    }

    @Override
    public void onScroll(int scrollY) {
        final int height = mParentLayoutTitle.getTop();
        Log.d(TAG, "scrollY = " + scrollY + ", height = " + height);
        if (scrollY > 0 && scrollY >= height) {
            if (mChildLayoutTitle.getParent() != mPrepareLayoutTitle) {
                mParentLayoutTitle.removeView(mChildLayoutTitle);
                mPrepareLayoutTitle.addView(mChildLayoutTitle);
            }
        } else {
            if (mChildLayoutTitle.getParent() != mParentLayoutTitle) {
                mPrepareLayoutTitle.removeView(mChildLayoutTitle);
                mParentLayoutTitle.addView(mChildLayoutTitle);
            }
        }
    }

}
