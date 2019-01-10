package com.suheng.ssy.boutique;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by wbj on 2019/1/7.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class UnitTestActivityTest {

    @Rule
    public ActivityTestRule<UnitTestActivity> mAty = new ActivityTestRule(UnitTestActivity.class);

    @Test
    public void clickBtnTest() {
        onView(withId(R.id.btn_unit_test)).perform(click())/*.check(matches(isEnabled()))*/;
        //onView(withText("After Click")).perform(longClick()).check(matches(isChecked()));
        onView(withId(R.id.text_result)).check(matches(withText("After Click")));
    }

    @Test
    public void clickTextTest() {
        onView(withId(R.id.text_result)).check(matches(withText("After Click")));
        //onView(withId(R.id.text_result)).perform(typeText("3"))/*.check(matches(withText("After Click")))*/;
    }
}
