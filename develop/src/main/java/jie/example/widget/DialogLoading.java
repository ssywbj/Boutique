package jie.example.widget;

import jie.example.boutique.R;
import jie.example.manager.BoutiqueApp;
import jie.example.manager.BoutiqueApp.AppHandler;
import jie.example.manager.BoutiqueApp.HandlerCallback;
import android.app.Dialog;
import android.content.Context;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

/**
 * 自定义LoadingDialog
 */
public class DialogLoading extends Dialog implements HandlerCallback {
	/**
	 * LoadingDialog提示语
	 */
	private TextView mTextDialogNote;
	/**
	 * LoadingDialog提示语后面的点
	 */
	private TextView mTextLoadingPoint;
	/**
	 * 用于连接“ .”或删除“ .”
	 */
	private StringBuilder mPointBuilder = new StringBuilder();
	/**
	 * 用于记录点的个数
	 */
	private int mTextPointCount = 0;
	/**
	 * 点个数的增减状态
	 */
	private boolean mIsAdd = true;
	/**
	 * 空格加点
	 */
	public static final String TEXT_POINT = " .";
	/**
	 * 点的最大个数
	 */
	public static final int MAX_TEXT_POINT_NUM = 3;
	/**
	 * 用于在主界面更新点的个数
	 */
	private AppHandler mHandler;

	public DialogLoading(Context context) {
		super(context, R.style.DialogStyle);
		initDialog();
	}

	private void initDialog() {
		View dialogView = View.inflate(getContext(), R.layout.dialog_loading,
				null);
		setContentView(dialogView);
		mTextLoadingPoint = (TextView) findViewById(R.id.dialog_text_point);
		mTextDialogNote = (TextView) findViewById(R.id.dialog_content_note);
	}

	@Override
	public void show() {
		super.show();
		mHandler = new BoutiqueApp.AppHandler(this);
		mHandler.sendEmptyMessage(AppHandler.MSG100);
	}

	@Override
	public void dismiss() {
		super.dismiss();
		mPointBuilder.delete(0, mPointBuilder.length());
		mTextPointCount = 0;
		if (mHandler != null) {
			mHandler.removeMessages(AppHandler.MSG100);// 移除消息，以免Handler在后台发送消息。
			mHandler = null;
		}
	}

	/**
	 * 设置LoadingDialog的提示语，但要注意提示语的字数尽量不要过多
	 */
	public void setDialogNoteContent(String noteContent) {
		if (mTextDialogNote == null) {
			mTextDialogNote = (TextView) findViewById(R.id.dialog_content_note);
		}
		mTextDialogNote.setText(noteContent.trim());
	}

	public void setNoteText(int resId) {
		String noteContent = getContext().getString(resId);
		setDialogNoteContent(noteContent);
	}

	@Override
	public void dispatchMessage(Message msg) {
		switch (msg.what) {
		case AppHandler.MSG100:
			if (mIsAdd) {// 如果是添加状态，则继续添加“ .”
				mTextPointCount++;
				mPointBuilder.append(TEXT_POINT);
				if (mTextPointCount == MAX_TEXT_POINT_NUM) {
					mIsAdd = false;// “ .”的个数到达最大值后，改变成减少状态
				}
			} else {
				mTextPointCount--;
				mPointBuilder.delete(2 * mTextPointCount - 1,
						2 * mTextPointCount + 1);
				if (mTextPointCount == 1) {
					mIsAdd = true;
				}
			}
			mTextLoadingPoint.setText(mPointBuilder.toString().trim());
			mHandler.sendEmptyMessageDelayed(AppHandler.MSG100, 300);// 每300毫秒发送一次消息更新界面
			break;
		default:
			break;
		}
	}

}
