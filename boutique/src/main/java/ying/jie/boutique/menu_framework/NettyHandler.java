package ying.jie.boutique.menu_framework;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ying.jie.entity.NettyResponse;
import ying.jie.util.LogUtil;

public final class NettyHandler extends SimpleChannelInboundHandler<String> {
    public static final String SOCKET_DATA_CONNECT = "DataConnect";
    public static final String SOCKET_DATA_NORMAL = "DataNormal";
    public static final String SOCKET_DATA_BEAN = "DataBean";
    public static final String SOCKET_DATA_LIST = "DataList";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg)
            throws Exception {
        LogUtil.d(NettyClient.TAG, "server msg-->" + msg + ", thread name-->" + Thread.currentThread().getName());
        if (!msg.startsWith("{") || !msg.endsWith("}")) {
            LogUtil.d(NettyClient.TAG, "msg is not json format data!!");
            return;
        }

        NettyResponse nettyResponse = new NettyResponse();
        JSONObject jsonObject = new JSONObject(msg);
        if (jsonObject.has("order")) {
            nettyResponse.order = jsonObject.getString("order");
        }
        if (jsonObject.has("code")) {
            nettyResponse.code = jsonObject.getInt("code");
        }
        if (jsonObject.has("msg")) {
            nettyResponse.msg = jsonObject.getString("msg");
        }
        if (jsonObject.has("data")) {
            nettyResponse.tmpData = jsonObject.getString("data");
        }

        EventBus.getDefault().post(nettyResponse);
        LogUtil.d(NettyClient.TAG, "nettyResponse.toString()-->" + nettyResponse.toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LogUtil.d(NettyClient.TAG, "netty client active");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        LogUtil.d(NettyClient.TAG, "netty client close");
    }

}