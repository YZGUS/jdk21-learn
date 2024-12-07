package org.zengyi.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;
import java.util.EventListener;

import static java.lang.Thread.currentThread;
import static java.nio.charset.StandardCharsets.UTF_8;

public class NettyServer {

    public static void main(String[] args) {
        final EventLoopGroup bossGroup = new NioEventLoopGroup(), workerGroup = new NioEventLoopGroup();
        final DefaultEventLoop defaultEventLoop = new DefaultEventLoop();
        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
//                .childHandler(new MyChannelInBoundHandler()); // 功能正常
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        // 使用 defaultEventLoop 将事件处理异步话, 避免阻塞
                        socketChannel.pipeline().addLast(defaultEventLoop, new ChannelInboundHandlerAdapter() {

                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                System.out.println("channelActive: " + ctx.channel().remoteAddress().toString());
                            }

                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                final ByteBuf buf = (ByteBuf) msg;
                                System.out.println(currentThread().getName() + ">> 接受到消息：" + buf.toString(UTF_8));
                                ctx.writeAndFlush(Unpooled.wrappedBuffer("已收到!".getBytes(UTF_8))); // OutboundHandler必须在前面
//                                ctx.channel().writeAndFlush(Unpooled.wrappedBuffer("已收到!".getBytes(UTF_8))); // 从整个 channel 从后向前遍历
                            }
                        });
//                                .addLast(new ChannelOutboundHandlerAdapter() {
//                                    @Override
//                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//                                        System.out.println("写事件: " + msg);
//                                        super.write(ctx, msg, promise);
//                                    }
//                                });
                    }
                });
        bootstrap.bind(12345);
    }
}
