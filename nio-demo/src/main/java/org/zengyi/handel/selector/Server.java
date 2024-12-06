package org.zengyi.handel.selector;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;

public class Server {

    public static void main(String[] args) {
        try (final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
             final Selector selector = Selector.open()) {
            serverSocketChannel.bind(new InetSocketAddress(12345));
            serverSocketChannel.configureBlocking(false); // 设置为非阻塞模式, 避免卡顿在 accept 方法
            serverSocketChannel.register(selector, OP_ACCEPT); // 注册到 selector 中, 监听 accept 事件
            while (true) {
                final int count = selector.select();// 阻塞等待事件发生
                System.out.println("count: " + count);

                final Set<SelectionKey> keys = selector.selectedKeys();
                final Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    final SelectionKey key = iterator.next();
                    if (key.isAcceptable()) {
                        final SocketChannel socketChannel = serverSocketChannel.accept();
                        System.out.println("客户端连接成功: " + socketChannel.getRemoteAddress());

                        socketChannel.configureBlocking(false); // 设置为非阻塞模式, 避免卡顿在 read 方法
                        socketChannel.register(selector, OP_READ); // 注册到 selector 中, 监听 read 事件
                    } else if (key.isReadable()) {
                        final SocketChannel channel = (SocketChannel) key.channel();
                        final ByteBuffer buffer = ByteBuffer.allocate(1024);
                        final int read = channel.read(buffer);
                        if (read == -1) {
                            channel.close();
                            continue;
                        }

                        buffer.flip();
                        System.out.println("收到消息: '" + new String(buffer.array(), 0, buffer.remaining()) + "'");
                        channel.write(ByteBuffer.wrap("已收到!".getBytes()));
                    }
                    iterator.remove();
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
