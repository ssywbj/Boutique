package com.example.wbj;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.wbj.view.ChangedScrollView;

public class TitleScrollViewActivity extends AppCompatActivity implements ChangedScrollView.OnScrollListener {
    private static final String TAG = "WBJ";
    private ViewGroup mPrepareLayoutTitle;//固定在顶部的Layout
    private ViewGroup mParentLayoutTitle;
    private ViewGroup mChildLayoutTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title_scroll_view);

        mParentLayoutTitle = findViewById(R.id.parent_layout_title);
        mChildLayoutTitle = findViewById(R.id.child_layout_title);
        mPrepareLayoutTitle = findViewById(R.id.prepare_layout_title);
        ((ChangedScrollView) findViewById(R.id.my_scrollview)).setOnScrollListener(this);//滑动监听
    }

    @Override
    public void onScroll(int scrollY, boolean isToBottom) {
        /*原理：当滚动到头部时，把固定的头部从父布局中移除，添加到最外层布局（和ScrollView平级的Layout）里面；
        当滚动返回时， 又把最外层的头部移除，重新添加到原来的父布局里面*/
        final int height = mParentLayoutTitle.getTop();//头部所在的父布局顶边到其父布局顶部的高度（view自身的顶边到其父布局顶边的距离）
        Log.d(TAG, "scrollY = " + scrollY + ", height = " + height);
        if (scrollY > 0 && scrollY >= height) {//滚动到头部父布局的位置
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

        if (isToBottom) {
            Log.i(TAG, "------scroll to bottom------");
        }
    }


}
