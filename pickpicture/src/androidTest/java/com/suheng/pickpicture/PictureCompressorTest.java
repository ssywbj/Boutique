package com.suheng.pickpicture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PictureCompressorTest {
    public static final String TAG = "PictureCompressor";
    PictureCompressor mPictureCompressor;

    @Before
    public void testStart() {
        Log.d(TAG, "picture compressor test begin");
        mPictureCompressor = new PictureCompressor();
    }

    @After
    public void testEnd() {
        Log.d(TAG, "picture compressor test end");
    }

    /**
     * 怎样测试异步代码：https://www.jianshu.com/p/8c9348ff4c15
     */
    @Test
    public void testCompressByQuality() throws Exception {
        String photoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                + File.separator + "national_emblem.jpg";
        //Looper.prepare();
        Object lock = new Object();
        new PictureCompressor().compressByQuality(photoPath);
        new PictureCompressor().compressByDimen(photoPath);
        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Looper.loop();
    }

    /**
     * 测试图片后缀名称
     */
    @Test
    public void testGetFileSuffix() {
        String picture = Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_DCIM) + File.separator + "大图_横图14863x6713_38.5M.png";
        PictureCompressor pictureCompressor = new PictureCompressor();
        assertEquals("png", pictureCompressor.getFileSuffix(picture));
        picture = Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_DCIM) + File.separator + "长图937x5000_1.92M.jpg";
        assertEquals("jpg", pictureCompressor.getFileSuffix(picture));
    }

    /**
     * 测试图片后缀名称
     */
    @Test
    public void testGetCompressFormat() {
        String fileSuffix = "png";
        assertEquals(Bitmap.CompressFormat.PNG, mPictureCompressor.getCompressFormat(fileSuffix));
        fileSuffix = "jpg";
        assertEquals(Bitmap.CompressFormat.JPEG, mPictureCompressor.getCompressFormat(fileSuffix));
    }

    @Test
    public void testByCompress() throws Exception {
        String photoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                + File.separator + "national_emblem.jpg";
        mPictureCompressor.compressByQuality(photoPath);
        mPictureCompressor.compressByDimen(photoPath);
    }

    @Test
    public void testGetNeedMemory() {
        String photoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                + File.separator + "大图_横图14863x6713_38.5M.png";
        //Log.d(TAG, "getNeedMemory: " + mPictureCompressor.getNeedMemory(photoPath));
    }

    /**
     * 测试大图产生OOM异常
     */
    @Test(expected = OutOfMemoryError.class)
    public void testCompressOOM() {
        String picture = Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_DCIM) + File.separator + "大图_横图14863x6713_38.5M.png";
        BitmapFactory.decodeFile(picture);
    }

    /**
     * 解决OOM异常
     */
    @Test
    public void testByCompress2() {
        final CountDownLatch latch = new CountDownLatch(1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String photoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                            + File.separator + "大图_坚图6713x14863_28.5M.png";
                    mPictureCompressor.compressByQuality(photoPath);
                    /*String photoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                            + File.separator + "大图_方图13386x13386_33.8M.jpg";
                    mPictureCompressor.compressByQuality(photoPath);*/
                    latch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
