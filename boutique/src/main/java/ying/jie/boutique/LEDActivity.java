package ying.jie.boutique;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ying.jie.util.CountDownTime;
import ying.jie.view.LedTextView;

/**
 * LED字体显示效果
 * Created by Weibj on 2016/1/22.
 */
public class LEDActivity extends BasicActivity {
    private CountDownTime mDownTime, mDownTime2;
    private LedTextView mTextTime;
    private TextView mTextTime2, mTextStatus, mTextReset;
    /**
     * 是否已经开始，默认为还没有开始
     */
    private boolean mIsStarted;
    /**
     * 是否处于暂停状态，默认为不处于
     */
    private boolean mIsPaused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setActivityTitle(getIntent().getStringExtra(App.INT_ACT_TITLE));
    }

    /**
     * 返回Activity的布局文件资源
     */
    @Override
    public int getLayoutId() {
        return R.layout.led_aty;
    }

    /**
     * 为了编码的规范，此方法用于初始化变量
     */
    @Override
    public void initData() {
        mDownTime = new CountDownTime(100, true);
        mDownTime.setCountTimeListener(new CountDownTime.CountTimeListener() {
            @Override
            public void endOfTime() {
                mTextTime.setText(mDownTime.getTimeString());//第一种获取时间的方式
            }

            @Override
            public void getTime(String hour, String minute, String second) {
                mTextTime.setText(hour + ":" + minute + ":" + second);//第二种获取时间的方式
            }
        });
        mTextTime = (LedTextView) findViewById(R.id.text_count_down);
        mTextTime.setText(mDownTime.getTimeString());

        mDownTime2 = new CountDownTime(20, false);
        mDownTime2.setCountTimeListener(new CountDownTime.CountTimeListener() {
            @Override
            public void endOfTime() {
                LEDActivity.this.recountTime();
            }

            @Override
            public void getTime(String hour, String minute, String second) {
                mTextTime2.setText(hour + ":" + minute + ":" + second);
            }
        });
        mTextTime2 = (TextView) findViewById(R.id.text_count_down2);
        final Typeface typeface = Typeface.createFromAsset(getAssets(), "digital-7 (mono).ttf");
        mTextTime2.setTypeface(typeface);// 设置字体类型
        mTextTime2.setText(mDownTime2.getTimeString());

        mTextReset = (TextView) findViewById(R.id.text_reset);
        mTextReset.setOnClickListener(this);
        mTextStatus = (TextView) findViewById(R.id.text_status);
        mTextStatus.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.text_reset) {
            this.recountTime();
        } else if (i == R.id.text_status) {
            if (!mIsStarted) {
                mDownTime2.startCountTime();
                mTextStatus.setText(R.string.pause);
                mIsStarted = true;

                mTextReset.setEnabled(true);
            } else {
                if (!mIsPaused) {
                    mDownTime2.pauseCountTime();
                    mTextStatus.setText(R.string.resume);
                } else {
                    mDownTime2.resumeCountTime();
                    mTextStatus.setText(R.string.pause);
                }
                mIsPaused = !mIsPaused;
            }
        }
    }


    private void recountTime() {
        mTextTime2.setText(mDownTime2.setNewSeconds(100));

        if (mIsPaused) {
            mTextStatus.setText(R.string.pause);
            mIsPaused = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDownTime.stopCountTime();
        mDownTime2.stopCountTime();
    }

}
