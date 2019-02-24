package jie.example.boutique;

import java.util.Random;

import com.nineoldandroids.view.animation.AnimatorProxy;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class NineOldActivity extends BasicActivity implements AnimatorListener {
	private static final int[] PHOTOS = { R.drawable.extract_screen_bg_a,
			R.drawable.extract_screen_bg_b, R.drawable.extract_screen_bg_c,
			R.drawable.extract_screen_bg_d, R.drawable.extract_screen_bg_e };
	private FrameLayout mContainer;
	private ImageView mNewView;
	private Random mRandom = new Random();
	private int mIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setActionBarTitle(R.string.nineoldapi);
		initData();
		loadingData();
	}

	@Override
	public void initData() {
		mContainer = new FrameLayout(this);
		mNewView = createNewView();
	}

	@Override
	public void loadingData() {
		mContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		mContainer.addView(mNewView);
		setContentView(mContainer);
		nextAnimation();
	}

	private ImageView createNewView() {
		ImageView imageView = new ImageView(this);
		imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setImageResource(PHOTOS[mIndex]);
		mIndex = (mIndex + 1 < PHOTOS.length) ? mIndex + 1 : 0;
		return imageView;
	}

	private void nextAnimation() {
		AnimatorSet anim = new AnimatorSet();
		final int index = mRandom.nextInt(4);
		switch (index) {
		case 0:
			anim.playTogether(ObjectAnimator.ofFloat(mNewView, "scaleX", 1.5f,
					1f));
			break;
		case 1:
			anim.playTogether(
					ObjectAnimator.ofFloat(mNewView, "scaleX", 1, 1.5f),
					ObjectAnimator.ofFloat(mNewView, "scaleY", 1, 1.5f));
			break;
		case 2:
			AnimatorProxy.wrap(mNewView).setScaleX(1.5f);
			AnimatorProxy.wrap(mNewView).setScaleY(1.5f);
			anim.playTogether(ObjectAnimator.ofFloat(mNewView, "translationY",
					80f, 0f));
			break;
		case 3:
		default:
			AnimatorProxy.wrap(mNewView).setScaleX(1.5f);
			AnimatorProxy.wrap(mNewView).setScaleY(1.5f);
			anim.playTogether(ObjectAnimator.ofFloat(mNewView, "translationX",
					0f, 40f));
			break;
		}

		anim.setDuration(3000);
		anim.addListener(this);
		anim.start();
	}

	@Override
	public void onAnimationStart(Animator animation) {
	}

	@Override
	public void onAnimationEnd(Animator animation) {
		mContainer.removeView(mNewView);// 先移除当前图片
		mNewView = createNewView();
		mContainer.addView(mNewView);

		nextAnimation();
	}

	@Override
	public void onAnimationCancel(Animator animation) {
	}

	@Override
	public void onAnimationRepeat(Animator animation) {
	}

}
