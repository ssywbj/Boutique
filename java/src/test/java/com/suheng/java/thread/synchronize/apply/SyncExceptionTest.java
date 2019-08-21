package com.suheng.java.thread.synchronize.apply;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SyncExceptionTest {

    private SyncException mSyncException;

    @Before
    public void init() {
        mSyncException = new SyncException();
        System.out.println("------------ 开始测试 ------------");
    }

    @After
    public void finish() {
        System.out.println("------------ 结束测试 ------------");
    }

    @Test
    public void listString() {
        System.out.println(mSyncException.listString());
    }

    @Test
    public void listRemove() {
        mSyncException.listRemove();
    }

    @Test
    public void iteratorRemove() {
        listString();
        mSyncException.iteratorRemove();
        listString();
    }

    @Test
    public void threadUnsafe() {
    }

    @Test
    public void threadSafe() {
        /*Object lock = new Object();
        mSyncException.threadSafe();
        try {
            synchronized (lock) {
                lock.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
}