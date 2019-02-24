package jie.example.boutique.fragment;

import jie.example.boutique.CommonFragmentActivity;
import jie.example.boutique.R;
import jie.example.utils.LogUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BasicFragmentImpl extends BasicFragment {
	// private ViewGroup mViewLayout;
	private CommonFragmentActivity mActivity;
	private TextView mTextInfo;

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// LogUtil.i(TAG, "BasicFragmentImpl===onCreateView");
	// mViewLayout = (ViewGroup) super.onCreateView(inflater, container,
	// savedInstanceState);
	// return mViewLayout;
	// }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtil.i(TAG, "BasicFragmentImpl===onActivityCreated");
		mActivity = (CommonFragmentActivity) getActivity();
		super.setTimeout(5);
		// mTextInfo = (TextView) mViewLayout.findViewById(R.id.text_info);
		mTextInfo = (TextView) mActivity.findViewById(R.id.text_info);
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtil.i(TAG, "BasicFragmentImpl===onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		LogUtil.i(TAG, "BasicFragmentImpl===onStop");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.i(TAG, "BasicFragmentImpl===onDestroy");
	}

	@Override
	public int getLayoutId() {
		return R.layout.common_fragment_frg_impl;
	}

	@Override
	public void timeToEnd() {
		mTextInfo.setText("时间到!!");
	}

}
