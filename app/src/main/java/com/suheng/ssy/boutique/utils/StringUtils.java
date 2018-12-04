package com.suheng.ssy.boutique.utils;

public class StringUtils {

    public static String format(String text) {
        return text + "---";
    }

    public static boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }
}
