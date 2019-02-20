package ying.jie.boutique;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import ying.jie.boutique.menu_function.ScreenCaptureManager;
import ying.jie.util.BitmapUtil;
import ying.jie.util.FileManager;
import ying.jie.util.LogUtil;
import ying.jie.util.ToastUtil;

/**
 * Created by Weibj on 2016/1/8.
 */
public class VideoViewActivity extends BasicActivity {
    private VideoView mVideoView;
    private MediaController mMediaController;
    private ScrollView mLayoutDescribe;
    private int mVideoPortraitHeight = 0;//竖屏时，VideoView的高度
    private Button mBtnTakeVideoPic;
    private File mVideoFile;
    private ScreenCaptureManager mScreenCaptureManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i(mLogTag, mLogTag + "::onCreate");
        super.setActivityTitle(getIntent().getStringExtra(App.INT_ACT_TITLE));
    }

    /**
     * 返回Activity的布局文件资源
     */
    @Override
    public int getLayoutId() {
        return R.layout.videoview_aty;
    }

    /**
     * 为了编码的规范，此方法用于初始化变量
     */
    @Override
    public void initData() {
        mVideoView = (VideoView) findViewById(R.id.video_view);
        mMediaController = new MediaController(this);
        mLayoutDescribe = (ScrollView) findViewById(R.id.bottom_layout);
        findViewById(R.id.text_describe_video).setOnClickListener(this);
        mBtnTakeVideoPic = (Button) findViewById(R.id.btn_take_video_pic);
        mBtnTakeVideoPic.setOnClickListener(this);

        mVideoPortraitHeight = getResources().getDimensionPixelOffset(R.dimen.video_view_height);

        //非竖屏情况下VideoView全屏显示
        if (getResources().getConfiguration().orientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            this.videoLandscapeLayout();
        }

        //new File("file:///android_asset/文件名")，错误；因为apk安装以后放在/data/app/**.apk，以apk形式存在，
        //asset/res和apk在一起，并不会解压到/data/data/YourApp目录，所以无法直接获取到assets的绝对路径，因为它根本不存在
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            mVideoFile = new File(Environment.getExternalStorageDirectory(),
                    "jiang_nan_style.mp4");
            if (mVideoFile.exists()) {
                //mVideoView.setVideoURI(Uri.parse("file:///android_asset/jiang_nan_style"));
                mVideoView.setVideoPath(mVideoFile.getAbsolutePath());
                mVideoView.setMediaController(mMediaController);
                mMediaController.setMediaPlayer(mVideoView);
                mVideoView.requestFocus();
                mVideoView.start();
            } else {
                ToastUtil.showToast(R.string.file_no_exist);
            }
        }

        mScreenCaptureManager = new ScreenCaptureManager(this);
    }

    /**
     * VideoView横屏时的布局
     */
    private void videoLandscapeLayout() {
        super.hideTopBar();
        mLayoutDescribe.setVisibility(View.GONE);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mVideoView.setLayoutParams(params);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.i(mLogTag, mLogTag + "::onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.i(mLogTag, mLogTag + "::onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.i(mLogTag, mLogTag + "::onDestroy");
        mScreenCaptureManager.releaseResources();
    }

    /*若Activity配置了android:configChanges="orientation|screenSize"属性，则当屏幕方向发生改变时，
      Activity不会重新执行生命周期函数，但会调用这个函数；反之，若是没有配置该属性，当屏幕方向发生改变时，
      Activity会重新执行生命周期函数，但不会调用这个函数。*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtil.i(mLogTag, mLogTag + "::onConfigurationChanged");
        this.updateVideoViewSize();
    }

    /**
     * 更新VideoView的尺寸
     */
    private void updateVideoViewSize() {
        if (getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            mLayoutDescribe.setVisibility(View.VISIBLE);
            super.showTopBar();
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, mVideoPortraitHeight);
            mVideoView.setLayoutParams(params);
            mBtnTakeVideoPic.setVisibility(View.GONE);
        } else {// 横屏情况下，VideoView全屏显示
            this.videoLandscapeLayout();
            mBtnTakeVideoPic.setVisibility(View.VISIBLE);
        }
    }

    private void takeVideoPicture() {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(mVideoFile.getAbsolutePath());
        int nowTime = mVideoView.getCurrentPosition();
        Bitmap bitmap = mediaMetadataRetriever.
                getFrameAtTime(nowTime * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
        LogUtil.i(mLogTag, "bitmap--> " + bitmap);
        if (bitmap == null) {
            return;
        }

        try {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + nowTime + ".png");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.close();
            bitmap.recycle();
        } catch (Exception e) {
            LogUtil.e(mLogTag, "Error: " + e.toString());
        }

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.text_describe_video || i == R.id.btn_take_video_pic) {
            this.takeVideoPicture();
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

}
