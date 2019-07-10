package com.suheng.pickpicture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class PictureCompressor implements Runnable {
    private static final String TAG = PictureCompressor.class.getSimpleName();
    private static final int COMPRESS_BY_QUALITY = 0;
    private static final int COMPRESS_BY_SIZE = 1;
    private Bitmap mBitmap;
    private long mCompressTime;
    private int mMaxMemory;
    private long mMaxFileSize;
    private String mPicturePath;
    //private WeakHandler mHandler = new WeakHandler(this);
    private int mCompressType = COMPRESS_BY_QUALITY;

    /**
     * 按质量压缩
     */
    public void compressByQuality(String picturePath) {
        mPicturePath = picturePath;
        mCompressType = COMPRESS_BY_QUALITY;
        new Thread(this).start();
    }

    /**
     * 按质量压缩
     */
    private void compressByQuality() throws Exception {
        Bitmap bitmap = BitmapFactory.decodeFile(mPicturePath);
        String newPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                + File.separator + "Quality" + System.currentTimeMillis() + ".jpg";
        File newFile = new File(newPath);
        int quality = 50;//按质量压缩到原来的50%（关键代码）
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, new FileOutputStream(newFile));
    }

    /**
     * 按尺寸压缩
     */
    public void compressByDimen(String picturePath) {
        mPicturePath = picturePath;
        mCompressType = COMPRESS_BY_SIZE;
        new Thread(this).start();
    }

    /**
     * 按尺寸压缩
     */
    private void compressByDimen() throws Exception {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;//按尺寸压缩到原来的inSampleSize分之一（关键代码）
        Bitmap bitmap = BitmapFactory.decodeFile(mPicturePath, options);
        String newPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                + File.separator + "Dimen" + System.currentTimeMillis() + ".jpg";
        File newFile = new File(newPath);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(newFile));
    }

    @Override
    public void run() {
        try {
            mCompressTime = System.currentTimeMillis();
            if (mCompressType == COMPRESS_BY_QUALITY) {
                this.compressByQuality();
                Log.d(TAG, "quality compress complete, take time: " + (1.0 * (System.currentTimeMillis() - mCompressTime) / 1000) + "s");
            } else {
                this.compressByDimen();
                Log.d(TAG, "dimen compress complete, take time: " + (1.0 * (System.currentTimeMillis() - mCompressTime) / 1000) + "s");
            }
            //从压缩效率上看，按尺寸压缩要快于按质量压缩
        } catch (Exception e) {
            Log.e(TAG, "compress picture throwable: " + e.toString());
        }
    }

    /*private static class WeakHandler extends Handler {
        private WeakReference<PictureCompressor> mWeakReference;

        private WeakHandler(PictureCompressor instance) {
            mWeakReference = new WeakReference<>(instance);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PictureCompressor instance = mWeakReference.get();
            if (instance == null) {
                return;
            }
        }
    }*/

}
