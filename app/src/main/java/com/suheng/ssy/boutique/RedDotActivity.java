package com.suheng.ssy.boutique;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

public class RedDotActivity extends BasicActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_dot);

        //ImmersionBar.with(this).init();//默认为透明色状态栏和黑色导航栏且Toolbar会盖住状态栏
        ImmersionBar.with(this).fitsSystemWindows(true)//使用该属性,必须指定状态栏颜色
                .statusBarColor(R.color.text_switch_login_type).init();

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {//设置Toolbar返回按钮的点击事件
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//创建菜单
        getMenuInflater().inflate(R.menu.red_dot, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//设置菜单点击事件
        switch (item.getItemId()) {
            case R.id.action_msg:
                ImmersionBar.with(this).fitsSystemWindows(true)//使用该属性,必须指定状态栏颜色
                        .statusBarColor(R.color.login_btn_pressed).init();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();//必须调用该方法，防止内存泄漏
    }

    private Badge mBadge;
    private int mRedCount = 13;

    public void onClickMsg(View view) {
        if (mBadge == null) {
            View actionView = mToolbar.getMenu().findItem(R.id.action_msg).getActionView();
            Log.i(mTag, "getActionView = " + actionView);
            mBadge = new QBadgeView(this).bindTarget(/*view*/actionView.findViewById(R.id.text));
            mBadge.setBadgeNumber(mRedCount);
            mBadge.setBadgeTextSize(10, true);
            mBadge.setBadgeGravity(Gravity.END | Gravity.TOP);

            mBadge.setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                @Override
                public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                }
            });
        } else {
            mBadge.setBadgeNumber(--mRedCount);
            if (mRedCount == 0) {
                mBadge = null;
                mRedCount = 13;
            }
        }
    }
}
