package org.zengyi.handel.reactor;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HandlerV2 implements Runnable {

    //  线程池, 用于异步处理消息
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(8);

    private final SocketChannel socketChannel;

    public HandlerV2(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        try {
            final ByteBuffer buffer = ByteBuffer.allocate(128);
            socketChannel.read(buffer);
            buffer.flip();
            EXECUTOR_SERVICE.submit(() -> { //  异步处理消息
                try {
                    System.out.println("接受消息: " + new String(buffer.array(), 0, buffer.remaining()));
                    socketChannel.write(ByteBuffer.wrap("已收到".getBytes()));
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
