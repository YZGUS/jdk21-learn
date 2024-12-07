package org.zengyi.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;

import java.io.FileInputStream;
import java.io.InputStream;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static java.lang.Integer.MAX_VALUE;
import static java.nio.charset.StandardCharsets.UTF_8;

public class NettyResourceServer {

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
                                .addLast(new HttpRequestDecoder())
                                .addLast(new HttpObjectAggregator(MAX_VALUE))
                                .addLast(defaultEventLoop, new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                        final FullHttpRequest req = (FullHttpRequest) msg;
                                        ctx.channel().writeAndFlush(PageResolver.INSTANCE.resolveResource(req.uri()));
                                        ctx.channel().close();
                                    }
                                })
                                .addLast(new HttpResponseEncoder());
                    }
                });
        bootstrap.bind(12345);
    }

    public static final class PageResolver {

        private static final String DEFAULT_FOLDER = "/Users/cengyi/Desktop/code/jdk21-learn/nio-demo/src/main/resources";

        private static final PageResolver INSTANCE = new PageResolver();

        public PageResolver() {
        }

        //请求路径给进来，接着我们需要将页面拿到，然后转换成响应数据包发回去
        public FullHttpResponse resolveResource(String path) {
            try {
                if (path.startsWith("/")) {
                    path = path.equals("/") ? "/index.html" : path;
                    // 分析为何读不到文件
//                    try (final InputStream stream = this.getClass().getClassLoader().getResourceAsStream(path)) {
                    try (final InputStream stream = new FileInputStream(DEFAULT_FOLDER + path)) {
                        if (stream != null) {
                            final byte[] bytes = new byte[stream.available()];
                            stream.read(bytes);
                            return packet(OK, bytes);
                        }
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return packet(NOT_FOUND, "404 NOT FOUND!".getBytes(UTF_8));
        }

        //包装成FullHttpResponse，把状态码和数据写进去
        private FullHttpResponse packet(HttpResponseStatus status, byte[] data) {
            final DefaultFullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status);
            response.content().writeBytes(data);
            return response;
        }
    }
}
