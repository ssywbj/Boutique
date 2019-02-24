package jie.example.boutique.fragment;

import jie.example.boutique.R;
import jie.example.manager.BoutiqueApp.AppHandler;
import jie.example.manager.BoutiqueApp.HandlerCallback;
import jie.example.utils.LogUtil;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class BasicFragment extends Fragment implements HandlerCallback {
	public static final String TAG = BasicFragment.class.getSimpleName();
	public static final int MSG_TIMEOUT = 0x100;
	public static final int TIMEOUT_DELAY_MILLIS = 1000;
	private AppHandler mHandler = new AppHandler(this);
	private LinearLayout mParentLayout;
	private TextView mTextTimeout;
	private int mTimeout = 10;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtil.i(TAG, this + "-->onCreateView");
		mParentLayout = (LinearLayout) inflater.inflate(
				R.layout.common_fragment_frg, container, false);
		mParentLayout.addView(inflater.inflate(this.getLayoutId(), null),
				new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT));
		return mParentLayout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtil.i(TAG, this + "-->onActivityCreated");
		mTextTimeout = (TextView) mParentLayout
				.findViewById(R.id.basic_frg_text_timeout);
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtil.i(TAG, this + "-->onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		LogUtil.i(TAG, this + "-->onStop");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mHandler.removeMessages(MSG_TIMEOUT);
		LogUtil.i(TAG, this + "-->onDestroy");
	}

	protected void setTimeout(int timeout) {
		this.mTimeout = timeout;
		mTextTimeout.setText(this.mTimeout + "");
		mHandler.sendEmptyMessageDelayed(MSG_TIMEOUT, TIMEOUT_DELAY_MILLIS);
	}

	@Override
	public void dispatchMessage(Message msg) {
		switch (msg.what) {
		case MSG_TIMEOUT:
			--this.mTimeout;
			mTextTimeout.setText(this.mTimeout + "");
			if (this.mTimeout > 0) {
				mHandler.sendEmptyMessageDelayed(MSG_TIMEOUT,
						TIMEOUT_DELAY_MILLIS);
			} else {
				mHandler.removeMessages(MSG_TIMEOUT);
				this.timeToEnd();
			}
			break;
		default:
			break;
		}
	}

	public abstract int getLayoutId();

	public abstract void timeToEnd();
}
