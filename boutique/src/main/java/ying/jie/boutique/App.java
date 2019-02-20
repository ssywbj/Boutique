package ying.jie.boutique;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

import ying.jie.util.FileManager;
import ying.jie.util.LogUtil;

public class App {
    public static final String TAG = App.class.getSimpleName();
    public static final String INT_ACT_TITLE = "ActivityTitle";
    private static Context sContext;
    /**
     * 屏幕宽度
     */
    public static int screenWidth = 900;
    /**
     * 屏幕高度
     */
    public static int screenHeight = 900;
    /**
     * 屏幕密度
     */
    public static float screenDensity = 0;

    public static void setContext(Context context) {
        sContext = context;
    }

    public static Context getContext() {
        return sContext;
    }

    public static void onCreate(Context context) {
        // 获取屏幕可展示页面的宽高，即不包括底部导航栏(若存在)以及密度，一般开发中所说的屏幕宽高就是指这个宽高
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        screenDensity = displayMetrics.density;
        LogUtil.globalLogInfo("screenWidth::" + screenWidth + ", screenHeight::" + screenHeight
                + ", screenDensity::" + screenDensity);

        // 获取屏幕实际的宽高，即包括底部导航栏(若存在)
        DisplayMetrics outMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getRealMetrics(outMetrics);
        LogUtil.globalLogInfo("screenWidthReal::" + outMetrics.widthPixels + ", screenHeightReal::" + outMetrics.heightPixels);

        /*将dimens文件里的尺寸转换成像素，也就是我们程序里常用的数值：经测试getDimension、getDimensionPixelOffset
         和getDimensionPixelSize的功能差不多，都是获取某个dimen的值,如果是dp或sp为单位，将其乘以density；
         如果是px为单位,则不乘；getDimension函数返回的float，其它两个返回int。*/
        LogUtil.i(TAG, "dpdpdpdpdpdpdpdpdpdpdpdpdpdpdpdpdpdpdpdpdp");//单位为dp
        LogUtil.i(TAG, "getDimensionPixelOffset::" + context.getResources().getDimensionPixelOffset(R.dimen.video_view_height));
        LogUtil.i(TAG, "getDimension::" + context.getResources().getDimension(R.dimen.video_view_height));
        LogUtil.i(TAG, "getDimensionPixelSize::" + context.getResources().getDimensionPixelSize(R.dimen.video_view_height));
        LogUtil.i(TAG, "spspspspspspspspspspspspspspspspspspspspsp");//单位为sp
        LogUtil.i(TAG, "getDimensionPixelOffset::" + context.getResources().getDimensionPixelOffset(R.dimen.sp_dimen));
        LogUtil.i(TAG, "getDimension::" + context.getResources().getDimension(R.dimen.sp_dimen));
        LogUtil.i(TAG, "getDimensionPixelSize::" + context.getResources().getDimensionPixelSize(R.dimen.sp_dimen));
        LogUtil.i(TAG, "pxpxpxpxpxpxpxpxpxpxpxpxpxpxpxpxpxpxpxpxpx");//单位为px
        LogUtil.i(TAG, "getDimensionPixelOffset::" + context.getResources().getDimensionPixelOffset(R.dimen.px_dimen));
        LogUtil.i(TAG, "getDimension::" + context.getResources().getDimension(R.dimen.px_dimen));
        LogUtil.i(TAG, "getDimensionPixelSize::" + context.getResources().getDimensionPixelSize(R.dimen.px_dimen));

        FileManager.getFileManager().createRootDir();
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context context) throws Exception {
        Class<?> cla = Class.forName("com.android.internal.R$dimen");
        Object object = cla.newInstance();
        int tempHeight = Integer.parseInt(cla.getField("status_bar_height")
                .get(object).toString());
        int statusBarHeight = context.getResources().getDimensionPixelOffset(tempHeight);
        return statusBarHeight;
    }

    public static class AppHandler extends Handler {
        private WeakReference<HandlerCallback> mCallback;

        public AppHandler(HandlerCallback callback) {
            mCallback = new WeakReference<>(callback);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            HandlerCallback callback = mCallback.get();
            if (callback != null) {
                callback.dispatchMessage(msg);
            }
        }
    }

    public interface HandlerCallback {
        /**
         * 分发消息以刷新界面
         */
        void dispatchMessage(Message msg);
    }

}
