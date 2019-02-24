package jie.example.boutique;

import java.util.ArrayList;
import java.util.List;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import jie.example.constant.Constant;
import jie.example.utils.LogUtil;
import jie.example.utils.ToastUtil;
import jie.example.utils.Utils;
import jie.example.widget.WaterWaveViewLayout;
import jie.example.widget.WaterWaveViewLayout.OnPunshCardListener;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

/**
 * 精品首页
 */
public class BoutiqueActivity extends BasicActivity {
	private static final String TAG = BoutiqueActivity.class.getSimpleName();
	private static final int TEST_REQUEST_CODE = 101;
	public static final String VOLUME_FILE = "VolumeFile";
	public static final String VOLUME_MUSIC = "volume_music";
	private BoutiqueActivity mActivity;
	private TextSwitcher mTextSwitcherOval;
	private TextSwitcher mTextSwitcherCircle;
	private TextSwitcher mTextSwitcherRectangle;
	private ImageButton mIBtnGIF;
	private AnimationDrawable mGIFAnim;// 用于播放GIF动画
	private WaterWaveViewLayout punchView;
	private String[] mTextLong = { "111111", "222222", "333333", "444444" };
	private String[] mTextShort = { "1", "2", "3", "4" };
	private int mTextOvalId = 0;
	private int mPressNumberCount = 1;
	private boolean mPlayGIF = true;
	private MediaPlayer mMediaPlayer;
	private Dialog mVoiceDialog;// 用于调节音量的Dialog
	private int mVolume = 0;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setActionBarTitle(R.string.aty_boutique);
		super.setLeftBtnImage(R.drawable.actionbar_left_ibtn_menu);
		setContentView(R.layout.boutique_aty);
		LogUtil.globalInfoLog("BoutiqueActivity onCreate");

		initData();
		loadingData();

