package ying.jie.util;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BitmapUtil {

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.flush();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void saveImageFile(final Bitmap bmp, final String childDir, final String fileName) {
        FileManager.getFileManager().saveInfoToSDCard(childDir, fileName, bmpToByteArray(bmp, false));
    }

}

