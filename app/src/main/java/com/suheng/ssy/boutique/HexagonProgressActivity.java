package com.suheng.ssy.boutique;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.suheng.ssy.boutique.view.HexagonView;

public class HexagonProgressActivity extends LaunchTypeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hexagon_progress);
        this.initView();
    }

    private void initView() {
        final HexagonView outHexagon = findViewById(R.id.outer_hexagon_view);
        final HexagonView stopHexagon = findViewById(R.id.stop_hexagon);
        final HexagonView hexagonView = findViewById(R.id.hexagon_view);
        mOuterAnimator = ValueAnimator.ofFloat(0, 1);
        mOuterAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animation.getAnimatedValue() instanceof Float) {
                    float animatedValue = (Float) animation.getAnimatedValue();
                    outHexagon.setAlpha(0.3f * (1 - animatedValue));
                    outHexagon.setScaleX(1.0f + 0.5f * animatedValue);
                    outHexagon.setScaleY(1.0f + 0.5f * animatedValue);
                }
            }
        });
        mOuterAnimator.setDuration(1200);
        mOuterAnimator.setInterpolator(new LinearInterpolator());
        mOuterAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mOuterAnimator.start();
        hexagonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stopHexagon.getVisibility() != View.VISIBLE) {
                    outHexagon.setVisibility(View.GONE);
                    stopHexagon.setVisibility(View.VISIBLE);
                    mOuterAnimator.cancel();
                } else {
                    outHexagon.setVisibility(View.VISIBLE);
                    stopHexagon.setVisibility(View.GONE);
                    mOuterAnimator.start();
                }

                for (int i = 0; i < 6; i++) {
                    final Message msg = new Message();
                    msg.what = 11;
                    msg.arg1 = (99 - i);
                    //mWeakHandler.sendMessageDelayed(msg, 300 * i);
                    mTextScore.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mTextScore.setText(String.valueOf(msg.arg1));
                        }
                    }, 300 * i);
                }
            }
        });

        final HexagonView middleHexagon = findViewById(R.id.middle_hexagon_view);
        mMiddleAnimator = ValueAnimator.ofFloat(0, 1);
        mMiddleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animation.getAnimatedValue() instanceof Float) {
                    float animatedValue = (Float) animation.getAnimatedValue();
                    middleHexagon.setAlpha(0.2f * (1 - animatedValue));
                    middleHexagon.setScaleX(animatedValue);
                    middleHexagon.setScaleY(animatedValue);
                }
            }
        });
        mMiddleAnimator.setDuration(2500);
        mMiddleAnimator.setInterpolator(new LinearInterpolator());
        mMiddleAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mMiddleAnimator.start();

        final HexagonView middleTurnHexagon = findViewById(R.id.middle_turn_hexagon);
        middleHexagon.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMiddleTurnAnimator = ValueAnimator.ofFloat(0, 1);
                mMiddleTurnAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (animation.getAnimatedValue() instanceof Float) {
                            float animatedValue = (Float) animation.getAnimatedValue();
                            middleTurnHexagon.setAlpha(0.2f * (1 - animatedValue));
                            middleTurnHexagon.setScaleX(animatedValue);
                            middleTurnHexagon.setScaleY(animatedValue);
                        }
                    }
                });
                mMiddleTurnAnimator.setDuration(2500);
                mMiddleTurnAnimator.setInterpolator(new LinearInterpolator());
                mMiddleTurnAnimator.setRepeatCount(ValueAnimator.INFINITE);
                mMiddleTurnAnimator.start();

                middleTurnHexagon.setVisibility(View.VISIBLE);
            }
        }, 1000);

        mTextScore = findViewById(R.id.text_score);
    }

    private ValueAnimator mOuterAnimator;
    private ValueAnimator mMiddleAnimator;
    private ValueAnimator mMiddleTurnAnimator;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOuterAnimator != null) {
            mOuterAnimator.cancel();
            mOuterAnimator = null;
        }

        if (mMiddleAnimator != null) {
            mMiddleAnimator.cancel();
            mMiddleAnimator = null;
        }

        if (mMiddleTurnAnimator != null) {
            mMiddleTurnAnimator.cancel();
            mMiddleTurnAnimator = null;
        }
    }

    private TextView mTextScore;
}