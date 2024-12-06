package org.zengyi.handel.channel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try (final SocketChannel channel = SocketChannel.open(new InetSocketAddress("localhost", 12345));
             final Scanner scanner = new Scanner(System.in)) {
            System.out.println("已经连接到服务器");
            System.out.println("请输入要发送的消息：");

//            final String message = scanner.nextLine();
            channel.write(ByteBuffer.wrap("message".getBytes())); // 怎么发送多条消息？
            channel.write(ByteBuffer.wrap("message2".getBytes()));
//            channel.write(ByteBuffer.wrap("message3".getBytes()));

            final ByteBuffer buffer = ByteBuffer.allocate(1024);
            channel.read(buffer);
            buffer.flip();
            System.out.println("收到服务器的消息：" + new String(buffer.array(), 0, buffer.remaining()));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
