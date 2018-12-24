package com.suheng.ssy.boutique.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    /**
     * 英文全称  如：2017-11-01 22:11:00
     */
    public static String FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 输入想要转换的时间（"如2017-11-01 22:11:00"）返回时间戳
     *
     * @param time
     * @return 时间戳
     */
    public static long dateToStamp(String time) throws Exception {
        SimpleDateFormat sdr = new SimpleDateFormat(FORMAT_YMDHMS, Locale.CHINA);
        Date date = sdr.parse(time);
        return date.getTime();
    }

    /**
     * 将时间戳转换为时间
     */
    public static String stampToDate(long lt) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_YMDHMS, Locale.CHINA);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

}
