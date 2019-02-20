package ying.jie.boutique;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import ying.jie.boutique.service.EventBusService;
import ying.jie.entity.EventBusBase;
import ying.jie.entity.EventBusTime;
import ying.jie.entity.EventBusTimeTmp;
import ying.jie.util.ToastUtil;

/**
 * 利用EventBus实现Activity与Service通信
 */
public class EventBusActivity extends BasicActivity {
    private TextView mTextUpdateUI, mTextUpdateUITmp;
    private Intent mIntentEventBusService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setActivityTitle(getIntent().getStringExtra(App.INT_ACT_TITLE));
        EventBus.getDefault().register(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.event_bus_aty;
    }

    @Override
    public void initData() {
        findViewById(R.id.text_open_service).setOnClickListener(this);
        findViewById(R.id.text_aidl).setOnClickListener(this);

        mTextUpdateUI = (TextView) findViewById(R.id.text_update);
        mTextUpdateUI.setText(getString(R.string.service_update_ui, String.valueOf(0)));

        mTextUpdateUITmp = (TextView) findViewById(R.id.text_aidl);
        mTextUpdateUITmp.setText(getString(R.string.service_update_ui, String.valueOf(0)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);

        if (mIntentEventBusService != null) {
            stopService(mIntentEventBusService);
            mIntentEventBusService = null;
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.text_open_service) {
            this.openService();
        } else if (i == R.id.text_aidl) {
            if (mIntentEventBusService == null) {
                this.openService();
                ToastUtil.showToast(R.string.opening_service);
                return;
            }
        }
    }

    public void openService() {
        if (mIntentEventBusService == null) {
            mIntentEventBusService = new Intent(this, EventBusService.class);
            startService(mIntentEventBusService);
        } else {
            ToastUtil.showToast(R.string.service_opened);
        }
    }

    /**
     * 如果已经注册了EventBus，则必须要写此方法。该方法在主线程中执行，可用于更新界面
     */
    public void onEventMainThread(EventBusBase eventBusBase) {
        Log.i(mLogTag, "Looper.myLooper() == Looper.getMainLooper()-->" + (Looper.myLooper() == Looper.getMainLooper()));

        String text = getString(R.string.service_update_ui, eventBusBase.getCount() + "");
        if (eventBusBase instanceof EventBusTime) {
            mTextUpdateUI.setText(text);
        } else if (eventBusBase instanceof EventBusTimeTmp) {
            mTextUpdateUITmp.setText(text);
        }
    }

}
