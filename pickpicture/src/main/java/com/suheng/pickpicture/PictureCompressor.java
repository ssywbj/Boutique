package com.suheng.pickpicture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class PictureCompressor {
    private static final String TAG = PictureCompressor.class.getSimpleName();
    private static final int COMPRESS_BY_QUALITY = 0;
    private static final int COMPRESS_BY_SIZE = 1;
    private Bitmap mBitmap;
    private long mCompressTime;
    private int mMaxMemory;
    private long mMaxFileSize = 1024 * 1024;
    private String mPicturePath;
    private int mCompressType = COMPRESS_BY_QUALITY;

    public PictureCompressor() {
        mMaxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024 / 1024);
        Log.d(TAG, "max memory: " + mMaxMemory + "M");
    }

    /**
     * 按质量压缩
     */
    public void compressByQuality(String picturePath) throws Exception {
        mPicturePath = picturePath;
        mCompressType = COMPRESS_BY_QUALITY;

        mCompressTime = System.currentTimeMillis();
        this.compressByQuality(new File(picturePath), mPicturePath, 100);
    }

    /**
     * 按尺寸压缩（从压缩效率上看，按尺寸压缩要快于按质量压缩）
     */
    public void compressByDimen(String picturePath) throws Exception {
        mPicturePath = picturePath;
        mCompressType = COMPRESS_BY_SIZE;

        compressByDimen();
    }

    /**
     * 获取文件后缀
     */
    public String getFileSuffix(String picturePath) {
        if (TextUtils.isEmpty(picturePath) || !picturePath.contains(".")) {
            return "";
        }
        return picturePath.substring(picturePath.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 获取压缩制式
     */
    public Bitmap.CompressFormat getCompressFormat(String fileSuffix) {
        if ("png".equals(fileSuffix)) {
            return Bitmap.CompressFormat.PNG;
        } else if ("jpg".equals(fileSuffix) || "jpeg".equals(fileSuffix)) {
            return Bitmap.CompressFormat.JPEG;
        } else if ("webp".equals(fileSuffix)) {
            return Bitmap.CompressFormat.WEBP;
        } else {
            return Bitmap.CompressFormat.JPEG;
        }
    }

    /**
     * 获取图片每个像素点占用的内存，单位byte
     */
    private int getPixelUseMemory(Bitmap.Config preferredConfig) {
        if (preferredConfig == Bitmap.Config.ARGB_8888) {
            return 4;//(8 + 8 + 8 + 8) / 8
        } else if (preferredConfig == Bitmap.Config.ARGB_4444) {
            return 2;//(4 + 4 + 4 + 4) / 8
        } else if (preferredConfig == Bitmap.Config.RGB_565) {
            return 2;//(5 + 6 + 5) / 8;
        } else if (preferredConfig == Bitmap.Config.ALPHA_8) {
            return 1;//8/8
        } else {
            return 2;
        }
    }

    /**
     * 计算图片占用的内存大小，单位M
     */
    public int getNeedMemory(String picturePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);
        int pixelUseMemory = this.getPixelUseMemory(options.inPreferredConfig);
        int needMemory = options.outWidth * options.outHeight * pixelUseMemory / 1024 / 1024;
        Log.d(TAG, "pixel use memory = " + pixelUseMemory + "byte, need memory = " + needMemory + "M");
        return needMemory;
    }

    private BitmapFactory.Options getOptions() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        //如果图片占用的内存不小于应用可分配的最大内存，则先通过尺寸的压缩减少内存的占用，以避免OOM异常
        int needMemory = this.getNeedMemory(mPicturePath);
        if (needMemory >= mMaxMemory) {
            options.inSampleSize = (int) (1.0 * needMemory / mMaxMemory + 1);
        }
        options.inMutable = true;
        return options;
    }

    int mQuality = 40;

    /**
     * 按质量压缩
     */
    public void compressByQuality(File destFile, String picturePath, int quality) throws Exception {
        if (destFile.length() <= mMaxFileSize) {
            Log.d(TAG, "quality compress complete, take time: " + (1.0 * (System.currentTimeMillis()
                    - mCompressTime) / 1000) + "s, dest file = " + destFile);
            this.recycleBitmap();
        } else {
            final String fileSuffix = this.getFileSuffix(picturePath);
            if (mBitmap == null) {
                mBitmap = BitmapFactory.decodeFile(picturePath, this.getOptions());
            }
            String newPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                    + File.separator + "Quality" + System.currentTimeMillis() + "." + fileSuffix;
            File newFile = new File(newPath);
            //按质量压缩到原来的quality%（关键代码）,压成Bitmap.CompressFormat.PNG有会一直在循环压缩的问题，为什么？
            //mBitmap.compress(this.getCompressFormat(fileSuffix), quality, new FileOutputStream(newFile));
            mBitmap.compress(Bitmap.CompressFormat.JPEG, quality, new FileOutputStream(newFile));
            Log.d(TAG, "quality compress run, new file = " + newFile);
            compressByQuality(newFile, picturePath, mQuality);
            mQuality -= 10;
        }
    }

    private void recycleBitmap() {
        if (mBitmap != null) {
            if (!mBitmap.isRecycled()) {
                mBitmap.recycle();
            }
            mBitmap = null;
        }
        mQuality = 40;
    }

    /**
     * 按尺寸压缩
     */
    private void compressByDimen() throws Exception {
        final String fileSuffix = this.getFileSuffix(mPicturePath);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;//按尺寸压缩到原来的inSampleSize分之一（关键代码）
        Bitmap bitmap = BitmapFactory.decodeFile(mPicturePath, options);
        String newPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                + File.separator + "Dimen" + System.currentTimeMillis() + "." + fileSuffix;
        File newFile = new File(newPath);
        bitmap.compress(this.getCompressFormat(fileSuffix), 100, new FileOutputStream(newFile));
    }

}
