package jie.example.boutique;

import jie.example.constant.Constant;
import jie.example.widget.WriterView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.RelativeLayout;

public class SignPanelActivity extends BasicActivity implements OnClickListener {
	private ViewStub mViewStub;
	private View mViewSignPanel;// 把签字板当做普通View处理
	private WriterView mWriterView;
	private float mDownX, mDownY;
	private float mMoveX, mMoveY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setActionBarTitle(R.string.sign_panel);
		setContentView(R.layout.sign_panel_aty);
		initData();
		loadingData();
	}

	@Override
	public void initData() {
		mViewStub = (ViewStub) findViewById(R.id.sign_name_panel);
		mViewSignPanel = mViewStub.inflate();
		mWriterView = (WriterView) mViewSignPanel.findViewById(R.id.sp_wv);
		mViewSignPanel.findViewById(R.id.sp_lb_btn_confirm).setOnClickListener(
				this);
		mViewSignPanel.findViewById(R.id.sp_lb_btn_clear).setOnClickListener(
				this);
		mViewSignPanel.findViewById(R.id.sp_lb_btn_close).setOnClickListener(
				this);
	}

	@Override
	public void setRightBtnClick(View view) {
		if (mViewSignPanel.getVisibility() != View.VISIBLE) {
			mViewSignPanel.setVisibility(View.VISIBLE);
			super.setRightBtnVisibility(View.GONE);
		}
	}

	@Override
	public void loadingData() {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				Constant.screenWidth * 3 / 5, Constant.screenHeight * 2 / 5);
		mViewSignPanel.setLayoutParams(params);// 重新定义mViewSignPanel的宽和高
		mViewSignPanel.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mDownX = event.getRawX();
					mDownY = event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					mMoveX = event.getRawX();
					mMoveY = event.getRawY();
					mViewSignPanel.setX(mViewSignPanel.getX()
							+ (mMoveX - mDownX));
					mViewSignPanel.setY(mViewSignPanel.getY()
							+ (mMoveY - mDownY));
					mDownX = mMoveX;
					mDownY = mMoveY;
					break;
				default:
					break;
				}
				return true;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sp_lb_btn_confirm:
			mWriterView.savePanelText();
			break;
		case R.id.sp_lb_btn_clear:
			mWriterView.clearPanel();
			break;
		case R.id.sp_lb_btn_close:
			mWriterView.clearPanel();
			mViewSignPanel.setVisibility(View.GONE);
			super.setRightBtnVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

}
