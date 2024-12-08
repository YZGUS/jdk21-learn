package org.zengyi.im;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.MultithreadEventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.netty.util.concurrent.GlobalEventExecutor.INSTANCE;

/**
 * 学习 Netty 基本使用
 */
public class IMServer {

    public static final Map<String, Channel> USER_MAP = new ConcurrentHashMap<>();

    public static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(INSTANCE);

    public static void start() {
        try {
            /**
             * 参考 {@link  MultithreadEventLoopGroup}, 当未指定线程大小时, 默认为 系统配置的 “io.netty.eventLoopThreads”
             * 或者 io.netty.availableProcessors/JVM 可用线程数 * 2
             */
            final NioEventLoopGroup boss = new NioEventLoopGroup(), worker = new NioEventLoopGroup();
            final ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class) // 用于创建 Channel 实例, 等价于 channelFactory
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 用于处理请求
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new HttpServerCodec()) // 设置 HTTP 编解码器
                                    .addLast(new ChunkedWriteHandler()) // 参考 https://cloud.tencent.com/developer/article/1152654
                                    .addLast(new WebSocketServerProtocolHandler("/")) // 设置 WebSocket 处理协议
                                    .addLast(new HttpObjectAggregator(1024 * 64)) // 聚合 HTTP 请求
                                    .addLast(new WebSocketHandler());
                        }
                    });
            bootstrap.bind(12345);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
