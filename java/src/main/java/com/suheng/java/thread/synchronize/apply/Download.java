package com.suheng.java.thread.synchronize.apply;

public class Download {
    private long uid;
    private long progress;

    Download(long uid, long progress) {
        this.uid = uid;
        this.progress = progress;
    }

    long getUid() {
        return uid;
    }

    private void setProgress(long progress) {
        this.progress = progress;
    }

    void update(Download download) {
        this.setProgress(download.progress);
    }

    @Override
    public String toString() {
        return "Download{" + "uid=" + uid + ", progress=" + progress + '}';
    }
}
