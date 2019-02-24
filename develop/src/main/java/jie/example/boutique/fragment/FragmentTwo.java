package jie.example.boutique.fragment;

import jie.example.boutique.HandleClientRequestActivity;
import jie.example.boutique.R;
import jie.example.utils.ToastUtil;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentTwo extends Fragment implements OnClickListener {
	private static final String TAG = HandleClientRequestActivity.TAG;
	private static final String TAG_PRIVATE = FragmentTwo.class.getSimpleName();
	private HandleClientRequestActivity mActivity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (HandleClientRequestActivity) activity;
		Log.i(TAG, TAG_PRIVATE + "::onAttach");
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
		View view = inflater.inflate(R.layout.handle_cra_f_two, container,
				false);
		view.findViewById(R.id.fw_center_btn).setOnClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i(TAG, TAG_PRIVATE + "::onActivityCreated");
		mActivity.findViewById(R.id.fw_center_btn).setOnClickListener(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, TAG_PRIVATE + "::onDestroy");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fw_center_btn:
			ToastUtil.showToast(((Button) v).getText().toString());
			break;
		default:
			break;
		}
	}

}
