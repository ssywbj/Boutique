package com.example.wbj;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.aaron.library.MLog;

import org.junit.Test;
import org.junit.runner.RunWith;

import ying.jie.util.VoiceToText;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    public static final String TAG = ExampleInstrumentedTest.class.getName();

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.wbj", appContext.getPackageName());
    }

    @Test
    public void testBankNumber() throws Exception {
        String voiceToText = VoiceToText.voiceToText("124");
        assertEquals("一百二十四元", voiceToText);

        boolean result = "18210741899".matches("\\d{11}");
        MLog.i(TAG, "#####:" + result);

        assertEquals(result, true);
    }
}
