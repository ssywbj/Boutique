package com.example.wbj.utils;

import android.content.Context;

/**
 * 使用到Glide框架，需要在build文件中加入：compile 'com.github.bumptech.glide:glide:3.7.0'
 */
public class ImageCompressor {
    private static final String TAG = ImageCompressor.class.getSimpleName();
    private static final int MSG_COMPRESS_AGAIN = 11;
    private static ImageCompressor sImageCompressor = new ImageCompressor();
    private Context mContext;

    private ImageCompressor() {
    }

    public static ImageCompressor with(Context context) {
        sImageCompressor.mContext = context;
        return sImageCompressor;
    }

    public void compress(String path, double... params) {
        if (params == null || params.length == 0) {
            //new CompressTask(320, 240).execute(path);
        } else {
            if (params.length == 1) {
                //new CompressTask(params[0]).execute(path);
            } else {
                //new CompressTask((int) params[0], (int) params[1]).execute(path);
            }
        }
    }

    /*private class CompressTask extends AsyncTask<String, Void, ImageInfo> {
        private ImageInfo mImageInfo;
        private double mMaxCompressSize = 0.4 * 1024 * 1024;//1M = 1024KB = (1024 * 1024)B;
        private int mCompressWidth;
        private int mCompressHeight;
        private boolean mCompressBySize;
        private long mBeginTime;

        private WeakHandler mHandler;
        private File mSizeTempFile;
        private int mCompressCount;
        private int mBasicTimes;

        public CompressTask(double maxCompressSize) {
            mMaxCompressSize = maxCompressSize;
            mCompressBySize = true;
            mHandler = new WeakHandler(this);
        }

        public CompressTask(int compressWidth, int compressHeight) {
            mCompressWidth = compressWidth;
            mCompressHeight = compressHeight;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBeginTime = System.currentTimeMillis();
        }

        @Override
        protected ImageInfo doInBackground(String... params) {
            return this.queryPicture(params[0]);
        }

        private ImageInfo queryPicture(String path) {
            ImageInfo imageInfo = null;
            String columns[] = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.WIDTH,
                    MediaStore.Images.Media.HEIGHT, MediaStore.Images.Media.SIZE};
            String selection = MediaStore.Images.Media.DATA + " like ?";//查询条件
            String[] selectionArgs = {path + "%"};//查询条件的值
            try {
                Cursor cursor = mContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        columns, selection, selectionArgs, null);
                if (cursor.moveToFirst()) {
                    String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    int width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH));
                    int height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT));
                    long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));
                    imageInfo = new ImageInfo(imagePath, width, height, size);
                    Log.i(TAG, "queryPicture: " + imageInfo);
                }
                cursor.close();
            } catch (Exception e) {
                Log.e(TAG, "queryPicture: " + e.toString(), new Exception());
            }
            return imageInfo;
        }

        @Override
        protected void onPostExecute(ImageInfo imageInfo) {
            super.onPostExecute(imageInfo);
            if (mCompressBySize) {
                mImageInfo = imageInfo;
                mBasicTimes = (int) (imageInfo.getSize() / mMaxCompressSize) + 1;
                this.compress(new File(mImageInfo.getPath()));
            } else {
                this.compress(imageInfo);
            }
        }

        private void compress(final ImageInfo imageInfo) {
            if (imageInfo.getSize() > mMaxCompressSize) {
                Log.i(TAG, "resolution begin: " + imageInfo.getPath());
                if (imageInfo.getWidth() < imageInfo.getHeight()) {
                    int temp = mCompressWidth;
                    mCompressWidth = mCompressHeight;
                    mCompressHeight = temp;
                } else if (imageInfo.getWidth() == imageInfo.getHeight()) {
                    mCompressHeight = mCompressWidth;
                }
                Glide.with(mContext).load(imageInfo.getPath()).asBitmap().into(new SimpleTarget<Bitmap>(mCompressWidth, mCompressHeight) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        try {
                            ImageUtil.saveBitmap(resource, "resolution_" + new Date().getTime() + ".png", false);
                            Log.e(TAG, "resolution end: " + imageInfo.getPath() + ", take time = "
                                    + 1.0 * (System.currentTimeMillis() - mBeginTime) / 1000 + "s");
                        } catch (Exception e) {
                            Log.e(TAG, "resolution error-->" + e.toString(), new Exception());
                        }
                    }
                });//必须在主线程中调用
            } else {
                Log.e(TAG, "resolution end, don't need to compress: " + imageInfo.getPath());
            }
        }

        private void compress(File file) {
            if (file.length() > mMaxCompressSize) {
                mCompressCount++;
                int width = (int) (mImageInfo.getWidth() / (mBasicTimes + Math.pow(2, mCompressCount)));
                int height = (int) (mImageInfo.getHeight() / (mBasicTimes + Math.pow(2, mCompressCount)));
                Log.i(TAG, "size begin, " + mImageInfo.getPath() + "\nfile size: " + file.length() + ", compress count: " + mCompressCount
                        + ", width: " + width + ", height: " + height + ", 2'n: " + Math.pow(2, mCompressCount) + ", basic times: " + mBasicTimes);
                Glide.with(mContext).load(mImageInfo.getPath()).asBitmap().into(new ImageSizeTarget(width, height));
            } else {
                Log.e(TAG, "size end, " + mImageInfo.getPath() + "\nfile size: " + file.length() + ", compress count:" + mCompressCount
                        + ", take time:" + 1.0 * (System.currentTimeMillis() - mBeginTime) / 1000 + "s");
                if (mCompressCount > 0 && mSizeTempFile != null) {
                    Log.e(TAG, "size end, final file-->" + mSizeTempFile.getPath());
                }
            }
        }

        private class ImageSizeTarget extends SimpleTarget<Bitmap> {

            public ImageSizeTarget(int width, int height) {
                super(width, height);
            }

            @Override
            public void onResourceReady(final Bitmap bitmap, GlideAnimation glideAnimation) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (mSizeTempFile != null && mSizeTempFile.exists()) {
                                mSizeTempFile.delete();
                            }
                            mSizeTempFile = ImageUtil.saveBitmap(bitmap, "size_" + new Date().getTime() + ".png", false);
                            Message msg = mHandler.obtainMessage();
                            msg.what = MSG_COMPRESS_AGAIN;
                            msg.obj = mSizeTempFile;
                            mHandler.sendMessage(msg);
                        } catch (Exception e) {
                            Log.e(TAG, "size error, " + mImageInfo.getPath() + "-->" + e.toString(), new Exception());
                        }
                    }
                }).start();
            }
        }

    }

    private static class WeakHandler extends Handler {
        private WeakReference<CompressTask> mWeakReference;

        private WeakHandler(CompressTask instance) {
            mWeakReference = new WeakReference<>(instance);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            CompressTask instance = mWeakReference.get();
            if (instance == null) {
                return;
            }

            switch (msg.what) {
                case MSG_COMPRESS_AGAIN:
                    instance.compress((File) msg.obj);
                    break;
                default:
                    break;
            }
        }
    }

    private class ImageInfo {
        private String mPath;
        private int mWidth;
        private int mHeight;
        private long mSize;

        public ImageInfo(String path, int width, int height, long size) {
            mPath = path;
            mWidth = width;
            mHeight = height;
            mSize = size;
        }

        public String getPath() {
            return mPath;
        }

        public int getWidth() {
            return mWidth;
        }

        public int getHeight() {
            return mHeight;
        }

        public long getSize() {
            return mSize;
        }

        @Override
        public String toString() {
            return "ImageInfo{" +
                    "mPath='" + mPath + '\'' +
                    ", mWidth=" + mWidth +
                    ", mHeight=" + mHeight +
                    ", mSize=" + mSize +
                    '}';
        }
    }*/

}
