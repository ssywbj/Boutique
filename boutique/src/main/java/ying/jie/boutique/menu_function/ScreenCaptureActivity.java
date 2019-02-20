package ying.jie.boutique.menu_function;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import java.util.Date;

import ying.jie.boutique.App;
import ying.jie.boutique.BasicActivity;
import ying.jie.boutique.R;
import ying.jie.util.BitmapUtil;
import ying.jie.util.FileManager;
import ying.jie.util.LogUtil;
import ying.jie.util.ToastUtil;

public class ScreenCaptureActivity extends BasicActivity {
    private ScreenCaptureManager mScreenCaptureManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setActivityTitle(getIntent().getStringExtra(App.INT_ACT_TITLE));
    }

    @Override
    public int getLayoutId() {
        return R.layout.screen_capture_aty;
    }

    @Override
    public void initData() {
        findViewById(R.id.text_screen_capture).setOnClickListener(this);

        mScreenCaptureManager = new ScreenCaptureManager(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.text_screen_capture) {
            if (!mScreenCaptureManager.isHadPermission()) {
                mScreenCaptureManager.applyPermission(new ScreenCaptureManager.CaptureScreenCallback() {
                    @Override
                    public void applyPermissionFail() {
                        ToastUtil.showToast(R.string.api_smaller_than_21);
                    }

                    @Override
                    public void captureFail(String failInfo) {
                        LogUtil.e(ScreenCaptureManager.TAG, "capture fail-->" + failInfo);
                    }

                    @Override
                    public void captureSuccess(Bitmap bitmap) {
                        ToastUtil.showToast(getString(R.string.capture_success));
                        BitmapUtil.saveImageFile(bitmap, FileManager.DIR_SCREEN_CAPTURE, new Date().getTime() + ".png");
                    }
                });
            } else {
                mScreenCaptureManager.captureScreen();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ScreenCaptureManager.REQUEST_MEDIA_PROJECTION:
                if (resultCode != Activity.RESULT_OK || data == null) {
                    ToastUtil.showToast(R.string.apply_capture_screen_fail);
                    return;
                }

                mScreenCaptureManager.applyPermissionSuccessAndBeginCapture(resultCode, data);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mScreenCaptureManager.releaseResources();
    }
}

