package ying.jie.boutique.menu_function;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.Random;

import ying.jie.boutique.App;
import ying.jie.boutique.R;
import ying.jie.util.LogUtil;

/**
 * 魔法表情悬浮移动View
 */
public class ExpressionFloatView extends ImageView implements
        App.HandlerCallback {
    private static final String TAG = ExpressionFloatView.class.getSimpleName();
    private static final int MSG_BEGIN_ANIM = 12;
    private static final int ANIM_DURATION = 2000;
    private App.AppHandler mHandler = new App.AppHandler(this);
    private DisplayMetrics mDisplayMetrics;
    /**
     * 三种下滑路线：0是S型，1是Z型，2是直线型
     */
    private int mPathType;
    /**
     * 下滑的垂直距离
     */
    private int mDropDistance;
    private Interpolator mInterpolator;
    private int mImageSize;
    //贝赛尔曲线的两个端点
    private PointF mBeginPoint = new PointF();
    private PointF mEndPoint = new PointF();
    //贝赛尔曲线中间的两个临界点
    private PointF mMiddlePointOne = new PointF();
    private PointF mMiddlePointTwo = new PointF();

    public ExpressionFloatView(Context context) {
        super(context);
        this.initViewAndData();
    }

    private void initViewAndData() {
        mImageSize = getResources().getDimensionPixelSize(R.dimen.expression_float_image_size);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(mImageSize, mImageSize);
        setLayoutParams(layoutParams);

        mDisplayMetrics = getResources().getDisplayMetrics();
        this.productPoints();
    }

    private void productPoints() {
        //开始下滑的区域1
        int areaOneMinX = (int) (mDisplayMetrics.widthPixels * 0.03);
        int areaOneMaxX = (int) (mDisplayMetrics.widthPixels * 0.60);
        int areaOneMinY = (int) (mDisplayMetrics.heightPixels * 0.01);
        int areaOneMaxY = (int) (mDisplayMetrics.heightPixels * 0.24);
        //LogUtil.d(TAG, areaOneMinX + "--" + areaOneMaxX + ", " + areaOneMinY + "--" + areaOneMaxY);
        //开始下滑的区域2
        int areaTwoMinX = (int) (mDisplayMetrics.widthPixels * 0.54);
        int areaTwoMaxX = (int) (mDisplayMetrics.widthPixels * 0.86);
        int areaTwoMinY = (int) (mDisplayMetrics.heightPixels * 0.28);
        int areaTwoMaxY = (int) (mDisplayMetrics.heightPixels * 0.42);
        //LogUtil.d(TAG, areaTwoMinX + "--" + areaTwoMaxX + ", " + areaTwoMinY + "--" + areaTwoMaxY);

        Random random = new Random();
        int areaType = random.nextInt(2);
        if (areaType == 0) {
            mBeginPoint.x = this.productCoordinate(areaOneMinX, areaOneMaxX);
            mBeginPoint.y = this.productCoordinate(areaOneMinY, areaOneMaxY);
        } else {
            mBeginPoint.x = this.productCoordinate(areaTwoMinX, areaTwoMaxX);
            mBeginPoint.y = this.productCoordinate(areaTwoMinY, areaTwoMaxY);
        }
        //LogUtil.d(TAG, "(" + mBeginPoint.x + ", " + mBeginPoint.y + ")");
        setX(mBeginPoint.x);
        setY(mBeginPoint.y);

        mDropDistance = (int) (mDisplayMetrics.heightPixels * 0.66 - mBeginPoint.y - mImageSize);

        mPathType = random.nextInt(3);//随机产生三种下滑路线
        if (mPathType == 0) {
            mMiddlePointOne.x = mBeginPoint.x - 400;
            if (mMiddlePointOne.x < 0) {
                mMiddlePointOne.x = 0;
            }
        } else if (mPathType == 1) {
            if (areaType == 0) {
                mMiddlePointOne.x = mBeginPoint.x + 430 - mImageSize;
            } else {
                mMiddlePointOne.x = mBeginPoint.x - mImageSize - 20;
            }
            if (mMiddlePointOne.x > mDisplayMetrics.widthPixels) {
                mMiddlePointOne.x = mDisplayMetrics.widthPixels - mImageSize - 50;
            }
        }
        mMiddlePointOne.y = mBeginPoint.y + mDropDistance * 0.33f;

        mMiddlePointTwo.x = mBeginPoint.x;
        mMiddlePointTwo.y = mBeginPoint.y + mDropDistance * 0.66f;

        if (mPathType == 0) {
            mEndPoint.x = mMiddlePointOne.x + 10;
        } else if (mPathType == 1) {
            mEndPoint.x = mMiddlePointOne.x - 10;
        }
        mEndPoint.y = mBeginPoint.y + mDropDistance + 20;

        int interpolatorType = random.nextInt(3);//三种变速插补器
        if (interpolatorType == 0) {
            mInterpolator = new LinearInterpolator();//匀速
        } else if (interpolatorType == 1) {
            mInterpolator = new DecelerateInterpolator();//减速
        } else {
            mInterpolator = new AccelerateDecelerateInterpolator();//先加速后减速
        }
    }

    /**
     * 在给定区间中生产合适的数
     *
     * @param min 最小值
     * @param max 最大值
     * @return min < x < max
     */
    private int productCoordinate(final int min, final int max) {
        Random random = new Random();
        int coordinate = random.nextInt(max);
        while (coordinate < min) {
            coordinate = random.nextInt(max);
        }
        return coordinate;
    }

    public void setImageExpressionAndBeginAnim(Drawable drawable, boolean isSendByMyself) {
        if (isSendByMyself) {
            setBackgroundResource(R.drawable.expression_bg_send);
        } else {
            setBackgroundResource(R.drawable.expression_bg_receive);
        }
        setImageDrawable(drawable);

        mHandler.sendEmptyMessageDelayed(MSG_BEGIN_ANIM, 300);//停留300毫秒再执行动画
    }

    private void setAnimationProperty() {
        AnimatorSet animatorSet = new AnimatorSet();//动画组合
        animatorSet.setDuration(ANIM_DURATION);//动画执行时间
        animatorSet.setInterpolator(mInterpolator);//设置加速器
        animatorSet.setTarget(this);//执行动画的View
        animatorSet.addListener(new AnimStatusListener());//动画执行状态监听

        ObjectAnimator alpha = ObjectAnimator.ofFloat(this, View.ALPHA, 1f, 0.4f);//透明度1(完全不透明)~0.3

        if (mPathType == 2) {//垂直下落
            ObjectAnimator translationHide = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, mEndPoint.y);
//            translationHide.setDuration(ANIM_DURATION);//单个动画执行
//            translationHide.setInterpolator(mInterpolator);
//            translationHide.start();

//        ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, View.SCALE_X, 1f, 1.3f);//X轴方向放大
//        ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, View.SCALE_Y, 0.7f, 1f);//Y轴方向缩小
            animatorSet.playTogether(alpha, translationHide);//一起执行、异步播放
        } else {
            animatorSet.playTogether(alpha, this.setAndBeginBezierAnim());
        }
        animatorSet.start();
    }

    /**
     * 设置贝赛尔曲线动画
     */
    private ValueAnimator setAndBeginBezierAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofObject(new BezierEvaluator(), new PointF(mBeginPoint.x, mBeginPoint.y),
                new PointF(mEndPoint.x, mEndPoint.y));//传入起点和终点
        valueAnimator.addUpdateListener(new ShiftTraceListener());
