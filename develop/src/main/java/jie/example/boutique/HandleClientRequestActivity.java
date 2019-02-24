package jie.example.boutique;

import jie.example.boutique.fragment.FragmentFour;
import jie.example.boutique.fragment.FragmentOne;
import jie.example.boutique.fragment.FragmentThree;
import jie.example.boutique.fragment.FragmentTwo;
import jie.example.net.ConnectServer;
import jie.example.utils.ToastUtil;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 根据用户指令处理客户请求
 */
public class HandleClientRequestActivity extends BasicActivity {
	public static final String TAG = HandleClientRequestActivity.class
			.getSimpleName();
	public static final int MSG_ONE = 101;
	private ConnectServer mConnectServer;
	private FragmentManager mFragmentManager;
	private FragmentOne mFragmentOne;
	private FragmentTwo mFragmentTwo;
	private FragmentThree mFragmentThree;
	private FragmentFour mFragmentFour;
	private Fragment mCurrentFragment;// 当前显示的Fragment

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setActionBarTitle(R.string.handle_client_request);
		setContentView(R.layout.handle_client_request_aty);
		initData();
		loadingData();
	}

	@Override
	public void initData() {
		mFragmentManager = getFragmentManager();
		mFragmentOne = (FragmentOne) mFragmentManager
				.findFragmentById(R.id.hcra_f_one);
		mFragmentTwo = (FragmentTwo) mFragmentManager
				.findFragmentById(R.id.hcra_f_two);
		mFragmentThree = (FragmentThree) mFragmentManager
				.findFragmentById(R.id.hcra_f_three);
		mFragmentFour = (FragmentFour) mFragmentManager
				.findFragmentById(R.id.hcra_f_four);
	}

	@Override
	public void loadingData() {
		mFragmentManager.beginTransaction().hide(mFragmentOne)
				.hide(mFragmentTwo).hide(mFragmentThree).hide(mFragmentFour)
				.commit();

		mConnectServer = new ConnectServer(mHandler);
		new Thread(mConnectServer).start();// 开启服务线程
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mConnectServer.closeServer();
		mFragmentOne.closeSignPanelWindow();
		mHandler = null;
	}

	private void showFragment(Fragment fragment) {
		if (fragment == null) {
			return;
		}
		if (mCurrentFragment == null) {
			mFragmentManager.beginTransaction().show(fragment).commit();
		} else {
			mFragmentManager.beginTransaction().show(fragment)
					.hide(mCurrentFragment).commit();
		}
		if (mFragmentOne == fragment) {
			mFragmentOne.showSignPanel();
		} else {
			mFragmentOne.closeSignPanelWindow();
		}
		mCurrentFragment = fragment;
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			Fragment currentFragment = null;
			switch (msg.what) {
			// 根据指令显示对应的Fragment
			case MSG_ONE:
				showFragment(mFragmentOne);// 第一种显示方式
				currentFragment = new FragmentOne();
				break;
			case MSG_ONE + 1:
				showFragment(mFragmentTwo);
				currentFragment = new FragmentTwo();
				break;
			case MSG_ONE + 2:
				showFragment(mFragmentThree);
				currentFragment = new FragmentThree();
				break;
			case MSG_ONE + 3:
				showFragment(mFragmentFour);
				currentFragment = new FragmentFour();
				break;
			default:
				ToastUtil.showToast(R.string.command_invalid);
				mConnectServer.setTempCount(0);
				break;
			}

			if (currentFragment != null) {
				mFragmentManager.beginTransaction()
						.replace(R.id.hcra_fl_fragment_layout, currentFragment)
						.commit();// 第二种显示方式。使用这种方法时，注意初始化Fragment中的View时和第一种不什么不同。
				currentFragment = null;
			}
		}
	};

}
