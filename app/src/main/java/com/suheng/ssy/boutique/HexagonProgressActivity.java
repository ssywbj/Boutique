package com.suheng.ssy.boutique;

import android.graphics.Color;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.SeekBar;
import android.widget.TextView;

import com.suheng.ssy.boutique.view.SixProgressBar;

public class HexagonProgressActivity extends LaunchTypeActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hexagon_progress);
        this.initView();
    }

    private void initView() {
        SeekBar seekBar = findViewById(R.id.seek_bar_anim);
        final TextView textView = findViewById(R.id.text_anim_describe);
        final SixProgressBar sixProgressBar = findViewById(R.id.hexagon_progress_bar);

        //sixProgressBar.setShader(null);
        SweepGradient shader = new SweepGradient(0, 0, new int[]{Color.parseColor("#7D5BCD")
        , Color.parseColor("#61C0EE"), Color.parseColor("#7D5BCD")}, null);
        sixProgressBar.setShader(shader);
        sixProgressBar.setMax(seekBar.getMax());
        //seekBar.setProgress((int) sixProgressBar.getProgress());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sixProgressBar.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sixProgressBar.setOnProgressChangeInter(new SixProgressBar.OnProgressChangeInter() {
            @Override
            public void progress(float scaleProgress, float progress, float max) {
                textView.setText("总进度：" + max + ",当前进度" + progress + "动画进度：" + scaleProgress);
            }
        });

        textView.setText("总进度：" + sixProgressBar.getMax() + ",当前进度" + sixProgressBar.getProgress() + "动画进度：" + sixProgressBar.getProgress());
    }

}
