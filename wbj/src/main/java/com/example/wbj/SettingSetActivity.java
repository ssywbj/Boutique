package com.example.wbj;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.wbj.utils.ScreenshotObserver;

public class SettingSetActivity extends AppCompatActivity {
    public static final String TAG = SettingSetActivity.class.getSimpleName();
    private ScreenshotObserver mScreenshotObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_set_aty);
        mScreenshotObserver = new ScreenshotObserver(this);
        mScreenshotObserver.setChangeListener(new ScreenshotObserver.ChangeListener() {
            @Override
            public void notifyChange(String imagePath) {
                Log.d(TAG, "imagePath: " + imagePath + ", " + Thread.currentThread().getName());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mScreenshotObserver.startWatching();
        mScreenshotObserver.registerObserver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mScreenshotObserver.stopWatching();
        mScreenshotObserver.unregisterObserver();
    }

    /*private ScreenshotObserver mScreenshotObserver;

    *//**
     * <p>1.FileObserver只能监听文件夹中子文件和子文件夹的变化情况，不能监听子文件夹内部的资源变化</p>
     * <p>2.需要READ_EXTERNAL_STORAGE、WRITE_EXTERNAL_STORAGE、MOUNT_UNMOUNT_FILESYSTEMS权限</p>
     * <p>3.android 6.0存在bug：监听不到目录的变动，只能监听指定的具体文件（shit，搞毛呀！）</p>
     *//*
    private class ScreenshotObserver extends FileObserver {

        public ScreenshotObserver(String path) {
            super(path);
        }

        *//**
     * @param event <p>事件说明如下：</p>
     *              <p>1.FileObserver.ACCESS：读取某个文件</p>
     *              <p>2.FileObserver.MODIFY：向某个文件写入数据</p>
     *              <p>4.FileObserver.ATTRIB：文件的属性被修改（权限/日期/拥有者）</p>
     *              <p>8.FileObserver.CLOSE_WRITE：写入数据后关闭（可以看成是存储完成操作）</p>
     *              <p>16.FileObserver.CLOSE_NOWRITE：只读模式打开文件后关闭</p>
     *              <p>32.FileObserver.OPEN：打开某个文件</p>
     *              <p>64.FileObserver.MOVED_FROM：有文件或者文件夹从被监听的文件夹中移走</p>
     *              <p>128.FileObserver.MOVED_TO：有文件或者文件夹移动到被监听的文件夹</p>
     *              <p>256.FileObserver.CREATE：文件或者文件夹被创建</p>
     *              <p>512.FileObserver.DELETE：文件被删除</p>
     *              <p>1024.FileObserver.DELETE_SELF：被监听的文件或者目录被删除</p>
     *              <p>2048.FileObserver.MOVE_SELF：被监听的文件或者目录被移走</p>
     * @param path
     *//*
        @Override
        public void onEvent(int event, String path) {
            Log.d(TAG, "onEvent: " + event + "," + path);
        }
    }*/


}
