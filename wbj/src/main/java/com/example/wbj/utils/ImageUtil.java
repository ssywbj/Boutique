package com.example.wbj.utils;

import android.graphics.Bitmap;

import com.example.wbj.manager.FileManager;

import java.io.File;
import java.io.FileOutputStream;

public class ImageUtil {

    /**
     * 回收Bitmap对象
     *
     * @param bitmap 需要回收的Bitmap对象
     */
    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        bitmap.recycle();
    }

    /**
     * 保存Bitmap对象到文件中，默认存在sd卡的Picture目录下
     *
     * @param bitmap    需要保存的Bitmap对象
     * @param fileName  保存的文件名称
     * @param isRecycle 保存后是否回收Bitmap对象
     * @return File Bitmap保存后的文件对象
     */
    public static File saveBitmap(Bitmap bitmap, String fileName, boolean isRecycle) throws Exception {
        File imageFile = new File(FileManager.getFileManager().getImageDir(), fileName);
        if (imageFile.exists()) {
            imageFile.delete();
        }
        imageFile.createNewFile();
        FileOutputStream out = new FileOutputStream(imageFile);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        out.flush();
        out.close();
        if (isRecycle) {
            recycleBitmap(bitmap);
        }
        return imageFile;
    }

    public static File saveBitmap(Bitmap bitmap, String fileName) throws Exception {
        return saveBitmap(bitmap, fileName, true);
    }

}
