package com.example.wbj.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.wbj.utils.clip.ClipChangedListener;
import com.example.wbj.utils.clip.ClipManager;
import com.wbj.view.ClipWindow;

public class ClipService extends Service {
    private ClipManager mClipManager;
    private ClipWindow mClipWindow;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {//此方法只会调用一次
        super.onCreate();
        mClipManager = new ClipManager(this);
        mClipManager.registerListener(new ClipChangedListener(this) {
            @Override
            public void latestTextChange(String latestText) {
                Log.d(ClipManager.TAG, "clip latest text: " + latestText);
                if (mClipWindow == null) {
                    mClipWindow = new ClipWindow(ClipService.this);
                }
                mClipWindow.show();
                mClipWindow.setClipText(latestText);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mClipManager.unregisterListener();
            mClipWindow.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
