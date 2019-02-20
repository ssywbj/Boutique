package ying.jie.boutique.menu_framework;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created by wbj on 2016/8/26.
 */
public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));// 以("\n")为结尾分割的解码器
        pipeline.addLast("decoder", new StringDecoder());// 字符串解码
        pipeline.addLast("encoder", new StringEncoder());// 字符串编码
        pipeline.addLast("handler", new HelloServerHandler());// 自己的逻辑Handler
    }
}
