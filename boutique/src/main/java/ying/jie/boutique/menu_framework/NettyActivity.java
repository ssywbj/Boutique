package ying.jie.boutique.menu_framework;

import android.os.Bundle;
import android.view.View;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import ying.jie.boutique.App;
import ying.jie.boutique.BasicActivity;
import ying.jie.boutique.R;
import ying.jie.entity.GsonExpose;
import ying.jie.entity.NettyRequest;
import ying.jie.entity.NettyResponse;
import ying.jie.util.GsonUtil;
import ying.jie.util.LogUtil;
import ying.jie.util.ToastUtil;

public class NettyActivity extends BasicActivity {
    public static final String NETTY_TAG = NettyActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setActivityTitle(getIntent().getStringExtra(App.INT_ACT_TITLE));
        EventBus.getDefault().register(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.netty_aty;
    }

    @Override
    public void initData() {
        //HelloServer.startServer();
        findViewById(R.id.text_connect_netty).setOnClickListener(this);
        findViewById(R.id.text_send_data).setOnClickListener(this);
        findViewById(R.id.text_send_json_data).setOnClickListener(this);
        findViewById(R.id.text_disconnect_netty).setOnClickListener(this);
        findViewById(R.id.text_send_list_data).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.text_connect_netty) {
            NettyClient.getInstance().connectServer();
        } else if (i == R.id.text_send_data) {
            NettyClient.getInstance().sendData(new NettyRequest<>(NettyHandler.SOCKET_DATA_NORMAL, "心跳正常"));
        } else if (i == R.id.text_send_json_data) {
            NettyRequest<GsonExpose> nettyGsonUser = new NettyRequest<>(NettyHandler.SOCKET_DATA_BEAN, new GsonExpose(100, "明天", "Tomorrow", "哈佛大学"));
            NettyClient.getInstance().sendData(nettyGsonUser);
        } else if (i == R.id.text_send_list_data) {
            List<GsonExpose> gsonExposeList = new ArrayList<>();
            gsonExposeList.add(new GsonExpose(99, "昨天", "Yesterday", "北京大学"));
            gsonExposeList.add(new GsonExpose(100, "今天", "Today", "清华大学"));
            gsonExposeList.add(new GsonExpose(101, "明天", "Tomorrow", "哈佛大学"));
            NettyClient.getInstance().sendData(new NettyRequest<>(NettyHandler.SOCKET_DATA_LIST, gsonExposeList));
        } else if (i == R.id.text_disconnect_netty) {
            NettyClient.getInstance().disconnectServer();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        NettyClient.getInstance().disconnectServer();
    }

    public void onEventMainThread(NettyResponse nettyResponse) {
        String order = nettyResponse.order;
        ToastUtil.showToast(order);
        switch (nettyResponse.order) {
            case NettyHandler.SOCKET_DATA_CONNECT:
                if (nettyResponse.code == 200) {
                    LogUtil.d(NettyClient.TAG, nettyResponse.msg + ", " + getString(R.string.netty_connect_successful));
                } else {
                    LogUtil.e(NettyClient.TAG, nettyResponse.msg + ", " + getString(R.string.netty_connect_fail));
                }
                break;
            case NettyHandler.SOCKET_DATA_NORMAL:
                String normal = GsonUtil.getInstance().jsonToObject(nettyResponse.tmpData, String.class);
                LogUtil.d(NettyClient.TAG, "normal-->" + normal);
                break;
            case NettyHandler.SOCKET_DATA_BEAN:
                GsonExpose exposeObj = GsonUtil.getInstance().jsonToObject(nettyResponse.tmpData, GsonExpose.class);
                if (exposeObj == null) {
                    LogUtil.d(NettyClient.TAG, "object is null");
                    return;
                }
                LogUtil.d(NettyClient.TAG, "exposeObj=====" + exposeObj.toString());
                break;
            case NettyHandler.SOCKET_DATA_LIST:
                List<GsonExpose> gsonExposeList = GsonUtil.getInstance().jsonToObject(nettyResponse.tmpData, new TypeToken<List<GsonExpose>>() {
                }.getType());
                if (gsonExposeList == null) {
                    LogUtil.d(NettyClient.TAG, "list is null");
                    return;
                }
                for (GsonExpose gsonExpose : gsonExposeList) {
                    LogUtil.d(NettyClient.TAG, "gsonExpose---------->" + gsonExpose.toString());
                }
                break;
            default:
                break;
        }
    }

}
