package jie.example.widget;

import java.lang.ref.WeakReference;

import jie.example.boutique.R;
import jie.example.constant.Constant;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 具有水波效果的View：半径逐渐变大，透明度逐渐变小
 */
public class WaveView extends View {
	private static final int WHAT_REFRESH = 1;
	private static final long DEFAULT_SLEEP_TIME = 100L;
	private boolean isSecondRunning = false;
	private DrawThread drawThread;
	private Paint mPaint;// 空心圆
	private Paint mCenterPaint;// 中间不做渐变的圆

	private Paint mTimePaint;// 绘制时间 paint
	private Circle firstCircle;
	private Circle secondCircle;
	private Point centerPoint;// center point

	private int yOffset = 100;// margin top.
	private static final int DEFAULT_ALPHA = 120;// alpha
	private static int startRadius = 120; // radius

	public WaveView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public WaveView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		int height = Constant.screenHeight * 25 / 100;
		int centerX = Constant.screenWidth / 2;
		int centerY = height / 2 + yOffset;// 这里+100表示是距离顶部的高度
		centerPoint = new Point(centerX, centerY);
		startRadius = height / 2;// init start
		initPaint();
		initCircle();
	}

	private void initPaint() {
		int circleColorId = getResources().getColor(R.color.eagle_one);
		mPaint = new Paint();
		mPaint.setColor(Color.BLUE);
		mPaint.setAntiAlias(true);// 抗锯齿
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(0);

		mCenterPaint = new Paint();
		mCenterPaint.setColor(circleColorId);
		mCenterPaint.setAntiAlias(true);
		mCenterPaint.setStyle(Paint.Style.STROKE);
		mCenterPaint.setStrokeWidth(20F);

		// time paint
		mTimePaint = new Paint();
		mTimePaint.setColor(circleColorId);
		mTimePaint.setAntiAlias(true);
		mTimePaint.setStyle(Paint.Style.FILL);
		mTimePaint.setTextSize(60);
	}

	// 初始化圆
	private void initCircle() {
		firstCircle = new Circle(startRadius, centerPoint.x, centerPoint.y,
				DEFAULT_ALPHA);
		secondCircle = new Circle(startRadius, centerPoint.x, centerPoint.y,
				DEFAULT_ALPHA);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBg(canvas);
	}

	/**
	 * 两个渐变消失的圆
	 */
	private void drawBg(Canvas canvas) {
		try {
			// 第1个圆
			mPaint.setAlpha(firstCircle.alpha);
			mPaint.setStrokeWidth((firstCircle.radius - startRadius) * 2);
			canvas.drawCircle(firstCircle.point.x, firstCircle.point.y,
					firstCircle.radius, mPaint);
			// 第2个圆
			mPaint.setAlpha(secondCircle.alpha);
			mPaint.setStrokeWidth((secondCircle.radius - startRadius) * 2);
			canvas.drawCircle(secondCircle.point.x, secondCircle.point.y,
					secondCircle.radius, mPaint);
		} catch (Exception e) {
			Log.e(Constant.TAG_GLOBAL,
					"Paint Circle Exception-->" + e.toString());
		}
	}

	private long logic() {
		long startTime = System.currentTimeMillis();
		doWaitPunshCard();
		long endTime = System.currentTimeMillis();
		return endTime - startTime;
	}

	/**
	 * 等待打卡的绘制
	 */
	private void doWaitPunshCard() {
		// 半径变大，透明度变小
		offsetCircle(firstCircle);

		// 当第1个圆渐变到透明度只有50的时候 开始绘制第2个圆
		if (firstCircle.alpha < 50) {
			isSecondRunning = true;
		}

		// 当第1个消失后 重置透明度，等待
		if (firstCircle.alpha < 0) {
			resetCircle(firstCircle);
		}

		// 重置第2个圆，
		if (isSecondRunning) {
			offsetCircle(secondCircle);
			if (secondCircle.alpha < 0) {
				resetCircle(secondCircle);
			}
		}
	}

	/**
	 * 重置Circle.消失后，重新开始.
	 */
	private void resetCircle(Circle targetCircle) {
		targetCircle.alpha = DEFAULT_ALPHA;
		targetCircle.radius = startRadius;
	}

	/**
	 * 半径变大，透明度变小
	 */
	private void offsetCircle(Circle targetCircle) {
		targetCircle.alpha -= 5;
		targetCircle.radius += 2;
	}

	private void resetDraw() {
		resetCircle(firstCircle);
		resetCircle(secondCircle);
		isSecondRunning = false;
		mHandler.sendEmptyMessage(WHAT_REFRESH);
	}

	// 开始线程
	public void startDrawThread() {
		if (null == drawThread) {
			drawThread = new DrawThread("PunchCard", this);
			drawThread.startDraw();
		}
	}

	// 暂停线程
	public void pauseDrawThread() {
		if (null != drawThread && drawThread.isThreadRuning) {
			drawThread.isThreadRuning = false;
			resetDraw(); // 需要重置绘制
		}
	}

	// 恢复线程
	public void resumeDrawThread() {
		if (null != drawThread && drawThread.isThreadStart
				&& !drawThread.isThreadRuning) {
			drawThread.isThreadRuning = true;
		}
	}

	// 停止线程。
	public void stopDrawThread() {
		if (null != drawThread) {
			drawThread.stopDraw();
			drawThread = null;
		}
	}

	private UiHandler mHandler = new UiHandler(this);

	private static class UiHandler extends Handler {

		private WeakReference<WaveView> weak;

		public UiHandler(WaveView target) {
			this.weak = new WeakReference<WaveView>(target);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			final WaveView target = weak.get();
			if (null != target) {
				switch (msg.what) {
				case WHAT_REFRESH:
					target.invalidate();
					break;
				default:
					break;
				}
			}
		}
	}

	// 内部线程类
	private static class DrawThread extends Thread {
		private WeakReference<WaveView> weak;
		private boolean isThreadStart = false; // 线程是否开始运行
		private boolean isThreadRuning = false;// 线程是否需要运行。

		public DrawThread(String name, WaveView target) {
			super(name);
			weak = new WeakReference<WaveView>(target);
		}

		public synchronized void startDraw() {
			if (!this.isThreadStart) {
				isThreadStart = true;
				start();
			}
		}

		@Override
		public synchronized void start() {
			super.start();
			isThreadRuning = true;
		}

		public synchronized void stopDraw() {
			isThreadRuning = false;
			isThreadStart = false;
		}

		@Override
		public void run() {
			while (isThreadStart) {
				if (isThreadRuning) {
					final WaveView target = weak.get();
					if (null != target) {
						long host = target.logic();
						try {
							if (host < DEFAULT_SLEEP_TIME) {
								Thread.sleep((DEFAULT_SLEEP_TIME - host));
							}
						} catch (InterruptedException e) {
							Log.e(Constant.TAG_GLOBAL,
									"Paint Circle Thread Exception-->"
											+ e.toString());
						}
						target.mHandler.sendEmptyMessage(WHAT_REFRESH);
					}
				}
			}
		}
	}

	// 圆圈封装类。
	class Circle {
		private Point point;
		private float radius;
		private int alpha;

		public Circle(float radius, int x, int y, int alpha) {
			this.radius = radius;
			this.alpha = alpha;
			this.point = new Point(x, y);
		}
	}

}
