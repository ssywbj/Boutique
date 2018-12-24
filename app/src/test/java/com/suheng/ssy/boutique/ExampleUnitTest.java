package com.suheng.ssy.boutique;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Before
    public void testBefore() {
        System.out.println("testBefore11111");
    }

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