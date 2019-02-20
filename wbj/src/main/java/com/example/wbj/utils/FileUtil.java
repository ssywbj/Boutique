package com.example.wbj.utils;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.FileChannel;

/**
 * Created by Wbj on 2016/3/20.
 */
public class FileUtil {

    /**
     * 从输入流中读取数据
     *
     * @param inputStream 输入流
     * @return byte（字节）数组
     * @throws Exception
     */
    public static byte[] readByte(InputStream inputStream) throws Exception {
        return readByte(inputStream, false);
    }

    /**
     * 读取文件数据
     *
     * @param file 读取的文件
     * @return byte（字节）数组
     * @throws Exception
     */
    public static byte[] readByte(File file) throws Exception {
        return readByte(new FileInputStream(file));
    }

    /**
     * 从输入流中读取数据
     *
     * @param inputStream 输入流
     * @param isReset     是否重置流：true表示重置，重置后可再次利用，但注意要在其它地方关闭流；false表示直接关闭流。
     * @return byte（字节）数组
     * @throws Exception
     */
    private static byte[] readByte(InputStream inputStream, boolean isReset) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4 * 1024];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        byte[] data = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        if (isReset) {
            inputStream.reset();
        } else {
            inputStream.close();
        }
        return data;
    }

    /**
     * 从输入流中读取内容
     *
     * @param inputStream 输入流
     * @return 文字内容
     * @throws Exception
     */
    public static String readString(InputStream inputStream) throws Exception {
        return new String(readByte(inputStream, true), getCharsetName(inputStream));
    }

    /**
     * 从文件中读取内容
     *
     * @param file 读取的文件
     * @return 文件内容
     * @throws Exception
     */
    public static String readString(File file) throws Exception {
        return new String(readByte(file), getCharsetName(file));
    }

    /**
     * 获取流的编码
     *
     * @param inputStream 输入流
     * @return 编码名称，如GBK，UTF-8，ISO-8859等
     * @throws Exception
     */
    public static String getCharsetName(InputStream inputStream) throws Exception {
        byte[] buffer = new byte[4 * 1024];
        UniversalDetector detector = new UniversalDetector(null);//需要导入“juniversalchardet-1.0.3.jar”包
        int readLen;
        while ((readLen = inputStream.read(buffer)) > 0 && !detector.isDone()) {
            detector.handleData(buffer, 0, readLen);
        }
        detector.dataEnd();
        String charset = detector.getDetectedCharset();
        detector.reset();
        inputStream.close();
        return charset;
    }

    /**
     * 获取文件编码
     *
     * @param file 需要获取编码的文件
     * @return 编码名称，如GBK，UTF-8，ISO-8859等
     * @throws Exception
     */
    public static String getCharsetName(File file) throws Exception {
        return getCharsetName(new FileInputStream(file));
    }

    /**
     * 复制文件
     *
     * @param source 被复制的文件
     * @param dest   复制到的文件
     * @throws Exception
     */
    public static void copyFile(File source, File dest) throws Exception {
        FileChannel inputChannel = new FileInputStream(source).getChannel();
        FileChannel outputChannel = new FileOutputStream(dest).getChannel();
        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        inputChannel.close();
        outputChannel.close();
    }

}

