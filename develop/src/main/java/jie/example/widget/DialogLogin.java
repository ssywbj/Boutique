package jie.example.widget;

import java.util.Timer;
import java.util.TimerTask;

import jie.example.boutique.R;
import jie.example.constant.Constant;
import jie.example.manager.BoutiqueApp.AppHandler;
import jie.example.manager.BoutiqueApp.HandlerCallback;
import jie.example.utils.LogUtil;
import jie.example.utils.ScreenUtil;
import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DialogLogin extends Dialog implements View.OnClickListener,
		HandlerCallback, OnFocusChangeListener {
	private static final String TAG = Dialog.class.getSimpleName();
	private Context mContext;
	private static final int TIME_TIMEOUT = 60;
	private static final int UNHANDLE_TIME = TIME_TIMEOUT;
	private static final int WIDTH = 350;
	private static final int HEIGHT = 244;
	private AppHandler mHandler;
	private EditText mEditLoginName, mEditLoginPwd;
	private Button mBtnCancel;
	private int mTimeout = TIME_TIMEOUT;
	private int mCountTime = 0;// 计时器
	private Timer mTimer;
	private int mStatusBarHeight = 0;// 状态栏高度
	private boolean mMoveToLeft = true;
	private boolean mMoveToBottom = true;

	public DialogLogin(Context context) {
		super(context, R.style.DialogStyle);
		initDialog(context);
	}

	private void initDialog(Context context) {
		mContext = context;
		mHandler = new AppHandler(this);
		try {
			mStatusBarHeight = ScreenUtil.getStatusBarHeight();
		} catch (Exception e) {
			LogUtil.e(TAG, e.toString(), new Exception());
		}

		View dialogView = View.inflate(context, R.layout.dialog_login, null);
		setContentView(dialogView);

		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.type = WindowManager.LayoutParams.TYPE_TOAST;
		lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
		lp.x = 20;// 相对"Gravity.LEFT | Gravity.TOP"(左顶点)的偏移量
		lp.y = 20;
		lp.width = WIDTH; // 宽度
		lp.height = HEIGHT; // 高度
		lp.alpha = 0.8f; // 透明度
		dialogWindow.setAttributes(lp);

		initView(dialogView);
	}

	private void initView(View view) {
		view.findViewById(R.id.login_btn).setOnClickListener(this);
		mBtnCancel = (Button) view.findViewById(R.id.login_btn_cancel);
		mBtnCancel.setOnClickListener(this);
		setTextTimeout(mTimeout);

		TextView textName = (TextView) view
				.findViewById(R.id.login_text_password);
		textName.setText(mContext.getString(R.string.user_password, "    "));

		mEditLoginName = (EditText) view.findViewById(R.id.login_edit_name);
		mEditLoginPwd = (EditText) view.findViewById(R.id.login_edit_password);
		mEditLoginName.setOnClickListener(this);
		mEditLoginPwd.setOnClickListener(this);
		mEditLoginName.setOnFocusChangeListener(this);
		mEditLoginPwd.setOnFocusChangeListener(this);
		mEditLoginName.addTextChangedListener(mTextWatcher);
		mEditLoginPwd.addTextChangedListener(mTextWatcher);

		view.findViewById(R.id.login_layout).setOnTouchListener(
				new View.OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						restartCountTime();
						return false;
					}
				});
	}

	@Override
	public void show() {
		super.show();
		startCountTime();

		mHandler.sendEmptyMessage(AppHandler.MOVE_DIALOG);

		mEditLoginName.setText(R.string.login_defaule_name);
		mEditLoginPwd.setText(R.string.login_defaule_pwd);
	}

	/**
	 * 取消计时任务
	 */
	private void cancelCountDown() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
	}

	/**
	 * 重新计时
	 */
	private void restartCountTime() {
		cancelCountDown();
		startCountTime();
	}

	/**
	 * 开始计时
	 */
	private void startCountTime() {
		mCountTime = 0;
		mTimeout = TIME_TIMEOUT;
		mBtnCancel.setText(R.string.user_login_c);
		mHandler.removeMessages(AppHandler.TIMEOUT);

		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				mCountTime++;
				if (mCountTime == UNHANDLE_TIME) {// 未操作的时间到达上限后，发消息提示关闭界面
					mTimer.cancel();
					mTimer = null;
					mHandler.sendEmptyMessage(AppHandler.TIMEOUT);
				}
			}
		}, 1000, 1000);
	}

	public void setTextTimeout(int timeout) {
		mBtnCancel.setText(mContext.getString(R.string.login_setting, timeout));
	}

	@Override
	public void dismiss() {
		super.dismiss();
		cancelCountDown();
		mHandler.removeMessages(AppHandler.TIMEOUT);
		mHandler.removeMessages(AppHandler.MOVE_DIALOG);
	}

	@Override
	public void dispatchMessage(Message msg) {
		switch (msg.what) {
		case AppHandler.TIMEOUT:
			if (mTimeout > 0) {
				setTextTimeout(mTimeout);
				mTimeout--;
				mHandler.sendEmptyMessageDelayed(AppHandler.TIMEOUT, 1000);
			} else {
				dismiss();
			}
			break;
		case AppHandler.MOVE_DIALOG:
			Window dialogWindow = getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			if (mMoveToLeft) {// 左移
				lp.x += 2;
				if (lp.x + WIDTH >= Constant.screenWidth) {// 左移到最右侧
					mMoveToLeft = false;
				}
			} else {// 右移
				lp.x -= 2;
				if (lp.x <= 0) {// 右移到最左侧
					mMoveToLeft = true;
				}
			}

			if (mMoveToBottom) {// 下移
				lp.y += 2;
				if (lp.y + lp.height >= Constant.screenHeight) {
					mMoveToBottom = false;
				}
			} else {// 下移到底后，开始上移
				lp.y -= 2;
				if (lp.y - mStatusBarHeight <= 0) {
					mMoveToBottom = true;
				}
			}

			dialogWindow.setAttributes(lp);
			mHandler.sendEmptyMessageDelayed(AppHandler.MOVE_DIALOG, 100);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_btn:
			mEditLoginName.getText().toString();
			mEditLoginPwd.getText().toString();
			// dismiss();
			break;
		case R.id.login_btn_cancel:
			dismiss();
			break;
		case R.id.login_edit_name:
		case R.id.login_edit_password:
			restartCountTime();
			break;
		default:
			break;
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			restartCountTime();
		}
	}

	private TextWatcher mTextWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			restartCountTime();
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

}
