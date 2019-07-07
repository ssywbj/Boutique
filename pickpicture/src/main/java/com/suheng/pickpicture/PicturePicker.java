package com.suheng.pickpicture;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;

public class PicturePicker extends Activity {
    private static final String TAG = PicturePicker.class.getSimpleName();
    private static final int REQUEST_CODE_TAKE_PHOTO = 0x11;
    private static final int REQUEST_CODE_PICK_PICTURE = 0x12;
    private static final String DATA_KEY_OPEN_TYPE = "data_key_open_type";
    private static final String DATA_KEY_MAX_FILE_SIZE = "data_key_max_file_size";
    private static PickerListener sPickerListener;
    private String mPhotoPath;
    private long mMaxFileSize;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Intent intent = getIntent();
            if (intent.getIntExtra(DATA_KEY_OPEN_TYPE, REQUEST_CODE_PICK_PICTURE) == REQUEST_CODE_TAKE_PHOTO) {
                this.openCamera();
            } else {
                this.openAlbum();
            }
            mMaxFileSize = intent.getLongExtra(DATA_KEY_MAX_FILE_SIZE, 0);
        } catch (Exception e) {
            Log.e(TAG, "open picture picker exception: " + e.toString());
            finish();
        }
    }

    private static void openPicker(Activity activity, PickerListener pickerListener
            , int openType, long compressSize) {
        Intent intent = new Intent(activity, PicturePicker.class);
        intent.putExtra(DATA_KEY_OPEN_TYPE, openType);
        intent.putExtra(DATA_KEY_MAX_FILE_SIZE, compressSize);
        activity.startActivity(intent);

        sPickerListener = pickerListener;
    }

    public static void openAlbum(Activity activity, PickerListener pickerListener) {
        openPicker(activity, pickerListener, REQUEST_CODE_PICK_PICTURE, 0);
    }

    public static void openAlbum(Activity activity, PickerListener pickerListener, int compressSize) {
        openPicker(activity, pickerListener, REQUEST_CODE_PICK_PICTURE, compressSize);
    }

    public static void openCamera(Activity activity, PickerListener pickerListener) {
        openPicker(activity, pickerListener, REQUEST_CODE_TAKE_PHOTO, 0);
    }

    public static void openCamera(Activity activity, PickerListener pickerListener, int compressSize) {
        openPicker(activity, pickerListener, REQUEST_CODE_TAKE_PHOTO, compressSize);
    }

    /**
     * 调用系统相册
     */
    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_PICTURE);
    }

    /**
     * 调用系统相机
     */
    private void openCamera() {
        try {
            mPhotoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    + File.separator + "origin_" + System.currentTimeMillis() + ".jpg";
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri = this.getFileUri(new File(mPhotoPath));
            Log.d(TAG, "uri = " + uri);
            //指定图片存放位置。指定后，在onActivityResult里得到的data将为null
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
        } catch (Exception e) {
            Log.e(TAG, "open camera exception: " + e.toString());
            finish();
        }
    }

    /**
     * 获取图片uri
     */
    private Uri getFileUri(File file) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Uri.fromFile(file);
        } else {
            return FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }

        switch (requestCode) {
            case REQUEST_CODE_TAKE_PHOTO:
            case REQUEST_CODE_PICK_PICTURE:
                try {
                    if (requestCode == REQUEST_CODE_PICK_PICTURE) {
                        if (data == null || data.getData() == null) {
                            return;
                        }
                        String[] projection = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(data.getData(), projection, null, null, null);
                        if (cursor == null) {
                            return;
                        }
                        cursor.moveToFirst();
                        mPhotoPath = cursor.getString(cursor.getColumnIndex(projection[0]));
                        cursor.close();
                    } else {
                        this.updateSystemAlbum(mPhotoPath);
                    }

                    if (mMaxFileSize > 0) {
                    } else {
                        if (sPickerListener != null) {
                            sPickerListener.obtainPicture(mPhotoPath);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "on activity result exception: " + e.toString());
                    finish();
                }
                break;
            default:
                break;
        }
        finish();
    }

    /**
     * 拍照后，更新系统图库
     */
    private void updateSystemAlbum(String photoPath) {
        MediaScannerConnection.scanFile(this, new String[]{photoPath}
                , new String[]{"image/jpeg", "image/png"}, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sPickerListener = null;
    }
}
