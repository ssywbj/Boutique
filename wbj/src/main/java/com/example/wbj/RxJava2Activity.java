package com.example.wbj;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.wbj.view.ChangedScrollView;

public class RxJava2Activity extends AppCompatActivity implements ChangedScrollView.OnScrollListener {
    private static final String TAG = "WBJ";
    private ViewGroup mPrepareLayoutTitle;//固定在顶部的Layout
    private ViewGroup mParentLayoutTitle;
    private ViewGroup mChildLayoutTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rxjava2_aty);

        mParentLayoutTitle = findViewById(R.id.parent_layout_title);
        mChildLayoutTitle = findViewById(R.id.child_layout_title);
        mPrepareLayoutTitle = findViewById(R.id.prepare_layout_title);
        ((ChangedScrollView) findViewById(R.id.my_scrollview)).setOnScrollListener(this);//滑动监听
    }

    @Override
    public void onScroll(int scrollY, boolean isToBottom) {
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

        if (isToBottom) {
            Log.i(TAG, "------scroll to bottom------");
        }
    }


}
