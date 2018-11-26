package com.suheng.ssy.boutique;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

//https://blog.csdn.net/mynameishuangshuai/article/details/51491074
//http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0520/2897.html
//https://developer.android.com/guide/components/tasks-and-back-stack?hl=zh-cn
public class LaunchTypeActivity extends BasicActivity {

    protected TextView mTextInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(mTag, "onCreate：" + getClass().getSimpleName() + "，TaskId: " + getTaskId() + "，hasCode:" + hashCode());
        dumpTaskAffinity();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(mTag, "onNewIntent：" + getClass().getSimpleName() + "，TaskId: " + getTaskId() + "，hasCode:" + hashCode());
        this.dumpTaskAffinity();
    }

    protected void dumpTaskAffinity() {
        try {
            ActivityInfo info = this.getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
            Log.d(mTag, "taskAffinity：" + info.taskAffinity);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void openStandardAty(View view) {
        startActivity(new Intent(this, LaunchStandardActivity.class));
    }

    public void openTopAty(View view) {
        startActivity(new Intent(this, SingleTopActivity.class));
    }

    public void openTaskAty(View view) {
        startActivity(new Intent(this, SingleTaskActivity.class));
    }

    public void openInstanceAty(View view) {
        startActivity(new Intent(this, SingleInstanceActivity.class));
    }
}
