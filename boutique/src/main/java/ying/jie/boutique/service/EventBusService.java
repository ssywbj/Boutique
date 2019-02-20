package ying.jie.boutique.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import ying.jie.entity.EventBusBase;
import ying.jie.entity.EventBusTime;
import ying.jie.entity.EventBusTimeTmp;

/**
 * Created by Wbj on 2016/06/15.
 */
public class EventBusService extends Service {
    public static final String          TAG              = EventBusService.class.getSimpleName();
    private             Timer           mTimer           = new Timer();
    private             EventBusTime    mEventBusTime    = (EventBusTime) EventBusBase.getInstance(0);
    private             EventBusTimeTmp mEventBusTimeTmp = (EventBusTimeTmp) EventBusBase.getInstance(1);
    private             int             mCount           = 1;
    private             int             mCountTmp        = 1;

    @Override
    public void onCreate() {
        super.onCreate();

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mEventBusTime.setCount(mCount++);
                EventBus.getDefault().post(mEventBusTime);
            }
        }, 1000, 1000);

        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mEventBusTimeTmp.setCount(mCountTmp++);
                EventBus.getDefault().post(mEventBusTimeTmp);
            }
        }, 1000, 1000);
    }

    /**
     * 只有采用Context.bindService()方法启动服务时才会调用该方法
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
    }

}
