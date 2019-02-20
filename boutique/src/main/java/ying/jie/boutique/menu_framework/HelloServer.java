package ying.jie.boutique.menu_framework;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import ying.jie.util.LogUtil;

public class HelloServer {
    /**
     * 服务端监听的端口地址
     */
    private static final int portNumber = 7978;

    public static void startServer() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new HelloServerInitializer());

            ChannelFuture f = b.bind(portNumber).sync();// 服务器绑定端口监听
            f.channel().closeFuture().sync();// 监听服务器关闭监听
            /* b.bind(portNumber).sync().channel().closeFuture().sync(); */
        } catch (Exception e) {
            LogUtil.globalLogError(e.toString());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
