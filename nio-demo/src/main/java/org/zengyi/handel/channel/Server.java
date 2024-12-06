package org.zengyi.handel.channel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {

    public static void main(String[] args) {
        try (final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress(12345));
            final SocketChannel accept = serverSocketChannel.accept();
            System.out.println("Accepted connection from " + accept.getRemoteAddress());

            final ByteBuffer buffer = ByteBuffer.allocate(1024);
            accept.read(buffer);
            buffer.flip();
            System.out.println(new String(buffer.array(), 0, buffer.remaining()));

            accept.write(ByteBuffer.wrap("GodBoy".getBytes()));
            accept.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
