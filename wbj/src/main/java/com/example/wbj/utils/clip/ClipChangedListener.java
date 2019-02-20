package com.example.wbj.utils.clip;

import android.content.ClipboardManager;
import android.content.Context;

public abstract class ClipChangedListener implements ClipboardManager.OnPrimaryClipChangedListener {
    private ClipManager mClipManager;
    private StringBuilder mPreviousText = new StringBuilder();

    public ClipChangedListener(Context context) {
        mClipManager = new ClipManager(context);
    }

    @Override
    public void onPrimaryClipChanged() {
        String latestText = mClipManager.getLatestText();
        if (mPreviousText.toString().equals(latestText)) {//防止一次变化却调用多次
            return;
        }
        mPreviousText.delete(0, mPreviousText.length());
        mPreviousText.append(latestText);

        this.latestTextChange(latestText);
    }

    public abstract void latestTextChange(String latestText);
}

