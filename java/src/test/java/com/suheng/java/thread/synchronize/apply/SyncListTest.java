package com.suheng.java.thread.synchronize.apply;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SyncListTest {
    private SyncList mSyncList;

    @Before
    public void setUp() {
        mSyncList = new SyncList();
        System.out.println("-------------------- 开始测试 --------------------");
    }

    @After
    public void tearDown() {
        System.out.println("-------------------- 结束测试 --------------------");
    }

    @Test
    public void update() {
        mSyncList.printList();
        System.out.println("-------------- 单个加入 --------------");
        mSyncList.update(new Download(1, 11111));
        mSyncList.update(new Download(2, 22222));
        mSyncList.printList();
        System.out.println("-------------- 加入列表 --------------");
        List<Download> downloads = new ArrayList<>();
        for (int uid = 0; uid < 4; uid++) {
            downloads.add(new Download(uid, 1000 + uid));
        }
        mSyncList.update(downloads);
        mSyncList.printList();
    }

    @Test
    public void delete() {
        this.update();
        System.out.println("-------------- Map信息 --------------");
        mSyncList.printMap();
        System.out.println("-------------- 单个删除 --------------");
        mSyncList.delete(1);
        mSyncList.printList();
        System.out.println("-------------- 列表删除 --------------");
        List<Long> uids = new ArrayList<>();
        for (long uid = 0; uid < 3; uid++) {
            uids.add(uid);
        }
        mSyncList.delete(uids);
        mSyncList.printList();
        System.out.println("-------------- Map信息 --------------");
        mSyncList.printMap();
    }
}