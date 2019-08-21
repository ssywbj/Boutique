package com.suheng.java.thread.synchronize.apply;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncList {
    private List<Download> mListDownload = new ArrayList<>();
    private Map<Long, Download> mMapItem = new HashMap<>();

    void update(Download download) {
        if (download == null) {
            return;
        }

        Long uid = download.getUid();
        if (mMapItem.containsKey(uid)) {
            Download item = mMapItem.get(uid);
            if (item != null) {
                item.update(download);
            }
        } else {
            mMapItem.put(uid, download);
            mListDownload.add(download);
        }
    }

    void update(List<Download> downloads) {
        if (downloads == null) {
            return;
        }

        for (Download download : downloads) {
            update(download);
        }
    }

    void delete(long uid) {
        if (mMapItem.containsKey(uid)) {
            Download download = mMapItem.get(uid);
            if (mListDownload.contains(download)) {
                mListDownload.remove(download);
                mMapItem.remove(uid);
            }
        }
    }

    void delete(List<Long> uids) {
        if (uids == null) {
            return;
        }

        for (Long uid : uids) {
            delete(uid);
        }
    }

    void printList() {
        if (mListDownload.size() > 0) {
            for (Download download : mListDownload) {
                System.out.println(Thread.currentThread().getName() + ", " + download);
            }
        } else {
            System.out.println("----------- list is empty ------------");
        }
    }

    void printMap() {
        if (mMapItem.size() > 0) {
            for (Map.Entry<Long, Download> downloadEntry : mMapItem.entrySet()) {
                System.out.println(downloadEntry.getKey() + "-->" + downloadEntry.getValue());

            }
        } else {
            System.out.println("----------- map is empty ------------");
        }
    }

    public static void main(String[] args) {
    }

}
