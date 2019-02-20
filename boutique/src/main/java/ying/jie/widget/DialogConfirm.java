package ying.jie.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import ying.jie.boutique.R;

public class DialogConfirm extends Dialog implements View.OnClickListener {
	private OnClickDialog mOnClickDialog;

	public DialogConfirm(Context context, String title, String content,
			String btnOk, String btnCancel) {
		super(context, R.style.DialogStyle);
		initDialog(context, title, content, btnOk, btnCancel);
	}

	private void initDialog(Context context, String title, String content,
			String btnOk, String btnCancel) {

		View dialogView = View.inflate(context, R.layout.dialog_confirm, null);
		setContentView(dialogView);

		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.alpha = 0.8f; // 透明度
		dialogWindow.setAttributes(lp);

		initView(dialogView, title, content, btnOk, btnCancel);
	}

	private void initView(View view, String title, String content,
			String btnOk, String btnCancel) {
		TextView textOk = (TextView) view.findViewById(R.id.btn_ok);
		TextView textUndo = (TextView) view.findViewById(R.id.btn_cancel);
		textOk.setSelected(true);
		textUndo.setSelected(true);
		textOk.setOnClickListener(this);
		textUndo.setOnClickListener(this);

		TextView textTitle = (TextView) view.findViewById(R.id.text_title);
		textTitle.setSelected(true);
		if (!TextUtils.isEmpty(title)) {
			textTitle.setText(title);
		} else {
			textTitle.setText(R.string.dialog_def_title);
		}

		TextView textContent = (TextView) view.findViewById(R.id.content);
		if (!TextUtils.isEmpty(content)) {
			textContent.setText(content);
		} else {
			textContent.setText(R.string.dialog_def_content);
		}

		if (!TextUtils.isEmpty(btnOk)) {
			textOk.setText(btnOk);
		} else {
			textOk.setText(R.string.confirm);
		}

		if (!TextUtils.isEmpty(btnCancel)) {
			textUndo.setText(btnCancel);
		} else {
			textUndo.setText(R.string.cancel);
		}

		show();
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (mOnClickDialog != null) {
			int i = v.getId();
			if (i == R.id.btn_ok) {
				mOnClickDialog.clickConfirmBtn();
			} else if (i == R.id.btn_cancel) {
				mOnClickDialog.clickCancelBtn();
			}
		}
	}

	public void setOnClickDialog(OnClickDialog onClickDialog) {
		mOnClickDialog = onClickDialog;
	}

	public interface OnClickDialog {
		public void clickConfirmBtn();

		public void clickCancelBtn();
	}

}
