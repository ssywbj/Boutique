package com.example.wbj.info;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.wbj.WbjApp;
import com.example.wbj.basic.BasicInfo;

public class ScreenInfo extends BasicInfo {

    private static ScreenInfo sScreenInfo = new ScreenInfo();
    private DisplayMetrics mRealMetrics;

    private ScreenInfo() {
    }

    public static ScreenInfo getScreenInfo() {
        return sScreenInfo;
    }

    public DisplayMetrics getRealMetrics() {
        return mRealMetrics;
    }

    @Override
    public void init() {
        mRealMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) WbjApp.getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getRealMetrics(mRealMetrics);
    }

}