//        valueAnimator.setTarget(this);
//        valueAnimator.setDuration(ANIM_DURATION);
//        valueAnimator.setInterpolator(mInterpolator);
//        valueAnimator.start();
        return valueAnimator;
    }

    /**
     * 贝赛尔曲线生成器
     */
    private class BezierEvaluator implements TypeEvaluator<PointF> {
        /**
         * @param time       time取值0到1
         * @param startValue 起始点
         * @param endValue   终点
         */
        @Override
        public PointF evaluate(float time, PointF startValue, PointF endValue) {
            float timeLeft = 1.0f - time;

            PointF point = new PointF();//代入公式获取结果
            point.x = timeLeft * timeLeft * timeLeft * (startValue.x)
                    + 3 * timeLeft * timeLeft * time * (mMiddlePointOne.x)
                    + 3 * timeLeft * time * time * (mMiddlePointTwo.x)
                    + time * time * time * (endValue.x);
            point.y = timeLeft * timeLeft * timeLeft * (startValue.y)
                    + 3 * timeLeft * timeLeft * time * (mMiddlePointOne.y)
                    + 3 * timeLeft * time * time * (mMiddlePointTwo.y)
                    + time * time * time * (endValue.y);
            return point;
        }
    }

    /**
     * 贝赛尔曲线移动轨迹监听
     */
    private class ShiftTraceListener implements ValueAnimator.AnimatorUpdateListener {

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            //获取到贝塞尔曲线计算出来的坐标并赋值给view，让view随着曲线走
            PointF pointF = (PointF) animation.getAnimatedValue();
            setX(pointF.x);
            setY(pointF.y);
        }
    }

    @Override
    public void dispatchMessage(Message msg) {
        switch (msg.what) {
            case MSG_BEGIN_ANIM:
                this.setAnimationProperty();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeMessages(MSG_BEGIN_ANIM);
    }

    /**
     * 动画执行状态监听
     */
    private class AnimStatusListener extends AnimatorListenerAdapter {
        @Override
        public void onAnimationStart(Animator animation) {
            super.onAnimationStart(animation);
            //LogUtil.d(TAG, "animation start");
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            //LogUtil.d(TAG, "animation end");
            ((ViewGroup) getParent()).removeView(ExpressionFloatView.this);//动画结束后，移除当前View
        }
    }

}

