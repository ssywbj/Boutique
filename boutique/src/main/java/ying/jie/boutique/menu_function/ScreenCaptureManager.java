package ying.jie.boutique.menu_function;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.nio.ByteBuffer;

import ying.jie.util.LogUtil;

/**
 * 该截屏方法只试用于安卓5.0以后的版本，它的强大之处在于不仅可以截取如TextView、ImageView等显示静态图像的View，
 * 还可以截取如SurfaceView等时刻需要动态图像渲染的View，避免它们被截屏时出现黑漆漆的一片。
 * <p/>
 * 使用步骤：
 * 1.向系统申请权限，只能成功申请权限才可以进行截屏操作；
 * 2.实例化MediaProjection、ImageReader、VirtualDisplay等对象后休眠100到500毫秒，才可以进行截屏操作
 * 注：在Activity或Fragment的onActivityResult(int, int, Intent)收到申请权限结果的消息
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScreenCaptureManager implements Runnable {
    public static final String TAG = ScreenCaptureManager.class.getSimpleName();
    public static final int REQUEST_MEDIA_PROJECTION = 0x11;
    private static final int MSG_CAPTURE_FAIL = 0x12;
    private static final int MSG_CAPTURE_SUCCESSFUL = 0x13;
    private MediaProjectionManager mProjectionManager;
    private MediaProjection mMediaProjection;
    private ImageReader mImageReader;
    private VirtualDisplay mVirtualDisplay;
    private Activity mActivity;
    private CaptureScreenCallback mCaptureScreenCallback;
    private Intent mResultIntent;
    private boolean mIsFirstCapture = true;

    public ScreenCaptureManager(Activity activity) {
        mActivity = activity;
    }

    public void applyPermission(CaptureScreenCallback captureScreenCallback) {
        mCaptureScreenCallback = captureScreenCallback;

        if (mActivity == null) {
            this.sendCaptureFailMsg("Activity object is null");
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

    public void applyPermissionSuccessAndBeginCapture(int resultCode, Intent data) {
        mResultIntent = data;

        LogUtil.i(TAG, "permission apply success and begin capture screen");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        try {
            mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
            mImageReader = ImageReader.newInstance(displayMetrics.widthPixels, displayMetrics.heightPixels, PixelFormat.RGBA_8888, 2);
            mVirtualDisplay = mMediaProjection.createVirtualDisplay("BoutiqueApp", displayMetrics.widthPixels, displayMetrics.heightPixels, displayMetrics.densityDpi,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mImageReader.getSurface(), null, null);

            this.captureScreen();
        } catch (Exception e) {
            this.sendCaptureFailMsg("init capture objects fail, " + e.toString());
        }
    }

    public void captureScreen() {
        LogUtil.i(TAG, "capture screen");
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            if (mIsFirstCapture) {
                Thread.sleep(200);//第一次截屏时，先适当休眠一会，让VirtualDisplay完全展示好整个视图以避免“ImageReader.acquireLatestImage()”获取到的Image对象为空
                mIsFirstCapture = false;
            }

            Image image = mImageReader.acquireLatestImage();
            if (image == null) {
                this.sendCaptureFailMsg("The method of ImageReader.acquireLatestImage() return null object");
                return;
            }

            int width = image.getWidth();
            int height = image.getHeight();
            final Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();
            int pixelStride = planes[0].getPixelStride();
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
            image.close();

            Message msg = mHandler.obtainMessage();
            msg.what = MSG_CAPTURE_SUCCESSFUL;
            msg.obj = bitmap;
            mHandler.sendMessage(msg);
        } catch (Exception e) {
            this.sendCaptureFailMsg("file save fail-->" + e.toString());
        }
    }

    private void sendCaptureFailMsg(String failInfo) {
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_CAPTURE_FAIL;
        msg.obj = failInfo;
        mHandler.sendMessage(msg);
    }

    public void releaseResources() {
        if (mImageReader != null) {
            mImageReader.close();
        }
        if (mMediaProjection != null) {
            mMediaProjection.stop();
        }
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
        }
    }

    public boolean isHadPermission() {
        return mResultIntent == null ? false : true;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (mCaptureScreenCallback == null) {
                return;
            }
            switch (msg.what) {
                case MSG_CAPTURE_SUCCESSFUL:
                    mCaptureScreenCallback.captureSuccess((Bitmap) msg.obj);
                    break;
                case MSG_CAPTURE_FAIL:
                    mCaptureScreenCallback.captureFail((String) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    public interface CaptureScreenCallback {
        /**
         * 申请权限失败：要使用截屏功能，必须先申请截屏功能权限，5.0(API20)之后才允许上层应用使用屏幕截图
         */
        void applyPermissionFail();

        /**
         * 截屏失败
         *
         * @param failInfo 截屏失败返回的错误信息
         */
        void captureFail(String failInfo);

        /**
         * 截屏成功
         *
         * @param bitmap 获取的屏幕Bitmap
         */
        void captureSuccess(Bitmap bitmap);
    }
}
