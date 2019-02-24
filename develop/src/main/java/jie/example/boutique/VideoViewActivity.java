package jie.example.boutique;

import java.io.File;

import jie.example.utils.LogUtil;
import jie.example.utils.ToastUtil;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.VideoView;

public class VideoViewActivity extends Activity {
	private static final String TAG = VideoViewActivity.class.getSimpleName();
	private ScrollView mBottomLayout = null;
	private VideoView mVideoView = null;
	private MediaController mMediaController;
	private boolean mMeasureVideoViewHeight = true;
	private int mVideoViewHeight = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videoview_aty);
		LogUtil.i(TAG, "onCreate(Bundle)");
		mBottomLayout = (ScrollView) findViewById(R.id.bottom_layout);
		mVideoView = (VideoView) findViewById(R.id.video_view);
		mMediaController = new MediaController(this);

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			File file = new File(Environment.getExternalStorageDirectory(),
					"jiang_nan_style.mp4");
			if (file.exists()) {
				mVideoView.setVideoPath(file.getAbsolutePath());
				mVideoView.setMediaController(mMediaController);
				mMediaController.setMediaPlayer(mVideoView);
				mVideoView.requestFocus();
				mVideoView.start();
			} else {
				ToastUtil.showToast(R.string.upload_file_unexist);
			}
		}
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		LogUtil.i(TAG, "onRestart()");
	}

	@Override
	protected void onPause() {
		super.onPause();
		LogUtil.i(TAG, "onPause()");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogUtil.i(TAG, "onDestroy()");
	}

	// android:configChanges="orientation|screenSize"
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		LogUtil.i(TAG, "onConfigurationChanged(Configuration)");
		setVideoViewPosition();
	}

	private void setVideoViewPosition() {
		if (mMeasureVideoViewHeight) {
			mVideoViewHeight = mVideoView.getHeight();
			mMeasureVideoViewHeight = false;
			LogUtil.i(TAG, "mVideoViewHeight = " + mVideoViewHeight);
		}

		if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			mBottomLayout.setVisibility(View.VISIBLE);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, mVideoViewHeight);
			mVideoView.setLayoutParams(params);
		} else {// 横屏
			mBottomLayout.setVisibility(View.GONE);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			mVideoView.setLayoutParams(params);
		}
	}
}
