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
            // JAVA NIO客户端主动关闭连接，导致服务器空轮询: https://segmentfault.com/q/1010000010655743
            // Netty 对 Java NIO 空轮询问题的处理: https://blog.51cto.com/u_15459458/8673854
            if (socketChannel.read(buffer) == -1) {
                socketChannel.close();
                return;
            }

            // 注意事项, 可能会存在粘包 和 半包的情况: https://blog.csdn.net/p793049488/article/details/129943911
            buffer.flip();
            System.out.println("接受消息: " + new String(buffer.array(), 0, buffer.remaining()));
            socketChannel.write(ByteBuffer.wrap("已收到".getBytes()));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
