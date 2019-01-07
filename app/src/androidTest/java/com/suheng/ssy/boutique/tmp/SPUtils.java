package com.suheng.ssy.boutique.tmp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wbj on 2019/1/7.
 */
public class SPUtils {
    public static final String SP_TEST_FILE = "sp_test_file";
    private SharedPreferences mPreferences;

    public SPUtils(Context context) {
        mPreferences = context.getSharedPreferences(SP_TEST_FILE, Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        mPreferences.edit().putString(key, value).commit();
    }

    public String get(String key) {
        return mPreferences.getString(key, "");
    }
}
