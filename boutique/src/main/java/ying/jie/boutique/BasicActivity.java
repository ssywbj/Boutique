package ying.jie.boutique;

import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import ying.jie.util.ActivityCollector;
import ying.jie.util.LogUtil;

/**
 * BasicActivity为公共的Activity，用于实现所有Activity的共有功能。存在继承关系的两个Activity，创建时先执行
 * 同一阶段中父Activity的生命周期方法，再执行子Activity相应的的周期方法（即先有父类再有子类）
 */
public abstract class BasicActivity extends Activity implements View.OnClickListener {
    protected String mLogTag = getClass().getSimpleName();
    /**
     * 是否已经执行过onStart方法
     */
    private boolean mIsExecutedOnStartMethod;
    private View mViewTopBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.setContext(this);
        //先执行BasicActivity的onCreate(Bundle)方法，再执行它的子类的onCreate(Bundle)方法
        LogUtil.globalLogInfo(mLogTag + "-->Parent Activity::onCreate(Bundle)");
        LogUtil.globalLogInfo("-->Parent Activity-->onCreate Activity::" + this);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.basic_aty);

        mViewTopBar = findViewById(R.id.top_bar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*
         *分别执行完父类和子类的onCreate(Bundle)方法后，才执行onStart()方法：
         * 先执行BasicActivity的onStart()方法，再执行它的子类的onStart()方法
         */
        if (!mIsExecutedOnStartMethod) {
            LogUtil.globalLogInfo(mLogTag + "-->Parent Activity::onStart()");
            mIsExecutedOnStartMethod = true;

            LinearLayout parentLayout = (LinearLayout) findViewById(R.id.basic_aty_layout);
            if (this instanceof MainActivity) {//设置主页左侧显示的图片
                parentLayout.setBackgroundResource(R.drawable.boutique_aty_bg);
            }

            try {
                parentLayout.addView(getLayoutInflater().inflate(this.getLayoutId(), null),
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));
                this.initData();
            } catch (Exception e) {
                LogUtil.globalLogError(e.toString());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //同理：先执行BasicActivity的onDestroy()方法，再执行它的子类的onDestroy()方法
        LogUtil.globalLogInfo(mLogTag + "-->Parent Activity::onDestroy()");
        LogUtil.globalLogInfo("-->Parent Activity-->Activity onDestroy()::" + this);
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onClick(View v) {
    }

    /**
     * 左侧图片按钮点击事件
     */
    public void clickLeftBtn(View view) {
        finish();
    }

    /**
     * 设置左侧按钮图片
     *
     * @param drawableResId 图片资源id
     */
    protected void setLeftBtnImage(int drawableResId) {
        ImageButton imageButton = (ImageButton) findViewById(R.id.top_bar_left_btn);
        imageButton.setImageResource(drawableResId);
    }

    /**
     * 右侧按钮点击事件
     */
    public void clickRightBtn(View view) {
    }

    /**
     * 设置右侧按钮文字
     *
     * @param text 要显示的文字
     */
    protected void setRightBtn1Text(String text) {
        if (!TextUtils.isEmpty(text)) {
            TextView textView = (TextView) findViewById(R.id.top_bar_r_btn1);
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
    }

    /**
     * 设置右侧按钮文字
     *
     * @param textResId 文字资源id
     */
    protected void setRightBtn1Text(int textResId) {
        String text = "";
        try {
            text = getString(textResId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setRightBtn1Text(text);
    }

    /**
     * 设置Activity标题
     *
     * @param title 标题文字
     */
    protected void setActivityTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            hideTopBar();
            return;
        }

        TextView textView = (TextView) findViewById(R.id.top_bar_title);
        textView.setText(title);
    }

    /**
     * 设置Activity标题
     *
     * @param textResId 标题文字资源id
     */
    protected void setActivityTitle(int textResId) {
        String title = "";
        try {
            title = getString(textResId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setActivityTitle(title);
    }

    /**
     * 隐藏TopBar
     */
    protected void hideTopBar() {
        if (mViewTopBar.getVisibility() != View.GONE) {
            mViewTopBar.setVisibility(View.GONE);
        }
    }

    /**
     * 显示TopBar
     */
    protected void showTopBar() {
        if (mViewTopBar.getVisibility() != View.VISIBLE) {
            mViewTopBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 返回Activity的布局文件资源(不包括TopBar)，覆写此方法后不再需要调用setContentView方法设置布局
     */
    public abstract int getLayoutId();

    /**
     * 为了编码的规范，此方法用于初始化变量
     */
    public abstract void initData();
}
