package com.suheng.ssy.boutique;

import com.suheng.ssy.boutique.utils.DateUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by wbj on 2018/12/24.
 * 手动创建测试类：选择对应的类->光标停留在类名上->按下ALT + ENTER->在弹出的弹窗中选择Create Test
 */
@RunWith(Parameterized.class)
public class DateUtilTest {

    private String mTimePlain;
    private long mTimeStamp = 1508054402000L;

    @Parameterized.Parameters
    public static Collection primeNumbers() {
        return Arrays.asList("2017-10-15", "2017-10-15 10:12", "2017-10-15 10:72", "2017-10-15 10:52:34");
    }

    public DateUtilTest(String timePlain) {
        this.mTimePlain = timePlain;
    }

    @Before
    public void init() {
        System.out.println("测试开始！");
        Date date = new Date();
        date.setTime(mTimeStamp);
    }

    @After
    public void destroy() {
        System.out.println("测试结束！");
    }

    @Test
    public void testDateToStamp() {
        //System.out.println(DateUtil.stampToDate(mTimeStamp));
        //assertEquals("dfafa", DateUtil.stampToDate(mTimeStamp));
        assertEquals("2017-10-15 16:00:02", DateUtil.stampToDate(mTimeStamp));
    }

    @Test
    public void testDateToStamp2() {
        assertNotEquals("dfafa", DateUtil.stampToDate(mTimeStamp));
    }

    @Test
    public void dateToStamp() throws Exception {
        //assertEquals(1670793, DateUtil.dateToStamp(mTimePlain));
        assertEquals(1508035954000L, DateUtil.dateToStamp(mTimePlain));
    }

    @Test(expected = ParseException.class)
    public void dateToStamp2() throws Exception {
        //assertEquals(1670793, DateUtil.dateToStamp(mTimePlain));
        assertEquals(1508054402000L, DateUtil.dateToStamp("2017-10-15"));
    }

    @Test(timeout = 100)//性能测试，如果方法耗时超过100毫秒->失败
    public void testTimeout() {
        try {
            Thread.sleep(990);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
