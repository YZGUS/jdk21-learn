package org.zengyi.handel.classic;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static java.nio.charset.StandardCharsets.UTF_8;


public class Client {

    public static void main(String[] args) {
        try {
            final String host = "127.0.0.1";
            final int port = 12345;
            final Socket socket = new Socket(host, port);
            final OutputStream out = socket.getOutputStream();
            out.write("你好  zy".getBytes(UTF_8));
            out.write("你好  zy v2".getBytes(UTF_8));
            socket.shutdownOutput(); // 关闭输入流，告诉服务端，客户端已经发送完毕了

            final InputStream in = socket.getInputStream();
            final byte[] bytes = new byte[1024];
            final StringBuffer sb = new StringBuffer();
            int len = 0;
            while ((len = in.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, len));
            }
            System.out.println("message: " + sb);

            in.close();
            out.close();
            socket.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
