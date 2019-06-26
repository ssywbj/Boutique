package com.suheng.pickpicture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import java.io.File;

public class PickPicture {
    private static final int REQUEST_CODE_TAKE_PICTURE = 101;
    private static final int REQUEST_CODE_PICK_PICTURE = 102;
    private File tempFile;

    /**
     * 调用系统相机
     */
    public void openCamera(Activity activity) {
        tempFile = createTempFile(activity);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = this.getFileUri(activity, tempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri); //指定图片存放位置，指定后，在onActivityResult里得到的Data将为null
        activity.startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
    }

    /**
     * 调用系统相册
     */
    public void openAlbum(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_PICTURE);
    }

    /**
     * 获取图片uri
     */
    private Uri getFileUri(Activity activity, File file) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Uri.fromFile(file);
        } else {
            return FileProvider.getUriForFile(activity, activity.getApplicationContext()
                    .getPackageName() + ".fileprovider", file);
        }
    }

    /**
     * 创建临时文件
     */
    private File createTempFile(@NonNull Context context) {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }
        return new File(cacheDir, System.currentTimeMillis() + ".jpg");
    }



    /*
     * 返回sdcard的cacheDir， 否则返回内部的
     private File getCacheDir(@NonNull Context context) {
     File cacheDir = context.getExternalCacheDir();
     if (cacheDir == null) {
     cacheDir = context.getCacheDir();
     }
     return cacheDir;
     }*/
}
