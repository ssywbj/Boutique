package com.suheng.pickpicture;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

@RunWith(AndroidJUnit4.class)
public class PictureCompressorTest {
    public static final String TAG = PictureCompressorTest.class.getSimpleName();
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
    public void testCompressByQuality() {
        String photoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
                + File.separator + "hand_front_face.jpg";
        Log.d(TAG, "file length = " + new File(photoPath).length());
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
}
