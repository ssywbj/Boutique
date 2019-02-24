package jie.example.boutique.service;

import jie.example.utils.LogUtil;
import jie.example.widget.SignNamePanel;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SignPanelService extends Service implements
		SignNamePanel.SignPanelConn {
	private static final String TAG = SignPanelService.class.getSimpleName();
	private SignNamePanel mSignNamePanel;

	@Override
	public void onCreate() {
		super.onCreate();
		mSignNamePanel = new SignNamePanel(this);
		LogUtil.i(TAG, TAG + "::onCreate()");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.i(TAG, TAG + "::onDestroy()");
		if (mSignNamePanel != null) {
			mSignNamePanel.removeFromWindow();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void closeSignPanelWindow() {
		stopSelf();// 停止服务
	}

}
