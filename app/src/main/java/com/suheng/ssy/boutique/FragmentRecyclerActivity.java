package com.suheng.ssy.boutique;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.suheng.ssy.boutique.fragment.RecyclerFragment;

public class FragmentRecyclerActivity extends PermissionApplyActivity {

    private RecyclerFragment mRecyclerFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_recycler);
        //getSupportFragmentManager().beginTransaction().add(new RecyclerFragment(), "RecyclerFragment").commit();//创建fragment但是不绘制UI
        mRecyclerFragment = new RecyclerFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_fragment, mRecyclerFragment).commit();
        /*//通过路由获取Fragment实例
        getSupportFragmentManager().beginTransaction().add(R.id.layout_fragment,
                (RecyclerFragment) ARouter.getInstance().build("/app/activity/fragment_recycler").navigation()).commit();*/
    }

    public void queryExternalStoragePermission(int requestCode) {
        PermissionApplyActivityPermissionsDispatcher.requestExternalStoragePermissionWithCheck(this, requestCode);
    }

    public void queryCallPhonePermission(String phoneNumber) {
        PermissionApplyActivityPermissionsDispatcher.requestCallPhonePermissionWithCheck(this, phoneNumber);
    }

    @Override
    public void requestExternalStoragePermission(int requestCode) {
        super.requestExternalStoragePermission(requestCode);
        mRecyclerFragment.saveInfoInFile();
    }

    @Override
    public void onBackPressed() {
        if (mRecyclerFragment.hasNextNewbie()) {
            mRecyclerFragment.onNextNewbie();
        } else {
            super.onBackPressed();
        }
    }
}
