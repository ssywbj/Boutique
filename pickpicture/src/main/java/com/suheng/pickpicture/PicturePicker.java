package com.suheng.pickpicture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;

public class PicturePicker extends Activity {
    private static final int REQUEST_CODE_TAKE_PICTURE = 101;
    private static final int REQUEST_CODE_PICK_PICTURE = 102;
    private static final String DATA_KEY_OPEN_TYPE = "data_key_open_type";
    private static final String DATA_KEY_PICK_CALLBACK = "data_key_pick_callback";
    private PickCallback mPickCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        } else {
            int openType = intent.getIntExtra(DATA_KEY_OPEN_TYPE, REQUEST_CODE_PICK_PICTURE);
            if (openType == REQUEST_CODE_TAKE_PICTURE) {
                this.openCamera();
            } else {
                this.openAlbum();
            }

            Parcelable serializable = intent.getParcelableExtra(DATA_KEY_PICK_CALLBACK);
            if (serializable instanceof PickCallback) {
                mPickCallback = (PickCallback) serializable;
            }
        }
    }


    public static void openAlbum(Activity activity, PickCallback pickCallback) {
        Intent intent = new Intent(activity, PicturePicker.class);
        intent.putExtra(DATA_KEY_OPEN_TYPE, REQUEST_CODE_PICK_PICTURE);
        intent.putExtra(DATA_KEY_PICK_CALLBACK, pickCallback);
        intent.setExtrasClassLoader(PickCallback.class.getClassLoader());
        activity.startActivity(intent);
    }

    private static PickerListener sPickerListener;

    public static void openAlbum(Activity activity, PickerListener pickerListener) {
        Intent intent = new Intent(activity, PicturePicker.class);
        intent.putExtra(DATA_KEY_OPEN_TYPE, REQUEST_CODE_PICK_PICTURE);
        intent.setExtrasClassLoader(PickCallback.class.getClassLoader());
        activity.startActivity(intent);

        sPickerListener = pickerListener;
    }

    /**
     * 调用系统相册
     */
    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_PICK_PICTURE:
                if (mPickCallback != null) {
                    mPickCallback.obtainPicture("path");
                }

                if (sPickerListener != null) {
                    sPickerListener.obtainPicture("path");
                }
                break;
            case REQUEST_CODE_TAKE_PICTURE:
                break;
            default:
                break;
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sPickerListener = null;
        Log.d("WBJ", "PicturePicker, onDestroy");
    }

    private File tempFile;

    /**
     * 调用系统相机
     */
    private void openCamera() {
        tempFile = createTempFile(this);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = this.getFileUri(this, tempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri); //指定图片存放位置，指定后，在onActivityResult里得到的Data将为null
        startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
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
