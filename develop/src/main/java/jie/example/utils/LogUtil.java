package jie.example.utils;

import jie.example.constant.Constant;
import android.util.Log;

public class LogUtil {

	private static final int VERBOSE = 1;
	private static final int DEBUG = 2;
	private static final int INFO = 3;
	private static final int WARN = 4;
	private static final int ERROR = 5;
	public static final int NOTHING = 6;
	private static final int LEVEL = VERBOSE;

	public static void v(String tag, String msg) {
		if (LEVEL <= VERBOSE) {
			Log.v(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (LEVEL <= DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (LEVEL <= INFO) {
			Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (LEVEL <= WARN) {
			Log.w(tag, msg);
		}
	}

	public static void e(String tag, String msg, Exception exception) {
		if (LEVEL <= ERROR) {
			Log.e(tag, msg, exception);
		}
	}

	public static void e(String tag, String msg) {
		if (LEVEL <= ERROR) {
			Log.e(tag, msg);
		}
	}

	public static void globalInfoLog(String msg) {
		i(Constant.TAG_GLOBAL, msg);
	}

	public static void globalErrorLog(String msg) {
		e(Constant.TAG_GLOBAL, msg);
	}
}
