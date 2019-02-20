package ying.jie.boutique;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import ying.jie.aidl.IOtherBoutique;
import ying.jie.aidl.Person;
import ying.jie.util.FileUtil;
import ying.jie.util.LogUtil;
import ying.jie.util.ToastUtil;

/**
 * Created by Wbj on 2016/3/18.
 */
public class AIDLActivity extends BasicActivity {
    private IOtherBoutique mAidlOtherBoutique;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtil.i(mLogTag, "onServiceConnected(ComponentName, IBinder)");
            mAidlOtherBoutique = IOtherBoutique.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.i(mLogTag, "onServiceDisconnected(ComponentName)");
            mAidlOtherBoutique = null;
        }
    };

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
        return R.layout.aidl_aty;
    }

    /**
     * 为了编码的规范，此方法用于初始化变量
     */
    @Override
    public void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle  = new Bundle();
        Intent service = new Intent();
        service.setComponent(new ComponentName("jie.ying.otherboutique",
                                               "jie.ying.otherboutique.service.AidlService"));
        service.putExtras(bundle);
        bindService(service, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConnection);
    }

    public void callAidl(View view) {
        try {
            LogUtil.i(mLogTag, "current Thread id = " + Thread.currentThread().getId());
            mAidlOtherBoutique.testAidl("Weibangjie");
        } catch (Exception e) {
            LogUtil.e(mLogTag, e.toString());
        }
    }

    public void transmitCustomData(View view) {
        try {
            LogUtil.i(mLogTag, "current Thread id = " + Thread.currentThread().getName());
            mAidlOtherBoutique.setPerson(new Person("Sushiying", 18));
        } catch (Exception e) {
            LogUtil.e(mLogTag, e.toString());
        }
    }

    public void getOtherAppContext(View view) {
        try {
            //通过createPackageContext方法可获取外部应用的上下文对象
            StringBuilder sb           = new StringBuilder();
            Context       otherContext = createPackageContext("jie.ying.otherboutique", Context.CONTEXT_IGNORE_SECURITY);
            byte[]        data         = FileUtil.readInputStream(otherContext.openFileInput("OtherBoutique.txt"));
            sb.append(new String(data)).append('\n');

            SharedPreferences sp = otherContext.getSharedPreferences("OtherBoutique", Context.MODE_PRIVATE);
            sb.append(sp.getString("OtherData", "default data"));

            ToastUtil.showToast(sb.toString());
        } catch (Exception e) {
            LogUtil.e(mLogTag, e.toString());
            ToastUtil.showToast("get other app context error");
        }
    }

}
