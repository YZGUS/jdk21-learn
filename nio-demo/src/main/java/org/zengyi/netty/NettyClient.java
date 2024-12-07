package org.zengyi.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

public class NettyClient {

    public static void main(String[] args) {
        try {
            final Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(new NioEventLoopGroup())
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    final ByteBuf buf = (ByteBuf) msg;
                                    System.out.println(">> 接受消息：" + buf.toString(UTF_8));
                                }
                            });
                        }
                    });

            final Channel channel = bootstrap.connect("127.0.0.1", 12345).channel();
            try (final Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    System.out.println("<< 请输入要发送给服务端的内容：");
                    String text = scanner.nextLine();
                    if (text.isEmpty()) continue;
                    channel.writeAndFlush(Unpooled.wrappedBuffer(text.getBytes(UTF_8)));  //通过Channel对象发送数据
                }
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
