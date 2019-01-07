package com.suheng.ssy.boutique;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.suheng.ssy.boutique.tmp.SPUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * 仪器化测试（Instrumented tests）
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private static final String TAG = ExampleInstrumentedTest.class.getSimpleName();

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.suheng.ssy.boutique", appContext.getPackageName());

        Log.d(TAG, "app package name: " + appContext.getPackageName() + ", " + appContext);
    }

    private SPUtils mSPUtils;
    private static final String PREFS_NAME_KEY = "prefs_name_key";
    private static final String PREFS_NAME_VALUE = "Weibangjie";

    @Before
    public void init() {
        mSPUtils = new SPUtils(InstrumentationRegistry.getContext());
        mSPUtils.putString(PREFS_NAME_KEY, PREFS_NAME_VALUE);
    }

    @Test
    public void testGetString() {
        Assert.assertEquals(PREFS_NAME_VALUE, mSPUtils.get(PREFS_NAME_KEY));
    }
}
