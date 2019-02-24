package jie.example.widget;

import java.util.List;

import jie.example.adapter.BasicAdapter;
import jie.example.boutique.R;
import jie.example.utils.StringUtil;
import android.app.Dialog;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class DialogMuti extends Dialog implements View.OnClickListener {
	private OnClickDialog mOnClickDialog;

	public DialogMuti(Context context, String title, List<String> content,
			String btnCancel) {
		super(context, R.style.DialogStyle);
		initDialog(context, title, content, btnCancel);
	}

	private void initDialog(Context context, String title,
			List<String> content, String btnCancel) {

		View dialogView = View.inflate(context, R.layout.dialog_single, null);
		setContentView(dialogView);

		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.alpha = 0.8f; // 透明度
		dialogWindow.setAttributes(lp);

		initView(context, dialogView, title, content, btnCancel);
	}

	private void initView(Context context, View view, String title,
			List<String> content, String btnCancel) {
		TextView textUndo = (TextView) view.findViewById(R.id.btn_cancel);
		textUndo.setSelected(true);
		textUndo.setOnClickListener(this);

		TextView textTitle = (TextView) view.findViewById(R.id.text_title);
		textTitle.setSelected(true);
		if (StringUtil.isNotEmpty(title)) {
			textTitle.setText(title);
		} else {
			textTitle.setText(R.string.dialog_def_title);
		}

		if (StringUtil.isNotEmpty(btnCancel)) {
			textUndo.setText(btnCancel);
		} else {
			textUndo.setText(R.string.cancel);
		}

		ListView listContent = (ListView) view.findViewById(R.id.content);
		if (content != null && content.size() > 0) {
			final ListContentAdapter adapter = new ListContentAdapter(content,
					context);
			listContent.setAdapter(adapter);
			listContent.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (mOnClickDialog != null) {
						mOnClickDialog.clickContentItem(position);
						adapter.setCheckedIndex(position);
						adapter.notifyDataSetChanged();
					}
				}
			});
		} else {

		}

		show();
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (mOnClickDialog != null) {
			switch (v.getId()) {
			case R.id.btn_cancel:
				mOnClickDialog.clickCancelBtn();
				break;
			default:
				break;
			}
		}
	}

	private final class ListContentAdapter extends
			BasicAdapter<String, ListContentAdapter.ViewHolder> {
		private SparseBooleanArray mCheckedFlag = new SparseBooleanArray();

		public ListContentAdapter(List<String> dataList, Context context) {
			super(dataList, context);
			initAdapter(dataList);
		}

		private void initAdapter(List<String> dataList) {
			mCheckedFlag.clear();
			if (dataList != null && dataList.size() > 0) {
				mCheckedFlag.put(0, true);
				for (int i = 1; i < dataList.size(); i++) {
					mCheckedFlag.put(i, false);
				}
			}
		}

		protected void setCheckedIndex(int checkedIndex) {
			boolean isChecked = mCheckedFlag.get(checkedIndex);
			mCheckedFlag.put(checkedIndex, !isChecked);
		}

		@Override
		protected int getItemViewLayoutId() {
			return R.layout.dialog_single_adt;
		}

		@Override
		protected ViewHolder loadViewHolder(View view) {
			return new ViewHolder(view);
		}

		@Override
		protected void bindView(int position, String object, ViewHolder vHolder) {
			vHolder.contentItem.setText(object);

			vHolder.rButton.setChecked(mCheckedFlag.get(position));
		}

		final class ViewHolder {
			TextView contentItem;
			RadioButton rButton;

			public ViewHolder(View view) {
				contentItem = (TextView) view
						.findViewById(R.id.text_content_item);
				contentItem.setSelected(true);
				rButton = (RadioButton) view.findViewById(R.id.dialog_rb);
			}
		}
	}

	public void setOnClickDialog(OnClickDialog onClickDialog) {
		mOnClickDialog = onClickDialog;
	}

	public interface OnClickDialog {
		public void clickCancelBtn();

		public void clickContentItem(int position);
	}

}
