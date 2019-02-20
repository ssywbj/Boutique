package ying.jie.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Wbj on 2016/3/20.
 */
public class FileUtil {

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

}

