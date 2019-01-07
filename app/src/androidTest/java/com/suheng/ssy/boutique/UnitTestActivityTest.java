package com.suheng.ssy.boutique;

import android.support.test.espresso.Espresso;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by wbj on 2019/1/7.
 */
@RunWith(AndroidJUnit4.class)
public class UnitTestActivityTest {

    @Rule
    public ActivityTestRule mActivityTestRule = new ActivityTestRule(UnitTestActivity.class);

    @Test
    public void clickBtnTest() throws Exception {
        Espresso.onView(withId(R.id.btn_unit_test)).perform(click());
    }
}
