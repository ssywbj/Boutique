package com.example.wbj.basic;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.wbj.R;
import com.example.wbj.manager.DialogQueueManager;

public abstract class BasicDialog extends Dialog implements Comparable {

    private int mPriority = DialogQueueManager.PRIORITY_NO;
    private DialogQueueManager.DialogDismissLister mDismissListener;

    public BasicDialog(Context context) {
        this(context, R.style.BasicDialog);
    }

    public BasicDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.setParams();
    }

    private void setParams() {
        setContentView(this.getLayoutId());
        setCancelable(false);//按返回键不消失，默认为true(消失)
        //setCanceledOnTouchOutside(false);

        this.init();
    }

    @Override
    public int compareTo(@NonNull Object object) {
        BasicDialog dialog = (BasicDialog) object;
        int result = getPriority() < dialog.getPriority() ? 1 : (getPriority() == dialog.getPriority() ? 0 : -1);
        return result;
    }

    @Override
    public void show() {
        super.show();
        if (getPriority() < DialogQueueManager.PRIORITY_NO) {
            if (mDismissListener == null) {
                mDismissListener = new DialogQueueManager.DialogDismissLister(this);
            }
            setOnDismissListener(mDismissListener);//在show方法里或show方法执行后设置Dialog的消失监听才有效
        }
    }

    public void setOutsideDismissListener(DialogQueueManager.DialogDismissLister outsideDismissListener) {
        mDismissListener = outsideDismissListener;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    protected abstract int getLayoutId();

    protected abstract void init();
}
