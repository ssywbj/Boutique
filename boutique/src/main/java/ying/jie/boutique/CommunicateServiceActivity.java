package ying.jie.boutique;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import ying.jie.boutique.service.BindService;
import ying.jie.interfaces.IBindService;
import ying.jie.util.LogUtil;
import ying.jie.util.ToastUtil;

/**
 * Created by Wbj on 2016/3/21.
 */
public class CommunicateServiceActivity extends BasicActivity implements BindService.IServiceOut {
    private IBindService mIBindService;
    private boolean isOpenService;
    private TextView mTextUpdateUI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setActivityTitle(getIntent().getStringExtra(App.INT_ACT_TITLE));
    }

    /**
     * 返回Activity的布局文件资源
     */
    @Override
    public int getLayoutId() {
        return R.layout.communicate_service_aty;
    }

    /**
     * 为了编码的规范，此方法用于初始化变量
     */
    @Override
    public void initData() {
        mTextUpdateUI = (TextView) findViewById(R.id.text_update);
        mTextUpdateUI.setText(getString(R.string.service_update_ui, String.valueOf(0)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isOpenService){
            unbindService(mServiceConn);
            isOpenService = false;
        }
    }

    public void openService(View view) {
        this.openService();
    }

    public void openService() {
        if (!isOpenService) {
            Intent service = new Intent(this, BindService.class);
            bindService(service, mServiceConn, BIND_AUTO_CREATE);
            isOpenService = true;
        } else {
            ToastUtil.showToast(R.string.service_opened);
        }
    }

    public void getServiceData(View view) {
        if (!isOpenService) {
            this.openService();
            ToastUtil.showToast(R.string.opening_service);
            return;
        }

        ToastUtil.showToast(mIBindService.getName());
    }

    private ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.i(BindService.TAG, BindService.TAG + "::onServiceConnected(ComponentName, IBinder)");
            mIBindService = (IBindService) service;
            mIBindService.setServiceOut(CommunicateServiceActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.i(BindService.TAG, BindService.TAG + "::onServiceDisconnected(ComponentName)");
            mIBindService = null;
        }
    };

    @Override
    public void setCount(int count) {
        mTextUpdateUI.setText(getString(R.string.service_update_ui, String.valueOf(count)));
    }

}
