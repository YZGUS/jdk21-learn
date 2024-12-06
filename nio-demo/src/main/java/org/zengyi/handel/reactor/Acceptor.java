package org.zengyi.handel.reactor;

import org.zengyi.handel.selector.Server;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import static java.nio.channels.SelectionKey.OP_READ;

public class Acceptor implements Runnable {

    private final ServerSocketChannel serverSocketChannel;

    private final Selector selector;

    public Acceptor(ServerSocketChannel serverSocketChannel, Selector selector) {
        this.serverSocketChannel = serverSocketChannel;
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            final SocketChannel socketChannel = serverSocketChannel.accept();
            System.out.println("已接收请求, 客户端地址: " + socketChannel.getRemoteAddress());

            socketChannel.configureBlocking(false);
            socketChannel.register(selector, OP_READ, new Handler(socketChannel));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
