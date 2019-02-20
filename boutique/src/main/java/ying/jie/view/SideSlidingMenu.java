package ying.jie.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import com.nineoldandroids.view.ViewHelper;

import ying.jie.boutique.App;
import ying.jie.boutique.R;
import ying.jie.util.LogUtil;

/**
 * 使用HorizontalScrollView自定义侧滑菜单，菜单页面结构：左菜单--主菜单
 */
public class SideSlidingMenu extends HorizontalScrollView {
    private static final String TAG = SideSlidingMenu.class.getSimpleName();
    private ViewGroup mLeftLayout, mMainLayout;
    /**
     * 菜单距离屏幕右边的宽度
     */
    private int mSideMenuMargin = 100;
    /**
     * 左侧菜单的宽度
     */
    private int mMenuWidth = 0;
    /**
     * 显示侧滑菜单的临界坐标的X轴坐标
     */
    private int mShowMenuXPivot = 0;
    /**
     * 是否已经打开菜单
     */
    private boolean isMenuOpen;

    public SideSlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideSlidingMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SideSlidingMenu, defStyle, defStyle);
        mSideMenuMargin = typedArray.getDimensionPixelSize(R.styleable.SideSlidingMenu_rightPadding,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f,
                        getResources().getDisplayMetrics()));
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mLeftLayout = (ViewGroup) findViewById(R.id.boutique_side_menu_layout);
        mMainLayout = (ViewGroup) findViewById(R.id.main_layout);
        mMenuWidth = App.screenWidth - mSideMenuMargin;
        mShowMenuXPivot = mMenuWidth / 2;//坐标(mShowMenuXPivot, 0)
        LogUtil.i(TAG, "mShowMenuXPivot = " + mShowMenuXPivot + ", mMenuWidth = "
                + mMenuWidth + ", mSideMenuMargin = " + mSideMenuMargin);
        mLeftLayout.getLayoutParams().width = mMenuWidth;
        mMainLayout.getLayoutParams().width = App.screenWidth;
        //设置各个菜单的宽度后，左菜单的的开始位置为(0, 0),主菜单的的开始位置为(mMenuWidth, 0)
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.closeLeftMenu();//滑动到左菜单界面结束的地方(也是主菜单开始的地方)，即显示主菜单
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();//获取HorizontalScrollView滑动到的位置的X轴坐标

                if (scrollX <= mShowMenuXPivot) {//当滑动到或滑过(mShowMenuXPivot, 0)坐标时，显示左菜单
                    this.openLeftMenu();
                } else {//未滑动到(mShowMenuXPivot, 0)坐标时，显示的依然是主菜单
                    this.closeLeftMenu();
                }

                return true;//此处立刻返回true，否则不会自动滑动到停止，即滑到哪就停在哪
            default:
                break;
        }

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float ratio = l * 1.0f / mMenuWidth;// 比值，0~1之间
        //LogUtil.i(TAG, "ratio = " + ratio);
        ViewHelper.setTranslationX(mLeftLayout, mMenuWidth * ratio);// 揭幕式地打开菜单
        ViewHelper.setAlpha(mLeftLayout, 1 - ratio);
    }

    /**
     * 打开左菜单
     */
    private void openLeftMenu() {
        smoothScrollTo(0, 0);
        isMenuOpen = true;
    }

    /**
     * 返回主菜单
     */
    public void closeLeftMenu() {
        smoothScrollTo(mMenuWidth, 0);
        isMenuOpen = false;
    }

    /**
     * 切换菜单状态
     */
    public void switchMenu() {
        if (isMenuOpen) {
            this.closeLeftMenu();
        } else {
            this.openLeftMenu();
        }
    }

}
