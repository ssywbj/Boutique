package com.wbj.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.wbj.ClipActivity;
import com.example.wbj.R;

public class ClipWindow implements View.OnClickListener, View.OnTouchListener {
    public static final String DATA_CLIP_CONTENT = "data_clip_content";
    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;
    private View mWindowView;
    private TextView mTextClip;
    private float mDownX, mDownY;
    private float mMoveX, mMoveY;
    private boolean mIsShowing;

    public ClipWindow(Context context) {
        mContext = context;
        this.initWindow();
    }

    private void initWindow() {
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;//失去焦点，以免屏幕的点击、触摸等事件直接作用在WindowManager的视图上
        mWindowParams.format = PixelFormat.TRANSLUCENT;
        mWindowParams.type = WindowManager.LayoutParams.TYPE_TOAST;//此类型可省去权限请求：https://www.liaohuqiu.net/cn/posts/android-windows-manager/
        mWindowParams.gravity = Gravity.LEFT | Gravity.TOP;// 悬浮窗以左上角为坐标原点
        //mWindowParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;//需要设置宽高，免得会全屏展示
        mWindowParams.width = 860;

        this.initView();
    }

    private void initView() {
        mWindowView = LayoutInflater.from(mContext).inflate(R.layout.clip_window, null);
        mTextClip = (TextView) mWindowView.findViewById(R.id.text_clip);
        mTextClip.setMovementMethod(ScrollingMovementMethod.getInstance());

        mWindowView.findViewById(R.id.btn_left).setOnClickListener(this);
        mWindowView.findViewById(R.id.btn_right).setOnClickListener(this);

        mWindowView.setOnTouchListener(this);
    }

    public void show() {
        if (!mIsShowing) {
            mWindowManager.addView(mWindowView, mWindowParams);// 添加View并显示悬浮窗
        }
        mIsShowing = true;
    }

    /**
     * 移除悬浮窗
     */
    public void dismiss() {
        if (mIsShowing) {
            mWindowManager.removeView(mWindowView);
        }
        mIsShowing = false;
    }

    public void setClipText(String text) {
        mTextClip.setText(text);
    }

    /**
     * 刷新悬浮窗的位置
     */
    private void refreshLocation(int xPivot, int yPivot) {
        mWindowParams.x = xPivot;
        mWindowParams.y = yPivot;
        mWindowManager.updateViewLayout(mWindowView, mWindowParams); // 刷新显示
    }

    @Override
    public void onClick(View view) {
        this.dismiss();
        switch (view.getId()) {
            case R.id.btn_right:
                Intent intent = new Intent(mContext, ClipActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//在非Activity中打开另一个Activity需要这标志
                intent.putExtra(DATA_CLIP_CONTENT, mTextClip.getText().toString());
                mContext.startActivity(intent);//Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mDownX = event.getRawX();
            mDownY = event.getRawY();
        } else if (action == MotionEvent.ACTION_MOVE) {
            mMoveX = event.getRawX();
            mMoveY = event.getRawY();
            this.refreshLocation(mWindowParams.x + ((int) (mMoveX - mDownX)),
                    mWindowParams.y + ((int) (mMoveY - mDownY)));

            mDownX = mMoveX;
            mDownY = mMoveY;
        }
        return true;
    }

}
