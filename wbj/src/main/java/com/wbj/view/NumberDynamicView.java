package com.wbj.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.wbj.R;

import java.lang.ref.WeakReference;

public class NumberDynamicView extends FrameLayout {
    private static final String TAG = NumberDynamicView.class.getSimpleName();
    private static final int MSG_REFRESH_INIT = 11;
    private static final int MSG_REFRESH_FINAL = 12;

    //#######################################################################
    private static final int TIME_STAY_SHOW = 300;//开始或结束时的停留时长
    /*
     开始显示时停留的时间比结束时显示的时间长，是考虑刚开始显示时，界面从不可见变成可见，如果让数字
     可见后马上切换到下一个数字，会让视觉感到很匆忙，有闪一下的感觉，所以有必要稍作停留，让视觉感觉
     到平稳舒适后，再切到下一个数字。
     */
    //#######################################################################
    private WeakHandler mHandler = new WeakHandler(this);
    private int mCurrentNumber, mNewNumber;
    private TextView mTextOne, mTextTwo;

    public NumberDynamicView(Context context) {
        super(context);
        this.initView();
    }

    public NumberDynamicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView();
    }

    private void initView() {
        setMinimumWidth(70);
        View.inflate(getContext(), R.layout.number_dynamic_view, this);
        mTextOne = (TextView) findViewById(R.id.text_current);
        mTextTwo = (TextView) findViewById(R.id.text_prepare);
    }


    private void resetView() {
        setVisibility(GONE);

        mTextOne.setText("");
        mTextTwo.setText("");
    }

    public void setNewNumber(int newNumber) {
        if (mCurrentNumber == newNumber) {//新来的值和当前的一样时，不需要更换
            return;
        }
        mNewNumber = newNumber;
        Log.d(TAG, "setNewText: mNewNumber = " + mNewNumber);

        if (mHandler.hasMessages(MSG_REFRESH_FINAL)) {//结束消息标示存在列表中，说明上次来的值是0而且目前还没有发送消息过去调用resetData()方法
            mHandler.removeMessages(MSG_REFRESH_FINAL);
            this.resetView();//移除结束消息后，立即调用修改消息中本要调用的方法修改对应的值
            return;
        }

        String textOne = mTextOne.getText().toString().trim();
        if (textOne.equals("")) {//如果当前显示的数字为空，表示视图不是可见状态，初次展示或被重新设为为空
            //##############################################################
            mTextOne.setText((mCurrentNumber = 0) + "");
            setVisibility(VISIBLE);
            mHandler.sendEmptyMessageDelayed(MSG_REFRESH_INIT, TIME_STAY_SHOW);
            //开始显示时，显示0并且设置视图由不可见状态变成可见状态，然后让视觉短暂停留后再显示新来的值
            //##############################################################
            return;
        }

        mTextOne.setText(mCurrentNumber + "");
        mTextTwo.setText(mNewNumber + "");
        if (mCurrentNumber < mNewNumber) {//示例：9<10，10下移显示9
            mTextOne.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_top_out));
            mTextTwo.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_bottom_in));
        } else {//示例：11>10，10上移显示11
            mTextOne.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_bottom_out));
            mTextTwo.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_top_in));
        }
        mCurrentNumber = mNewNumber;
        if (mNewNumber == 0) {//如果最后一个显示的是0，则切换数字并短暂停留后发出结束消息以隐藏视图
            mHandler.sendEmptyMessageDelayed(MSG_REFRESH_FINAL, TIME_STAY_SHOW + 600);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler.hasMessages(MSG_REFRESH_INIT)) {
            mHandler.removeMessages(MSG_REFRESH_INIT);
        }
        if (mHandler.hasMessages(MSG_REFRESH_FINAL)) {
            mHandler.removeMessages(MSG_REFRESH_FINAL);
        }
    }


    private static class WeakHandler extends Handler {
        private WeakReference<NumberDynamicView> mWeakReference;

        private WeakHandler(NumberDynamicView instance) {
            mWeakReference = new WeakReference<>(instance);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            NumberDynamicView instance = mWeakReference.get();
            if (instance == null) {
                return;
            }

            switch (msg.what) {
                case MSG_REFRESH_INIT:
                    instance.setNewNumber(instance.mNewNumber);
                    break;
                case MSG_REFRESH_FINAL:
                    instance.resetView();
                    break;
                default:
                    break;
            }
        }
    }

}
