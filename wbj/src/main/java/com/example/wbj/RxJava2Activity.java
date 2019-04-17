package com.example.wbj;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.wbj.view.MyScrollView;

public class RxJava2Activity extends AppCompatActivity implements MyScrollView.OnScrollListener {
    public static final String TAG = RxJava2Activity.class.getSimpleName();
    /**
     * 顶部固定的TabViewLayout
     */
    private LinearLayout mTopTabViewLayout;
    /**
     * 跟随ScrollView的TabviewLayout
     */
    private LinearLayout mTabViewLayout;

    /**
     * 要悬浮在顶部的View的子View
     */
    private LinearLayout mTopView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rxjava2_aty);

        MyScrollView mMyScrollView = findViewById(R.id.my_scrollview);
        mTabViewLayout = findViewById(R.id.ll_tabView);
        mTopTabViewLayout = findViewById(R.id.ll_tabTopView);
        mTopView = findViewById(R.id.tv_topView);
        mMyScrollView.setOnScrollListener(this);//滑动监听
    }

    @Override
    public void onScroll(int scrollY) {
        int mHeight = mTabViewLayout.getTop();
        if (scrollY > 0 && scrollY >= mHeight) {
            if (mTopView.getParent() != mTopTabViewLayout) {
                mTabViewLayout.removeView(mTopView);
                mTopTabViewLayout.addView(mTopView);
            }

        } else {
            if (mTopView.getParent() != mTabViewLayout) {
                mTopTabViewLayout.removeView(mTopView);
                mTabViewLayout.addView(mTopView);
            }

        }
    }
}
