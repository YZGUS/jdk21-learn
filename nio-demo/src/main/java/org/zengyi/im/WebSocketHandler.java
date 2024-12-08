package org.zengyi.im;

import com.alibaba.fastjson2.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.zengyi.im.model.CommandEnum;
import org.zengyi.im.model.Message;
import org.zengyi.im.model.MessageEnum;
import org.zengyi.im.model.Result;

import static org.zengyi.im.IMServer.CHANNEL_GROUP;
import static org.zengyi.im.IMServer.USER_MAP;
import static org.zengyi.im.model.Result.error;
import static org.zengyi.im.model.Result.success;

public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        try {
            final Message message = JSON.parseObject(msg.text(), Message.class);
            switch (CommandEnum.codeOf(message.getCommandType())) {
                case CONNECT -> handleConnect(ctx, message);
                case CHAT -> handleChat(ctx, message);
                case JOIN_GROUP -> handleJoinGroup(ctx, message);
                case DISCONNECT -> handleDisconnect(ctx, message);
                case ERROR -> ctx.channel().writeAndFlush(error("系统错误"));
            }
        } catch (Throwable t) {
            ctx.channel().writeAndFlush(error("系统错误"));
        }
    }

    private void handleConnect(ChannelHandlerContext ctx, Message message) {
        final String name = message.getName();
        if (USER_MAP.containsKey(name)) {
            ctx.channel().writeAndFlush(error("用户名已注册"));
            ctx.channel().disconnect();
            return;
        }

        USER_MAP.put(name, ctx.channel());
        ctx.channel().writeAndFlush(Result.success("链接成功"));
    }

    private void handleChat(ChannelHandlerContext ctx, Message message) {
        switch (MessageEnum.codeOf(message.getMessageType())) {
            case SINGLE -> handleSingleMessage(ctx, message);
            case GROUP -> handleGroupMessage(ctx, message);
            case ERROR -> ctx.channel().writeAndFlush(error("系统错误"));
        }
    }

    private void handleSingleMessage(ChannelHandlerContext ctx, Message message) {
        final Channel targetChannel = USER_MAP.get(message.getTarget());
        if (targetChannel == null || !targetChannel.isActive()) {
            ctx.channel().writeAndFlush(success("目标用户不在线"));
            return;
        }

        targetChannel.writeAndFlush(success(message.message()));
        ctx.channel().writeAndFlush(success("发送成功"));
    }

    private void handleGroupMessage(ChannelHandlerContext ctx, Message message) {
        CHANNEL_GROUP.writeAndFlush(success(message.message()));
        ctx.channel().writeAndFlush(success("发送成功"));
    }

    private void handleJoinGroup(ChannelHandlerContext ctx, Message message) {
        CHANNEL_GROUP.add(ctx.channel());
        ctx.channel().writeAndFlush(Result.success("加入成功"));
    }

    private void handleDisconnect(ChannelHandlerContext ctx, Message message) {
        CHANNEL_GROUP.remove(ctx.channel());
        USER_MAP.remove(message.getName());
    }
}
