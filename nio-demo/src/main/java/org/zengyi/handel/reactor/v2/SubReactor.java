package org.zengyi.handel.reactor.v2;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SubReactor implements Runnable, AutoCloseable {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(4);

    private static final SubReactor[] subReactors = new SubReactor[4];

    private static int index = 0;

    private final Selector selector;

    static {
        try {
            for (int i = 0; i < subReactors.length; i++) {
                subReactors[i] = new SubReactor();
                EXECUTOR_SERVICE.submit(subReactors[i]);

            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public SubReactor() throws Throwable {
        this.selector = Selector.open();
    }

    public static Selector nextSelector() {
        return subReactors[index++ % 4].selector;
    }

    @Override
    public void run() {
        try {
            while (true) {
                final int cnt = selector.select();
                System.out.println("监听到 " + cnt + " 个事件");

                final Set<SelectionKey> keys = selector.selectedKeys();
                final Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    ((Runnable) iterator.next().attachment()).run();
                    iterator.remove();
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        selector.close();
    }
}
