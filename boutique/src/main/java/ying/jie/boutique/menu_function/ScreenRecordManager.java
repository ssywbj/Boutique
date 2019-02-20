package ying.jie.boutique.menu_function;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.IOException;

import ying.jie.util.LogUtil;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScreenRecordManager {
    public static final String TAG = ScreenRecordManager.class.getSimpleName();
    public static final int REQUEST_MEDIA_PROJECTION = 0x11;
    private MediaProjectionManager mProjectionManager;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaRecorder mMediaRecorder;
    private Activity mActivity;
    DisplayMetrics mDisplayMetrics = new DisplayMetrics();
    private CaptureScreenCallback mCaptureScreenCallback;

    public ScreenRecordManager(Activity activity) {
        mActivity = activity;
    }

    public void applyPermission(CaptureScreenCallback captureScreenCallback) {
        mCaptureScreenCallback = captureScreenCallback;

        if (mActivity == null) {
            if (mCaptureScreenCallback != null) {
                mCaptureScreenCallback.captureFail("Activity object is null");
            }
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {//5.0(API20)之后才允许上层应用使用屏幕截图
            if (mCaptureScreenCallback != null) {
                mCaptureScreenCallback.applyPermissionFail();
            }
            return;
        }

        mProjectionManager = (MediaProjectionManager) mActivity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        //要使用截屏功能，必须先向系统申请截屏功能权限
        mActivity.startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
    }

    private void initRecorder(String path) {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mMediaRecorder.setVideoEncodingBitRate(6000000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);
        mMediaRecorder.setOutputFile(path);

        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            if (mCaptureScreenCallback != null) {
                mCaptureScreenCallback.captureFail("MediaRecorder prepare exception: " + e.toString());
            }
        }
    }

    public void recordScreen(int resultCode, Intent data) {
        LogUtil.i(TAG, "permission apply success, begin record screen");
        try {
            ((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(mDisplayMetrics);

            this.initRecorder(Environment.getExternalStorageDirectory().getPath() + "/" + "ssywbj.mp4");

            mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
            mVirtualDisplay = mMediaProjection.createVirtualDisplay(TAG, mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels, mDisplayMetrics.densityDpi,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mMediaRecorder.getSurface(), null, null);

            Thread.sleep(100);
            mMediaRecorder.start();
        } catch (Exception e) {
            if (mCaptureScreenCallback != null) {
                mCaptureScreenCallback.captureFail("init record objects fail, " + e.toString());
            }
        }
    }

    public void releaseResources() {
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
        }
        if (mMediaProjection != null) {
            mMediaProjection.stop();
        }
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
        }
    }

    public interface CaptureScreenCallback {
        /**
         * 申请权限失败：要使用截屏功能，必须先申请截屏功能权限
         */
        void applyPermissionFail();

        /**
         * 截屏失败
         */
        void captureFail(String failInfo);
    }

}
