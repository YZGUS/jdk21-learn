package org.zengyi.handel.reactor;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        final ByteBuffer buffer = ByteBuffer.allocate(1024);
        try (final SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(12345));
             final Scanner scanner = new Scanner(System.in)) {
            System.out.println("已经连接到服务器");
            while (true) {
                System.out.println("请输入要发送的消息：");
                // 当输入回车时, 是 nextLine 卡柱, 需要输入 "\n"
                final String nextLine = scanner.nextLine();
                socketChannel.write(ByteBuffer.wrap(nextLine.getBytes()));

                socketChannel.read(buffer);
                buffer.flip();
                System.out.println("收到服务器的消息：" + new String(buffer.array(), 0, buffer.remaining()));
                buffer.clear();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

