package com.example.wbj.manager;

import android.os.Environment;

import com.example.wbj.R;
import com.example.wbj.WbjApp;

import java.io.File;

public class FileManager {

    private static FileManager sFileManager = new FileManager();
    private File mAppDir;
    private File mImageDir;

    public static FileManager getFileManager() {
        return sFileManager;
    }

    private FileManager() {
    }

    public void init() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            mAppDir = new File(Environment.getExternalStorageDirectory(), WbjApp.getContext().getString(R.string.app_name));
            if (!mAppDir.exists()) {
                mAppDir.mkdirs();
            }

            mImageDir = new File(mAppDir, "Image");
            if (!mImageDir.exists()) {
                mImageDir.mkdir();
            }
        }
    }

    public File getAppDir() {
        return mAppDir;
    }

    public File getImageDir() {
        return mImageDir;
    }

}
