package ying.jie.boutique.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import ying.jie.boutique.App;
import ying.jie.interfaces.IBindService;
import ying.jie.util.LogUtil;

/**
 * Created by Wbj on 2016/3/21.
 */
public class BindService extends Service implements App.HandlerCallback {
    public static final String TAG = BindService.class.getSimpleName();
    private static final int MSG_COUNT = 1;
    private App.AppHandler mHandler = new App.AppHandler(this);
    private IServiceOut mIServiceOut;
    private int mCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.i(TAG, TAG + "::onCreate()");

        mHandler.sendEmptyMessageDelayed(MSG_COUNT, 1000);
    }

    /**
     * 只有采用Context.bindService()方法启动服务时才会调用该方法
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.i(TAG, TAG + "::onBind(Intent)");
        return new ServiceBinder();
    }

    /**
     * 若只有采用Context.bindService()方法启动服务时，则不会调用该方法
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.i(TAG, TAG + "::onStartCommand(Intent, int, int)");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG, TAG + "::onDestroy()");
        mHandler.removeMessages(MSG_COUNT);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.i(TAG, TAG + "::onUnbind(Intent)");
        return super.onUnbind(intent);
    }

    private String getInfo() {
        return "data from service";
    }

    private void setServiceOut(IServiceOut iServiceOut) {
        mIServiceOut = iServiceOut;
    }

    @Override
    public void dispatchMessage(Message msg) {
        if (mIServiceOut == null) {
            return;
        }

        mIServiceOut.setCount(++mCount);
        mHandler.sendEmptyMessageDelayed(MSG_COUNT, 1000);
    }

    private class ServiceBinder extends Binder implements IBindService {
        @Override
        public String getName() {
            return BindService.this.getInfo();
        }

        @Override
        public void setServiceOut(IServiceOut iServiceOut) {
            BindService.this.setServiceOut(iServiceOut);
        }
    }

    public interface IServiceOut {
        void setCount(int count);
    }

}
