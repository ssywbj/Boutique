package jie.example.boutique.fragment;

import jie.example.boutique.HandleClientRequestActivity;
import jie.example.boutique.R;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentThree extends Fragment {
	private static final String TAG = HandleClientRequestActivity.TAG;
	private static final String TAG_PRIVATE = FragmentThree.class.getSimpleName();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
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
		return inflater.inflate(R.layout.handle_cra_f_three, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.i(TAG, TAG_PRIVATE + "::onActivityCreated");
	}

}
