package ying.jie.boutique.menu_framework;

import java.net.InetAddress;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ying.jie.util.LogUtil;

/**
 * Created by wbj on 2016/8/26.
 */
public class HelloServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        LogUtil.i(NettyActivity.NETTY_TAG, channelHandlerContext.channel().remoteAddress() + " Say : " + s);// 收到客户端的消息
        channelHandlerContext.writeAndFlush("Received your message !\n"); // 向客户端返回消息
    }

    /**
     * 覆盖 channelActive 方法 在channel被启用的时候触发 (在建立连接的时候)
     * channelActive 和 channelInActive 在后面的内容中讲述，这里先不做详细的描述
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LogUtil.i(NettyActivity.NETTY_TAG, "RemoteAddress : " + ctx.channel().remoteAddress() + " active !");
        ctx.writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n");
        super.channelActive(ctx);
    }

}
