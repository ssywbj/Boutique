package ying.jie.boutique.menu_framework;

import android.os.NetworkOnMainThreadException;

import org.json.JSONObject;

import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ConnectTimeoutException;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import ying.jie.entity.NettyRequest;
import ying.jie.entity.NettyResponse;
import ying.jie.util.GsonUtil;
import ying.jie.util.LogUtil;

public final class NettyClient implements Runnable {
    public static final String TAG = NettyClient.class.getSimpleName();
    private static String HOST = "192.168.23.1";
    private static int PORT = 7978;
    private static NettyClient mNettyClient = new NettyClient();

    private EventLoopGroup mEventLoopGroup;
    private Channel mDataChannel;

    private NettyClient() {
        mEventLoopGroup = new NioEventLoopGroup();
    }

    public static NettyClient getInstance() {
        if (mNettyClient == null) {
            mNettyClient = new NettyClient();
        }
        return mNettyClient;
    }

    @Override
    public void run() {
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(mEventLoopGroup).channel(NioSocketChannel.class)
                    .handler(new ClientChannelInitializer());
            mDataChannel = bootstrap.connect(HOST, PORT).sync().channel();// 连接服务端
        } catch (Exception e) {
            NettyResponse nettyResponse = new NettyResponse();
            nettyResponse.code = 404;
            nettyResponse.order = NettyHandler.SOCKET_DATA_CONNECT;

            LogUtil.e(TAG, e.toString());
            if (e instanceof NetworkOnMainThreadException) {//在主线程中进行连网操作
                nettyResponse.msg = "Connect in main thread";
            }
            if (e instanceof ConnectException) {
                nettyResponse.msg = "Network is unreachable";
                /*
                连接超时：因为ConnectTimeoutException是ConnectException的子类，所以需要在ConnectException这个异常大类里判断属于它的子类异常；如果写成
                两个并排的if判断，因为ConnectException和ConnectTimeoutException都符合“e instanceof ConnectException”，程序向下执行后以最后一个出现的为准，
                那么这样得到的异常信息就不够具体了！
                */
                if (e instanceof ConnectTimeoutException) {
                    nettyResponse.msg = "Connect timeout";
                }
            }

            nettyResponse.tmpData = "connect socket server fail";
            EventBus.getDefault().post(nettyResponse);
        }
    }

    /**
     * 连接Netty服务器
     * <p/>
     * 如果是测试环境下，保证连接成功的条件：客户端和服务端在同一个网关
     */
    public void connectServer() {
        if (mDataChannel == null) {
            new Thread(this).start();
        }
    }

    public void sendData(NettyRequest nettyEntity) {
        if (mDataChannel == null) {
            return;
        }

        mDataChannel.writeAndFlush(GsonUtil.getInstance().objectToJson(nettyEntity) + "\n");// 向服务器写入数据，因为服务器设置了以"\n"为结尾分割的解码器，所以客户端发送数据时一定要以"\n"结尾，不然服务器无法解码出数据
    }

    public void disconnectServer() {
        if (mNettyClient == null) {
            return;
        }

        if (mDataChannel != null) {
            mDataChannel.close();
            mDataChannel = null;
        }

        if (mEventLoopGroup != null) {
            mEventLoopGroup.shutdownGracefully();// The connection is closed automatically on shutdown.
            mEventLoopGroup = null;
        }

        mNettyClient = null;
    }

    private final class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            //这个地方的 必须和服务端对应上。否则无法正常解码和编码
            pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192,
                    Delimiters.lineDelimiter()));
            pipeline.addLast("decoder", new StringDecoder());
            pipeline.addLast("encoder", new StringEncoder());

            pipeline.addLast("handler", new NettyHandler());// 客户端的逻辑
        }
    }

}
