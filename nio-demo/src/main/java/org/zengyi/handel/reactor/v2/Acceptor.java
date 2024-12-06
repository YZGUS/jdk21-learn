package org.zengyi.handel.reactor.v2;

import org.zengyi.handel.reactor.Handler;

import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import static java.nio.channels.SelectionKey.OP_READ;

public class Acceptor implements Runnable {

    private final ServerSocketChannel serverSocketChannel;

    public Acceptor(ServerSocketChannel serverSocketChannel) {
        this.serverSocketChannel = serverSocketChannel;
    }

    @Override
    public void run() {
        try {
            final SocketChannel socketChannel = serverSocketChannel.accept();
            System.out.println("已接收请求, 客户端地址: " + socketChannel.getRemoteAddress());

            socketChannel.configureBlocking(false);

            final Selector selector = SubReactor.nextSelector();
            selector.wakeup(); // 唤醒selector
            socketChannel.register(selector, OP_READ, new Handler(socketChannel));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
