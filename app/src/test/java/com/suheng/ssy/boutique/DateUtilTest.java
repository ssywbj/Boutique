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
 */
@RunWith(Parameterized.class)
public class DateUtilTest {

    private String time = "2017-10-15 16:00:02";
    private long timeStamp = 1508054402000L;
    private Date mDate;

    public DateUtilTest(String time) {
        this.time = time;
    }

    @Before
    public void init() throws Exception {
        System.out.println("测试开始！");
        mDate = new Date();
        mDate.setTime(timeStamp);
    }

    @After
    public void destroy() throws Exception {
        System.out.println("测试结束！");
    }

    @Test
    public void testDateToStamp() throws Exception {
        //System.out.println(DateUtil.stampToDate(timeStamp));
        //assertEquals("dfafa", DateUtil.stampToDate(timeStamp));
        assertEquals("2017-10-15 16:00:02", DateUtil.stampToDate(timeStamp));
    }

    @Test
    public void testDateToStamp2() throws Exception {
        assertNotEquals("dfafa", DateUtil.stampToDate(timeStamp));
    }

    @Test
    public void dateToStamp() throws Exception {
        //assertEquals(1670793, DateUtil.dateToStamp(time));
        assertEquals(1508035954000L, DateUtil.dateToStamp(time));
    }

    @Test(expected = ParseException.class)
    public void dateToStamp2() throws Exception {
        //assertEquals(1670793, DateUtil.dateToStamp(time));
        assertEquals(1508054402000L, DateUtil.dateToStamp("2017-10-15"));
    }

    @Test(timeout = 100)//性能测试，如果方法耗时超过100毫秒->失败
    public void testTimeout() throws Exception {
        try {
            Thread.sleep(99);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testTimeout2() throws Exception {
        try {
            Thread.sleep(99);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Parameterized.Parameters
    public static Collection primeNumbers() {
        return Arrays.asList(new String[]{"2017-10-15", "2017-10-15 10:12"
                , "2017-10-15 10:72", "2017-10-15 10:52:34"});
    }

}
