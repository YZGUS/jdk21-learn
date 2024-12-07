package org.zengyi.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static java.lang.Thread.currentThread;
import static java.nio.charset.StandardCharsets.UTF_8;

public class MyChannelInBoundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("注册");
        throw new Exception("注册异常");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("取消注册");
        throw new Exception("取消注册异常");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("活跃");
        throw new Exception("活跃异常");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("处理读事件");
        final ByteBuf buf = (ByteBuf) msg;
        System.out.println(currentThread().getName() + ">> 接受到消息：" + buf.toString(UTF_8));
        ctx.writeAndFlush(Unpooled.wrappedBuffer("已收到!".getBytes(UTF_8)));
        ctx.fireChannelRead(msg); // 注意，注释改代码，将不会走到后面的 Handler
        throw new Exception("读事件异常");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("断开");
        throw new Exception("断开异常");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("读事件完毕");
        throw new Exception("读事件完毕异常");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        System.out.println("写事件改变");
        throw new Exception("写事件改变异常");
    }

    // 发生异常不会中断流程, 而是会触发 exceptionCaught 方法
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常捕获:" + cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        System.out.println("userEventTriggered");
    }
}
