package jie.example.boutique.fragment;

import jie.example.boutique.HandleClientRequestActivity;
import jie.example.boutique.R;
import jie.example.utils.ToastUtil;
import jie.example.widget.SignNamePanel;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentOne extends Fragment implements OnClickListener,
		SignNamePanel.SignPanelConn {
	private static final String TAG = HandleClientRequestActivity.TAG;
	private static final String TAG_PRIVATE = FragmentOne.class.getSimpleName();
	private HandleClientRequestActivity mActivity;
	private SignNamePanel mSignNamePanel;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.i(TAG, TAG_PRIVATE + "::onAttach");
		mActivity = (HandleClientRequestActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, TAG_PRIVATE + "::onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, TAG_PRIVATE + "::onCreateView");
		return inflater.inflate(R.layout.handle_cra_f_one, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i(TAG, TAG_PRIVATE + "::onActivityCreated");
		mActivity.findViewById(R.id.fo_btn_center).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fo_btn_center:
			ToastUtil.showToast(((Button) v).getText().toString());
			break;
		default:
			break;
		}
	}

	/**
	 * 显示签字板
	 */
	public void showSignPanel() {
		mSignNamePanel = new SignNamePanel(mActivity);
		mSignNamePanel.setSignPanelConn(this);
	}

	/**
	 * 关闭签字板
	 */
	@Override
	public void closeSignPanelWindow() {
		if (mSignNamePanel != null) {
			mSignNamePanel.removeFromWindow();
			mSignNamePanel = null;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, TAG_PRIVATE + "::onDestroy");
	}

}
