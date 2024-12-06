package org.zengyi.handel.reactor;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Handler implements Runnable {

    private final SocketChannel socketChannel;

    public Handler(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        try {
            final ByteBuffer buffer = ByteBuffer.allocate(128);
            final int read = socketChannel.read(buffer);
            if (read == -1) { // 连接断开
                socketChannel.close();
                return;
            }

            buffer.flip();
            System.out.println("接受消息: " + new String(buffer.array(), 0, buffer.remaining()));
            socketChannel.write(ByteBuffer.wrap("已收到".getBytes()));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
