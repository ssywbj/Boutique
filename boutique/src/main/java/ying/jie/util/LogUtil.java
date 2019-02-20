package ying.jie.util;

import android.util.Log;

import ying.jie.boutique.App;

public class LogUtil {
    private static final int LEVEL = Log.VERBOSE;

    public static void d(String tag, String msg) {
        if (LEVEL <= Log.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (LEVEL <= Log.INFO) {
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LEVEL <= Log.ERROR) {
//            Log.e(tag, msg, new Exception());
            Log.e(tag, msg);
        }
    }

    public static void globalLogInfo(String msg) {
        if (LEVEL <= Log.INFO) {
            Log.i(App.TAG, msg);
        }
    }

    public static void globalLogError(String msg) {
        if (LEVEL <= Log.ERROR) {
            Log.e(App.TAG, msg, new Exception());
        }
    }

}
