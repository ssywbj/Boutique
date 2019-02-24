package jie.example.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import jie.example.boutique.R;
import jie.example.constant.Constant;

/**
 * 签名板窗口
 */
public class SignNamePanel implements View.OnClickListener {
    private WindowManager mWindiwMananger;
    private WindowManager.LayoutParams mWindowParams = new WindowManager.LayoutParams();
    private View mWindowView;
    private WriterView mWriterView;
    private SignPanelConn mSignPanelConn;

    public void setSignPanelConn(SignPanelConn signPanelConn) {
        mSignPanelConn = signPanelConn;
    }

    private float mDownX, mDownY;
    private float mMoveX, mMoveY;
    private Activity mContext;

    public SignNamePanel(Context context) {
        initWindow(context);
        mContext = (Activity) context;
    }

    private void initWindow(Context context) {
        if (context instanceof SignPanelConn) {
            mSignPanelConn = (SignPanelConn) context;
        }
        mWindiwMananger = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mWindowView = LayoutInflater.from(context).inflate(
                R.layout.view_sign_name, null);
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mWindowParams.format = PixelFormat.TRANSLUCENT;
        mWindowParams.gravity = Gravity.LEFT | Gravity.TOP;// 悬浮窗以左上角为坐标原点
        mWindowParams.x = 0;// 悬浮窗显示位置的X坐标
        mWindowParams.y = 0;// 悬浮窗显示位置的Y坐标
        mWindowParams.width = Constant.screenWidth * 3 / 5;// 悬浮窗宽度
        mWindowParams.height = Constant.screenHeight * 2 / 5;// 悬浮窗高度
        showSignWindow();
        initViews(mWindowView);
        setListeners();
    }

    private void initViews(View view) {
        mWriterView = (WriterView) view.findViewById(R.id.sp_wv);
        Button btnConfirm = (Button) view.findViewById(R.id.sp_lb_btn_confirm);
        Button btnClear = (Button) view.findViewById(R.id.sp_lb_btn_clear);
        Button btnClose = (Button) view.findViewById(R.id.sp_lb_btn_close);
        btnConfirm.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }

    private void setListeners() {
        mWindowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    mDownX = event.getRawX();
                    mDownY = event.getRawY();
                } else if (action == MotionEvent.ACTION_MOVE) {
                    mMoveX = event.getRawX();
                    mMoveY = event.getRawY();
                    refreshWindowView(
                            mWindowParams.x + (int) (mMoveX - mDownX),
                            mWindowParams.y + (int) (mMoveY - mDownY));
                    mDownX = mMoveX;
                    mDownY = mMoveY;
                }
                return true;
            }
        });
    }

    public void showSignWindow() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(mContext)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivityForResult(intent, 1);
            } else {
            }
        }


        mWindiwMananger.addView(mWindowView, mWindowParams);// 添加View并显示悬浮窗
    }

    /**
     * 消除悬浮窗
     */
    public void removeFromWindow() {
        mWindiwMananger.removeView(mWindowView);
    }

    /**
     * 刷新悬浮窗
     */
    public void refreshWindowView(int xpiovt, int ypivot) {
        mWindowParams.x = xpiovt;
        mWindowParams.y = ypivot;
        mWindiwMananger.updateViewLayout(mWindowView, mWindowParams); // 刷新显示
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sp_lb_btn_confirm:
                mWriterView.savePanelText();
                break;
            case R.id.sp_lb_btn_clear:
                mWriterView.clearPanel();
                break;
            case R.id.sp_lb_btn_close:
                mWriterView.clearPanel();
                if (mSignPanelConn != null) {
                    mSignPanelConn.closeSignPanelWindow();
                }
                break;
            default:
                break;
        }
    }

    public interface SignPanelConn {
        void closeSignPanelWindow();
    }

}