		if (savedInstanceState != null) {
			LogUtil.globalInfoLog(savedInstanceState
					.getString(Constant.SAVE_INSTANCE_STATE));
		} else {
			LogUtil.globalInfoLog("(BoutiqueActivity) savedInstanceState is null");
		}
	}

	@Override
	public void initData() {
		mActivity = BoutiqueActivity.this;
		mTextSwitcherOval = (TextSwitcher) findViewById(R.id.text_switcher_oval);
		mTextSwitcherCircle = (TextSwitcher) findViewById(R.id.text_switcher_circle);
		mTextSwitcherRectangle = (TextSwitcher) findViewById(R.id.text_switcher_rectangle);
		punchView = (WaterWaveViewLayout) findViewById(R.id.punchCardView);
		mIBtnGIF = (ImageButton) findViewById(R.id.ibtn_gif);
		mGIFAnim = (AnimationDrawable) mIBtnGIF.getBackground();
		//new DialogLogin(this).show();
		// DialogConfirm dialogConfirm = new DialogConfirm(this, "", "", "",
		// "");
		// dialogConfirm.setOnClickDialog(new OnClickDialog() {
		// @Override
		// public void clickConfirmBtn() {
		// ToastUtil.showToast(R.string.confirm);
		// }
		//
		// @Override
		// public void clickCancelBtn() {
		// ToastUtil.showToast(R.string.cancel);
		// }
		// });
		List<String> content = new ArrayList<String>();
		content.add("足球足球足球足球足球足球足球足球");
		content.add("篮球足球足球足球篮球足球篮球足球");
		content.add("乒乓球");
		content.add("羽毛球");
		content.add("足球足球足球足球足球足球足球足球");
		content.add("篮球足球足球足球篮球足球篮球足球");
		content.add("乒乓球");
		content.add("羽毛球");
		content.add("足球足球足球足球足球足球足球足球");
		content.add("篮球足球足球足球篮球足球篮球足球");
		content.add("乒乓球");
		content.add("羽毛球");
		// DialogSingle dialogSingle = new DialogSingle(this, null, content,
		// "");
		// dialogSingle.setOnClickDialog(new DialogSingle.OnClickDialog() {
		//
		// @Override
		// public void clickContentItem(int position) {
		// ToastUtil.showToast(String.valueOf(position));
		// }
		//
		// @Override
		// public void clickCancelBtn() {
		// ToastUtil.showToast(R.string.cancel);
		// }
		// });
		// DialogMuti dialogSingle = new DialogMuti(this, null, content, "");
		// dialogSingle.setOnClickDialog(new DialogMuti.OnClickDialog() {
		//
		// @Override
		// public void clickContentItem(int position) {
		// ToastUtil.showToast(String.valueOf(position));
		// }
		//
		// @Override
		// public void clickCancelBtn() {
		// ToastUtil.showToast(R.string.cancel);
		// }
		// });
	}

	@Override
	public void loadingData() {
		// settingVolume();

		punchView.setPunshCardListener(punshListener);
		punchView.onCreate();

		PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY,
				Utils.getMetaValue(this, "api_key"));

		mTextSwitcherOval.setFactory(new ViewFactory() {
			@Override
			public View makeView() {
				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				TextView textView = new TextView(mActivity);
				textView.setLayoutParams(params);
				textView.setGravity(Gravity.CENTER);
				textView.setTextSize(18.0f);
				textView.setText(mTextLong[mTextOvalId]);
				LogUtil.i(TAG, "mTextSwitcherOval-->makeView");
				return textView;
			}
		});
		mTextSwitcherOval.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.textswitcher_text_in));
		mTextSwitcherOval.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.textswitcher_text_out));

		mTextSwitcherCircle.setFactory(new ViewFactory() {
			@Override
			public View makeView() {
				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				TextView textView = new TextView(mActivity);
				textView.setLayoutParams(params);
				textView.setGravity(Gravity.CENTER);
				textView.setTextSize(18.0f);
				textView.setText(mTextShort[mTextOvalId]);
				return textView;
			}
		});
		mTextSwitcherCircle.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.textswitcher_text_in));
		mTextSwitcherCircle.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.textswitcher_text_out));

		mTextSwitcherRectangle.setFactory(new ViewFactory() {
			@Override
			public View makeView() {
				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				TextView textView = new TextView(mActivity);
				textView.setLayoutParams(params);
				textView.setGravity(Gravity.CENTER);
				textView.setTextSize(18.0f);
				textView.setText(mTextLong[mTextOvalId]);
				return textView;
			}
		});
		mTextSwitcherRectangle.setInAnimation(AnimationUtils.loadAnimation(
				this, R.anim.textswitcher_text_in));
		mTextSwitcherRectangle.setOutAnimation(AnimationUtils.loadAnimation(
				this, R.anim.textswitcher_text_out));
	}

	@Override
	protected void onStart() {
		super.onStart();
		LogUtil.globalInfoLog("BoutiqueActivity onStart");
	}

	@Override
	public void setLeftBtnClick(View view) {
		// startActivity(new Intent(this, TreeListViewActivity.class));
		// startService(new Intent(this, SignPanelService.class));
		// startActivity(new Intent(this, RSAActivity.class));
		// startActivity(new Intent(this, JNIActivity.class));
		// startActivity(new Intent(this, NineOldActivity.class));
		// startActivity(new Intent(this, VideoViewActivity.class));
		startActivity(new Intent(this, CommonFragmentActivity.class));
	}

	@SuppressLint("NewApi")
	public void setOnClick(View view) {
		switch (view.getId()) {
		case R.id.cascade_layout:
			Intent intent = new Intent(this, CascadeLayoutActivity.class);
			intent.putExtra("testKey", "testValue");
			/*
			 * startActivityForResult启动另一个Activity时，这个Activity的启动方式不能"singleTask"
			 * ，不然会马上直接调用onActivityResult方法，返回的数据为空。
			 */
			startActivityForResult(intent, TEST_REQUEST_CODE);
			break;
		case R.id.transparent_menu:
			startActivity(new Intent(this, TransparentMenuActivity.class));
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			break;
		case R.id.textswitcher_text_pervious:
			mTextOvalId--;
			if (mTextOvalId < 0) {
				mTextOvalId = mTextLong.length - 1;
			}
			mTextSwitcherOval.setText(mTextLong[mTextOvalId]);
			mTextSwitcherCircle.setText(mTextShort[mTextOvalId]);
			mTextSwitcherRectangle.setText(mTextLong[mTextOvalId]);
			startActivity(new Intent(this, SettingActivity.class));
			break;
		case R.id.textswitcher_text_next:
			mTextOvalId++;
			if (mTextOvalId == mTextLong.length) {
				mTextOvalId = 0;
			}
			mTextSwitcherOval.setText(mTextLong[mTextOvalId]);
			mTextSwitcherCircle.setText(mTextShort[mTextOvalId]);
			mTextSwitcherRectangle.setText(mTextLong[mTextOvalId]);
			startActivity(new Intent(this, RSAActivity.class));
			break;
		case R.id.ibtn_gif:
			if (mPlayGIF) {// 通过AnimationDrawable播放和停止GIF动画
				mGIFAnim.start();
				mPlayGIF = false;
			} else {
				mGIFAnim.stop();
				mPlayGIF = true;
			}
			startActivity(new Intent(this, HandleClientRequestActivity.class));
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == TEST_REQUEST_CODE) {
			LogUtil.i(TAG, "TEST_REQUEST_CODE");
			if (data != null) {
				LogUtil.i(TAG, data
						.getStringExtra(CascadeLayoutActivity.TEST_RESULT_KEY));
			} else {
				LogUtil.i(TAG, "data is null");
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		LogUtil.globalInfoLog("(BoutiqueActivity) onSaveInstanceState(Bundle)");
		outState.putString(Constant.SAVE_INSTANCE_STATE,
				"(BoutiqueActivity) restore values that saved in onSaveInstanceState(Bundle)");
	}

	@SuppressLint("NewApi")
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		String outStateValue = savedInstanceState.getString(
				Constant.SAVE_INSTANCE_STATE, "defaultValue");
		LogUtil.globalInfoLog("(BoutiqueActivity) onRestoreInstanceState(Bundle)-->"
				+ outStateValue);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mPressNumberCount < 3) {
				ToastUtil.showToast(mPressNumberCount + "");
				mPressNumberCount++;
				return false;// 效果同return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 打卡控件监听
	 */
	private OnPunshCardListener punshListener = new OnPunshCardListener() {

		@Override
		public void onPunshSuccess() {
		}

		@Override
		public void onPunshFail() {
		}

		@Override
		public void onPunshException() {
		}

		@Override
		public void onPunshCardWait() {
		}

		@Override
		public void onPunshCardIng() {
			// 初始化定位数据

			// 点击打卡首先去定位，定位成功后再去调用打开接口
			// infos.setText(getResources().getString(R.string.location_get_ing));
			// 显示定位转圈图片
			// infos.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
			// null);
			// pbLocationGeting.setVisibility(View.VISIBLE);
		}

		@Override
		public boolean canPunsh() {
			// 如果是免打卡员工
			// if (attendMainActivity.isFreeCard()) {
			// openDialogBox(3);
			// return false;
			// }
			// root禁止打卡
			// if(PublicUtil.isRooted()){
			// openDialogBox(1);
			// return false;
			// }
			// 模拟器禁止打卡
			// if(PublicUtil.isEmulator()){
			// openDialogBox(2);
			// return false;
			// }
			return true;
		}

		@Override
		public void resumeTips() {
		}
	};

	/**
	 * 音量设置
	 */
	@SuppressWarnings("unused")
	private void settingVolume() {
		final SharedPreferences prefs = getSharedPreferences(VOLUME_FILE,
				Context.MODE_PRIVATE);// 获取SharedPreferences对象

		if (mMediaPlayer == null) {
			mMediaPlayer = MediaPlayer.create(this, R.raw.music);
		}
		mMediaPlayer.setLooping(true);// 设置循环播放
		mMediaPlayer.start();// 播放声音

		final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		int maxVolume = audioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 取得最大音量
		int volume = prefs.getInt(VOLUME_MUSIC, 1);
		audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);// 根据当前进度改变亮度

		SeekBar seekBar = new SeekBar(this);
		seekBar.setMax(maxVolume);
		seekBar.setProgress(volume);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {// 移动后放开事件
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				mVolume = seekBar.getProgress();// 取得当前进度
				if (mVolume < 1) {// 当进度小于1时，设置成1，防止太小
					mVolume = 1;
				}
				audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
						mVolume, 0);// 根据当前进度改变亮度
			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher).setTitle(
				R.string.setting_volume);
		builder.setView(seekBar);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Editor editor = prefs.edit();
						editor.putInt(VOLUME_MUSIC, mVolume);
						editor.commit();

						closeMusic();
					}
				}).setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						closeMusic();
					}
				});
		mVoiceDialog = builder.create();
		mVoiceDialog.show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeMusic();

		if (mVoiceDialog != null && mVoiceDialog.isShowing()) {
			mVoiceDialog.dismiss();
			mVoiceDialog = null;
		}
	}

	/**
	 * 关闭音乐。注：该音乐用于调节音量
	 */
	private void closeMusic() {
		if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
			mMediaPlayer.reset();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

}
