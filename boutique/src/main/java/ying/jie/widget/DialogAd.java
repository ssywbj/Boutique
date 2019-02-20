package ying.jie.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import ying.jie.boutique.App;
import ying.jie.boutique.App.AppHandler;
import ying.jie.boutique.R;
import ying.jie.util.LogUtil;
import ying.jie.util.VoicePlayer;

/**
 * 广告Dialog
 */
public class DialogAd extends Dialog implements
        App.HandlerCallback {
    private static final String TAG = DialogAd.class.getSimpleName();
    private static final int MSG100 = 0x01;
    private static final int WIDTH = 160;
    private static final int HEIGHT = WIDTH;
    private static final int ADD = 2;
    private AppHandler mHandler = new AppHandler(this);
    private int mStatusBarHeight = 0;// 状态栏高度
    private boolean mMoveToLeft = true;
    private boolean mMoveToBottom = true;
    private int viewWidth = 100, viewHeight = viewWidth;

    public DialogAd(Context context) {
        super(context, R.style.DialogStyle);
        initDialog(context);
    }

    private void initDialog(Context context) {
        try {
            mStatusBarHeight = App.getStatusBarHeight(context);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }

        final View dialogView = View.inflate(context, R.layout.dialog_ad, null);
        setContentView(dialogView);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //lp.type = WindowManager.LayoutParams.TYPE_TOAST;//可能会带来悬浮窗权限问题
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);

        dialogView.post(new Runnable() {
            @Override
            public void run() {
                viewWidth = dialogView.getWidth();
                viewHeight = dialogView.getHeight();
            }
        });
        lp.x = 0;
        lp.y = 0;
        lp.width = WIDTH;
        lp.height = HEIGHT;
        lp.alpha = 0.8f; // 透明度
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void show() {
        super.show();
        mHandler.removeMessages(MSG100);
        mHandler.sendEmptyMessage(MSG100);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mHandler.removeMessages(MSG100);
    }

    @Override
    public void dispatchMessage(Message msg) {
        switch (msg.what) {
            case MSG100:
                Window dialogWindow = getWindow();
                WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                if (mMoveToLeft) {// 左移
                    lp.x += ADD;
                    if (lp.x + WIDTH >= App.screenWidth) {// 左移到最右侧
                        playVoice();
                        mMoveToLeft = false;
                    }
                } else {// 右移
                    lp.x -= ADD;
                    if (lp.x <= 0) {// 右移到最左侧
                        playVoice();
                        mMoveToLeft = true;
                    }
                }

                if (mMoveToBottom) {// 下移
                    lp.y += ADD;
                    if (lp.y + lp.height + mStatusBarHeight >= App.screenHeight) {
                        playVoice();
                        mMoveToBottom = false;
                    }
                } else {// 下移到底后，开始上移
                    lp.y -= ADD;
                    if (lp.y <= 0) {
                        playVoice();
                        mMoveToBottom = true;
                    }
                }

                dialogWindow.setAttributes(lp);
                mHandler.sendEmptyMessageDelayed(MSG100, 100);
                break;
            default:
                break;
        }
    }

    private void playVoice() {
        VoicePlayer.playVoice("fu.mp3");
    }

}

