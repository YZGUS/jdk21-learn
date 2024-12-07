package org.zengyi.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.*;
import io.netty.handler.ipfilter.IpFilterRule;
import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.RuleBasedIpFilter;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import static java.lang.Thread.currentThread;
import static java.nio.charset.StandardCharsets.UTF_8;

public class NettyServerV2 {

    public static void main(String[] args) {
        final EventLoopGroup bossGroup = new NioEventLoopGroup(), workerGroup = new NioEventLoopGroup();
        final DefaultEventLoop defaultEventLoop = new DefaultEventLoop();
        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline()
                                .addLast(new FixedLengthFrameDecoder(16)) // 设置包为固定长度
                                .addLast(new LoggingHandler())// 打印日志
                                .addLast(new RuleBasedIpFilter(new IpFilterRule() { // IP 过滤器
                                    @Override
                                    public boolean matches(InetSocketAddress inetSocketAddress) {
                                        return inetSocketAddress.getAddress().equals("127.0.0.1");
                                    }

                                    @Override
                                    public IpFilterRuleType ruleType() {
                                        return IpFilterRuleType.REJECT;
                                    }
                                }))
                                // 监控空闲连接, 触发 user userEventTriggered, 当参数为 0 时代表禁用
                                .addLast(new IdleStateHandler(10, 10, 0))
//                                .addLast(new DelimiterBasedFrameDecoder(1024, wrappedBuffer("!".getBytes(UTF_8)))) // 设置固定分隔符
//                                .addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4))
                                // 设置开头的 4 个字段存出消息的长度, 需 Client 结合 LengthFieldPrepender
//                                .addLast(new LengthFieldPrepender(4))
//                                .addLast(new MyEncoder())
//                                .addLast(new MyDecoder())
                                .addLast(new MyCodec())
                                .addLast(defaultEventLoop, new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                        System.out.println(currentThread().getName() + ">> 接受到消息：" + msg);
                                        ctx.writeAndFlush("已收到!"); // OutboundHandler必须在前面
                                    }
                                });
                    }
                });
        final ChannelFuture channelFuture = bootstrap.bind(12345);
        // channelFuture.sync(); // 阻塞等待执行完成
        channelFuture.addListener(future -> System.out.println("服务器是否启动完成: " + future.isDone()));
    }

    // 底层本质是 ChannelInboundHandlerAdapter
    private static final class MyDecoder extends MessageToMessageDecoder<ByteBuf> {

        @Override
        protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
            final String msg = byteBuf.toString(UTF_8); // 拆分为三条消息
            list.add(msg + "1");
            list.add(msg + "2");
            list.add(msg + "3");
        }
    }

    // 底层本质是 ChannelOutboundHandlerAdapter
    private static final class MyEncoder extends MessageToMessageEncoder<String> {

        @Override
        protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
            out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(msg), UTF_8));
        }
    }

    // 注意: 与网络交互的一定是 ByteBuf!!!!
    private static final class MyCodec extends MessageToMessageCodec<ByteBuf, String> {
        @Override
        protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
            System.out.println("编码");
//            out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(msg), UTF_8));
            out.add(wrappedBuffer(msg.getBytes(UTF_8)));
        }

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
            System.out.println("解码");
            out.add(msg.toString(UTF_8));
        }
    }
}
