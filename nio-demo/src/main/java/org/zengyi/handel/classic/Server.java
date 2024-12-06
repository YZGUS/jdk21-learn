package org.zengyi.handel.classic;


import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        // 参考: https://www.cnblogs.com/yiwangzhibujian/p/7107785.html
        try {
            final int port = 12345;
            final ServerSocket server = new ServerSocket(port);
            System.out.println("等待连接...");

            final Socket socket = server.accept();
            final InputStream in = socket.getInputStream();
            final byte[] bytes = new byte[1024];
            final StringBuffer sb = new StringBuffer();
            int len = 0;
            while ((len = in.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, len));
            }
            System.out.println("message: " + sb);

            final OutputStream out = socket.getOutputStream();
            out.write("GoodBye!".getBytes());
            socket.shutdownOutput();

            out.close();
            in.close();
            socket.close();
            server.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}