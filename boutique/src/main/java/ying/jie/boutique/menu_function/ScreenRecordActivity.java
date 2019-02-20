package ying.jie.boutique.menu_function;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ying.jie.boutique.App;
import ying.jie.boutique.BasicActivity;
import ying.jie.boutique.R;
import ying.jie.util.LogUtil;
import ying.jie.util.ToastUtil;

public class ScreenRecordActivity extends BasicActivity {
    private ScreenRecordManager mScreenRecordManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setActivityTitle(getIntent().getStringExtra(App.INT_ACT_TITLE));
    }

    @Override
    public int getLayoutId() {
        return R.layout.screen_record_aty;
    }

    @Override
    public void initData() {
        findViewById(R.id.text_screen_record).setOnClickListener(this);

        mScreenRecordManager = new ScreenRecordManager(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.text_screen_capture) {
            mScreenRecordManager.applyPermission(new ScreenRecordManager.CaptureScreenCallback() {
                @Override
                public void applyPermissionFail() {
                    ToastUtil.showToast(R.string.api_smaller_than_21);
                }

                @Override
                public void captureFail(String failInfo) {
                    LogUtil.e(ScreenCaptureManager.TAG, "capture fail-->" + failInfo);
                }
            });
        } else if (i == R.id.text_screen_record) {
            mScreenRecordManager.applyPermission(null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ScreenCaptureManager.REQUEST_MEDIA_PROJECTION:
                if (resultCode != Activity.RESULT_OK || data == null) {
                    ToastUtil.showToast(R.string.apply_record_screen_fail);
                    return;
                }
                mScreenRecordManager.recordScreen(resultCode, data);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScreenRecordManager.releaseResources();
    }
}

