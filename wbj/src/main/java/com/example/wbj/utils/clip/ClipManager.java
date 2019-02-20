package com.example.wbj.utils.clip;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

/**
 * 剪贴板管理器：如果使用，就要注意要销毁(调用destroy()方法)
 */
public class ClipManager {
    public static final String TAG = ClipManager.class.getSimpleName();
    private ClipboardManager mClipboardManager;
    private ClipChangedListener mClipChangedListener;

    public ClipManager(Context context) {
        mClipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public String getLatestText() {
        String content = "";
        try {
            if (mClipboardManager.hasPrimaryClip()) {//检查剪贴板是否有内容
                ClipData.Item itemData = mClipboardManager.getPrimaryClip().getItemAt(0);//取出剪贴板最新一条内容
                content = itemData.getText().toString().trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    public void registerListener(ClipChangedListener clipChangedListener) {
        this.unregisterListener();
        mClipChangedListener = clipChangedListener;
        mClipboardManager.addPrimaryClipChangedListener(mClipChangedListener);//监听剪切板内容变化
    }

    public void unregisterListener() {
        if (mClipChangedListener != null) {
            mClipboardManager.removePrimaryClipChangedListener(mClipChangedListener);
        }
    }

}
