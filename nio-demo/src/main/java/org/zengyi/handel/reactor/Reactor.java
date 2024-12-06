package org.zengyi.handel.reactor;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

import static java.nio.channels.SelectionKey.OP_ACCEPT;

public class Reactor implements Runnable, AutoCloseable {

    private final ServerSocketChannel serverSocketChannel;

    private final Selector selector;

    public Reactor() throws Throwable {
        this.serverSocketChannel = ServerSocketChannel.open();
        this.selector = Selector.open();
    }

    @Override
    public void run() {
        try {
            serverSocketChannel.bind(new InetSocketAddress(12345));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, OP_ACCEPT, new Acceptor(serverSocketChannel, selector));
            while (true) {
                final int selectedCnt = selector.select();
                System.out.println("监听到 " + selectedCnt + " 个事件");

                final Set<SelectionKey> keys = selector.selectedKeys();
                final Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    ((Runnable) iterator.next().attachment()).run();
                    // 移除已处理的事件: https://blog.csdn.net/weixin_65349299/article/details/122301441
                    iterator.remove();
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        serverSocketChannel.close();
        selector.close();
    }
}
