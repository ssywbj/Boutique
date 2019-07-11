package com.suheng.pickpicture;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class PicturePicker extends Activity {
    private static final String TAG = PicturePicker.class.getSimpleName();
    private static final int REQUEST_CODE_TAKE_PHOTO = 0x11;
    private static final int REQUEST_CODE_PICK_PICTURE = 0x12;
    private static final String DATA_KEY_OPEN_TYPE = "data_key_open_type";
    private static final String DATA_KEY_MAX_FILE_SIZE = "data_key_max_file_size";
    private static PickerListener sPickerListener;
    private String mPhotoPath;
    private long mMaxFileSize;
    private Bitmap mBitmap;
    private long mCompressTime;
    private int mMaxMemory;

    private static void openPicker(Activity activity, PickerListener pickerListener
            , int openType, long maxFileSize) {
        Intent intent = new Intent(activity, PicturePicker.class);
        intent.putExtra(DATA_KEY_OPEN_TYPE, openType);
        intent.putExtra(DATA_KEY_MAX_FILE_SIZE, maxFileSize);
        activity.startActivity(intent);

        sPickerListener = pickerListener;
    }

    public static void openAlbum(Activity activity, PickerListener pickerListener, long maxFileSize) {
        openPicker(activity, pickerListener, REQUEST_CODE_PICK_PICTURE, maxFileSize);
    }

    public static void openAlbum(Activity activity, PickerListener pickerListener) {
        openAlbum(activity, pickerListener, 0);
    }

    public static void openCamera(Activity activity, PickerListener pickerListener, long maxFileSize) {
        openPicker(activity, pickerListener, REQUEST_CODE_TAKE_PHOTO, maxFileSize);
    }

    public static void openCamera(Activity activity, PickerListener pickerListener) {
        openCamera(activity, pickerListener, 0);
    }

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

            mMaxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024 / 1024);
            Log.d(TAG, "max memory: " + mMaxMemory + "M");
        } catch (Exception e) {
            Log.e(TAG, "open picture picker exception: " + e.toString());
            finish();
        }
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
                    + File.separator + "Origin" + System.currentTimeMillis() + ".jpg";
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

    /**
     * 拍照后，更新系统图库
     */
    private void updateSystemAlbum(String photoPath) {
        MediaScannerConnection.scanFile(this, new String[]{photoPath}
                , new String[]{"image/jpeg", "image/png"}, null);
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

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                new PictureCompressor().compressByQuality(mPhotoPath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    /*new PictureCompressor().compressByQuality(mPhotoPath);
                    new PictureCompressor().compressByDimen(mPhotoPath);
                    PictureCompressor pictureCompressor = new PictureCompressor();
                    pictureCompressor.compressByQuality(mPhotoPath);
                    pictureCompressor.compressByDimen(mPhotoPath);*/

                    if (mMaxFileSize > 0) {//如果传进来的最大文件体积大于0，那么需要压缩后返回压缩后的路径，否则返回原始路径
                        final File file = new File(mPhotoPath);
                        if (mMaxFileSize >= file.length()) {//如果传进来的最大文件大小大于文件大小，那么不需要压缩后再返回
                            if (mMaxFileSize >= mMaxMemory) {//但如果传进来的最大文件体积大于可分配的内存，那么还是需要压缩后再返回，以免报OOM异常
                                Log.d(TAG, "max file size bigger than file length and max memory");
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            File compress = compress(file);
                                            compressFile(compress, compress);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            } else {
                                Log.d(TAG, "don't need complete: mMaxFileSize = " + mMaxFileSize + ", file.length = " + file.length());
                                this.notifyListener(mPhotoPath);
                            }
                        } else {
                            this.compressFile(file);
                        }
                    } else {
                        this.notifyListener(mPhotoPath);
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

    private void compressFile(final File file) {
        mCompressTime = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String tmpPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                            + File.separator + "Tmp" + System.currentTimeMillis() + ".jpg";
                    File tmpFile = new File(tmpPath);
                    copyFile(file, tmpFile);
                    compressFile(tmpFile, tmpFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private File compressFile(final File destFile, File originFile) {
        try {
            if (destFile.length() <= mMaxFileSize) {
                mCompressTime = (System.currentTimeMillis() - mCompressTime);
                Log.d(TAG, "compress complete, take time: " + (1.0 * mCompressTime / 1000) + "s");
                this.recycleBitmap();
                //originFile.delete();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyListener(destFile.getAbsolutePath());
                    }
                });
                return destFile;
            } else {
                File newFile = compress(originFile);
                return compressFile(newFile, originFile);
            }
        } catch (Exception e) {
            Log.e(TAG, "compress image throwable: " + e.toString());
            this.recycleBitmap();
            return null;
        }
    }

    @NonNull
    private File compress(File originFile) throws Exception {
        if (mBitmap == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(originFile.getAbsolutePath(), options);
            int imageWidth = options.outWidth;
            int imageHeight = options.outHeight;
            String imageType = options.outMimeType;
            Bitmap.Config preferredConfig = options.inPreferredConfig;
            int needMemory = imageWidth * imageHeight * 4 / 1024 / 1024;
            Log.d(TAG, "imageWidth = " + imageWidth + ", imageHeight = " + imageHeight + ", imageType = "
                    + imageType + ", preferredConfig = " + preferredConfig + ", need memory = " + needMemory + "M");
            if (needMemory >= mMaxMemory) {
                options.inSampleSize = (int) (1.0 * needMemory / mMaxMemory + 1);//按尺寸压缩到原来的inSampleSize分之一
                Log.d(TAG, "picture prepare handle before put it in memory, inSampleSize = " + options.inSampleSize);
            }
            options.inMutable = true;
            options.inJustDecodeBounds = false;
            mBitmap = BitmapFactory.decodeFile(originFile.getAbsolutePath(), options);
        }

        String newPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                + File.separator + "Compress" + System.currentTimeMillis() + ".jpg";
        File newFile = new File(newPath);
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 30, new FileOutputStream(newFile));//按质量压缩到原来的30%
        return newFile;
    }

    private void notifyListener(String photoPath) {
        if (sPickerListener != null) {
            sPickerListener.obtainPicture(photoPath);
        }
    }

    /**
     * 复制文件
     *
     * @param source 被复制的文件
     * @param dest   复制到的文件
     */
    private void copyFile(File source, File dest) throws Exception {
        FileChannel inputChannel = new FileInputStream(source).getChannel();
        FileChannel outputChannel = new FileOutputStream(dest).getChannel();
        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        inputChannel.close();
        outputChannel.close();
    }

    private void recycleBitmap() {
        if (mBitmap != null) {
            if (!mBitmap.isRecycled()) {
                mBitmap.recycle();
            }
            mBitmap = null;
        }
    }

    public static void releaseSource() {
        sPickerListener = null;
    }
}
