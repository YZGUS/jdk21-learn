package org.zengyi.channel;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileChannelTest {

    public static void main(String[] args) {
        testFileChannelWirte();
        testFileChannelRead();
    }

    private static void testFileChannelWirte() {
        try (final RandomAccessFile randomAccessFile = new RandomAccessFile("test.txt", "rw");
             final FileChannel fileChannel = randomAccessFile.getChannel()) {
            final ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put("测试字符串".getBytes(UTF_8));
            buffer.flip();
            fileChannel.write(buffer);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static void testFileChannelRead() {
        try (final RandomAccessFile randomAccessFile = new RandomAccessFile("test.txt", "rw");
             final FileChannel fileChannel = randomAccessFile.getChannel()) {
            final ByteBuffer buffer = ByteBuffer.allocate(1024);
            fileChannel.read(buffer);
            System.out.println(new String(buffer.array(), 0, buffer.position()));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
