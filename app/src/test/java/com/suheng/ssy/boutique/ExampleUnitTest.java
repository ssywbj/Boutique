package com.suheng.ssy.boutique;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * 本地测试（Local tests）
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    //在测试之前执行，用于准备测试环境(如: 初始化类，读输入流等)，在一个测试类中，每个@Test方法的执行都会触发一次调用。
    @Before
    public void testBefore() {
        System.out.println("testBefore11111");
    }

    //在测试之后执行，用于清理测试环境数据，在一个测试类中，每个@Test方法的执行都会触发一次调用。
    @Before
    public void testBefore2() {
        System.out.println("testBefore22222");
    }

    @After
    public void testAfter2() {
        System.out.println("testAfter22222");
    }

    @After
    public void testAfter() {
        System.out.println("testAfter11111");
    }

    @After
    public void testAfter3() {
        System.out.println("aaaaaaaaaaaaaaaa");
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(5, 2 + 2);
    }

    @Test
    public void addition_isCorrect2() {
        assertEquals(6, 2 + 2);
    }
}