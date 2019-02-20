package com.example.wbj;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.aaron.library.MLog;
import com.example.wbj.info.ScreenInfo;
import com.example.wbj.manager.FileManager;

public class WbjApp extends Application {
    private static final String TAG = WbjApp.class.getSimpleName();

    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        Log.d(TAG, "wbj app create: " + sContext);
        ScreenInfo.getScreenInfo().init();
        FileManager.getFileManager().init();

        MLog.init(BuildConfig.LOG_DEBUG);
        MLog.d("WbjApp create!!");
    }

}
