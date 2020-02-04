package com.suheng.ssy.boutique;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.suheng.ssy.boutique.view.ThresholdProgressBar;
import com.suheng.ssy.boutique.view.ScoreChangeView;

public class PaintDemoActivity extends LaunchTypeActivity {
    private static final String TAG = PaintDemoActivity.class.getSimpleName();
    private int mCurrentColor;
    private ScoreChangeView mScoreChangeView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_demo);
        mScoreChangeView = findViewById(R.id.score_change_view);
        mScoreChangeView.setDefaultScore(100);
        mCurrentColor = this.getCurrentColor(100);
        mScoreChangeView.setScoreColor(mCurrentColor);
        try {
            mScoreChangeView.scoreChange(-29);
        } catch (Exception e) {
            Log.e(TAG, "scoreChange error -> " + e.toString());
        }
        mScoreChangeView.setScoreChangeListener(new ScoreChangeView.ScoreChangeListener() {
            @Override
            public void onScoreChange(int score) {
                Log.d(TAG, "OnScoreChange(), score = " + score);
                if (mCurrentColor == getCurrentColor(score)) {
                    return;
                }
                int beforeColor = mCurrentColor;
                mCurrentColor = getCurrentColor(score);
                colorChangeAnim(beforeColor, mCurrentColor);
            }
        });
        mScoreChangeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mScoreChangeView.scoreChange(10);
                } catch (Exception e) {
                    Log.e(TAG, "scoreChange error -> " + e.toString());
                }
            }
        });

        this.initProgressNoteBar();
    }

    private void initProgressNoteBar() {
        ThresholdProgressBar thresholdProgressBar = findViewById(R.id.progress_note_bar);
        thresholdProgressBar.setMax(100);
        thresholdProgressBar.setProgress(13);
        thresholdProgressBar.setProgressDrawable(ContextCompat.getDrawable(this, R.drawable.sim_card_progress_bar_select));
        thresholdProgressBar.setThreshold((int) (thresholdProgressBar.getMax() * 0.01));
    }

    private void colorChangeAnim(final int startColor, final int fromColor) {
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                int red = Color.red(startColor) - (Color.red(startColor) - Color.red(fromColor)) * progress / 100;
                int green = Color.green(startColor) - (Color.green(startColor) - Color.green(fromColor)) * progress / 100;
                int blue = Color.blue(startColor) - (Color.blue(startColor) - Color.blue(fromColor)) * progress / 100;

                mScoreChangeView.setScoreColor(Color.rgb(red, green, blue));
            }
        });
        animator.setDuration(1500);
        animator.start();
    }

    private static final int COLOR_GOOD_STATE = Color.parseColor("#318FEE");
    private static final int COLOR_MIDDLE_STATE = Color.parseColor("#7200FF");
    private static final int COLOR_TERRIBLE_STATE = Color.parseColor("#F600FF");
    private static final int COLOR_WORST_STATE = Color.parseColor("#E32C2C");

    private int getCurrentColor(int score) {
        int color;
        if (score >= 90) {
            color = COLOR_GOOD_STATE;
        } else if (score >= 80) {
            color = COLOR_MIDDLE_STATE;
        } else if (score >= 60) {
            color = COLOR_TERRIBLE_STATE;
        } else {
            color = COLOR_WORST_STATE;
        }
        return color;
    }

}
