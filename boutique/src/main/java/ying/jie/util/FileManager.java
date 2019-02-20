package ying.jie.util;

import android.os.Environment;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 应用的文件管理器：以应用的英文名建立根目录，该应用的所有文件和文件夹都保存在该目录下
 */
public class FileManager implements Runnable {
    private static final String TAG = FileManager.class.getSimpleName();
    private static final String DIR_ROOT = "BoutiqueApp";
    public static final String DIR_SCREEN_CAPTURE = "ScreenCapture";
    private static FileManager mFileManager = new FileManager();
    private File mRootDir;

    private FileManager() {
    }

    public void createRootDir() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            mRootDir = new File(Environment.getExternalStorageDirectory(), DIR_ROOT);
            if (!mRootDir.exists()) {
                mRootDir.mkdirs();
            }
        } else {
            LogUtil.e(TAG, "SD卡不存在或已经挂载");
        }
    }

    public void saveInfoToSDCard(String childDir, String fileName, byte[] data) {
        if (mRootDir == null) {
            LogUtil.e(TAG, "SD卡不存在或已经挂载");
            return;
        }

        if ("".equals(fileName)) {
            LogUtil.e(TAG, "请传入有效的文件名称");
            return;
        }

        if (data == null || data.length <= 0) {
            LogUtil.e(TAG, "请传入有效的数据");
            return;
        }

        File saveDir;
        if ("".equals(fileName)) {
            saveDir = mRootDir;
        } else {
            saveDir = new File(mRootDir, childDir);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
        }

        this.saveInfoToSDCard(new File(saveDir, fileName), data);
    }

    public void saveInfoToSDCard(final String childDir, final String fileName, final String data) {
        if (TextUtils.isEmpty(data)) {
            LogUtil.e(TAG, "请传入有效的数据");
            return;
        }
        this.saveInfoToSDCard(childDir, fileName, data.getBytes());
    }

    public void saveInfoToSDCard(File file, byte[] data) {
        try {
            OutputStream out = new FileOutputStream(file);
            out.write(data);
            out.close();
        } catch (IOException e) {
            LogUtil.e(TAG, "写入文件时失败-->" + e.toString());
        }
    }

    @Override
    public void run() {
    }

    /**
     * 从输入流中读取数据
     *
     * @param inputStream 输入流
     * @return 比特数组
     */
    public static byte[] readInputStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inputStream.close();
        return data;
    }

    public static FileManager getFileManager() {
        return mFileManager;
    }

}

