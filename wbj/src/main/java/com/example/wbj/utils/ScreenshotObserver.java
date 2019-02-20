package com.example.wbj.utils;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;

import java.util.Date;

public class ScreenshotObserver extends ContentObserver {
    private static final String[] KEYWORDS = {"screenshot", "screenshots", "screen_shot", "screen-shot",
            "screen shot", "screencapture", "screen_capture", "screen-capture", "screen capture",
            "screencap", "screen_cap", "screen-cap", "screen cap"};
    private static final Uri EXTERNAL_CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;//查询字段
    private static final String PROJECTION[] = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED};//查询字段
    private static final String SORT_ORDER = MediaStore.Images.Media.DATE_ADDED + " desc";//按添加的时间降序排列
    //private static final String SORT_ORDER = MediaStore.Images.Media.DATE_MODIFIED + " desc limit 100";//按修改的时间降序排列，拉取100条记录
    private Context mContext;
    private StringBuilder mPathBuilder = new StringBuilder();
    private ChangeListener mChangeListener;

    public ScreenshotObserver(Context context) {
        super(new Handler(context.getMainLooper()));
        mContext = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        this.queryChange();
    }

    private void queryChange() {
        try {
            Cursor cursor = mContext.getContentResolver().query(EXTERNAL_CONTENT_URI, PROJECTION, null, null, SORT_ORDER);
            if (cursor.moveToFirst()) {
                do {
                    String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                    long addTime = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED));
                    for (String keyword : KEYWORDS) {
                        long timeDifferent = new Date().getTime() - addTime * 1000;//addTime精确到秒，getTime()精确到毫秒
                        //如果路径中包括截屏关键字且与现在的时间差小于5秒，则认为刚刚生成的图片是截屏图片
                        if (imagePath.toLowerCase().contains(keyword) && timeDifferent > 0 && timeDifferent < 5 * 1000L) {
                            if (mPathBuilder.toString().equals(imagePath)) {//防止一次截屏却多次调用
                                return;
                            }
                            mPathBuilder.delete(0, mPathBuilder.length());
                            mPathBuilder.append(imagePath);
                            if (mChangeListener != null) {
                                mChangeListener.notifyChange(imagePath);
                            }
                            return;
                        }
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerObserver() {
        mContext.getContentResolver().registerContentObserver(EXTERNAL_CONTENT_URI, false, this);
    }

    public void unregisterObserver() {
        mContext.getContentResolver().unregisterContentObserver(this);
    }

    public void setChangeListener(ChangeListener changeListener) {
        mChangeListener = changeListener;
    }

    public interface ChangeListener {
        void notifyChange(String imagePath);
    }

}

