package org.zengyi.channel;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileChannelTest {

    public static void main(String[] args) {
//        testFileChannelWirte();
//        testFileChannelRead();
//        testFileChannelTruncate();
//        testFileChannelTransfer();
//        testFileChannelTransFrom();
//        testFileChannelMap();
        testFileChannelMap2();
    }

    private static void testFileChannelWrite() {
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

    private static void testFileChannelTruncate() {
        try (final RandomAccessFile randomAccessFile = new RandomAccessFile("test.txt", "rw");
             final FileChannel fileChannel = randomAccessFile.getChannel()) {
            fileChannel.truncate(20);

            final ByteBuffer buffer = ByteBuffer.allocate(128);
            fileChannel.read(buffer);
            buffer.flip();
            System.out.println(new String(buffer.array(), 0, buffer.limit()));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static void testFileChannelTransfer() {
        try (final FileInputStream inputStream = new FileInputStream("test1.txt");
             final FileOutputStream outputStream = new FileOutputStream("test2.txt")) {
            final FileChannel inChannel = inputStream.getChannel();
            inChannel.transferTo(3, inChannel.size() - 10, outputStream.getChannel());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static void testFileChannelTransFrom() {
        try (final FileInputStream inputStream = new FileInputStream("test1.txt");
             final FileOutputStream outputStream = new FileOutputStream("test2.txt")) {
            final FileChannel inChannel = inputStream.getChannel();
            outputStream.getChannel().transferFrom(inChannel, 3, inChannel.size() - 10);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static void testFileChannelMap() {
        try (final RandomAccessFile randomAccessFile = new RandomAccessFile("test.txt", "rw");
             final FileChannel fileChannel = randomAccessFile.getChannel()) {
            final MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileChannel.size());
            buffer.put("测试字符串".getBytes(UTF_8));
//            buffer.force(0, 5); ??? 没发现用处
            buffer.force();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static void testFileChannelMap2() {
        try {
            RandomAccessFile file = new RandomAccessFile("test.txt", "rw");
            FileChannel channel = file.getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, file.length());
            // 定义要更新的内容，这里简单地使用字符 'A'
            byte[] updateBytes = new byte[4];
            for (int i = 0; i < 4; i++) {
                updateBytes[i] = (byte) 'A';
            }
//            buffer.position(3);
            buffer.put(updateBytes);
            // 使用有参数的force方法将指定部分缓冲区内容写入磁盘
            buffer.force(3, 2);
            file.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Test
    public void testFileChannelLockA() {
        try (final RandomAccessFile randomAccessFile = new RandomAccessFile("test.txt", "rw");
             final FileChannel fileChannel = randomAccessFile.getChannel()) {
//            final FileLock lock = fileChannel.lock();
            // 锁文件的 0~6, 采用共享锁;
            //https://blog.csdn.net/wangbaochu/article/details/48546717
            final FileLock lock = fileChannel.lock(0, 6, true);
            Thread.sleep(10000);
            lock.release();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Test
    public void testFileChannelLockB() {
        try (final RandomAccessFile randomAccessFile = new RandomAccessFile("test.txt", "rw");
             final FileChannel fileChannel = randomAccessFile.getChannel()) {
//            final FileLock lock = fileChannel.lock();
            // 锁文件的 0~6, 采用共享锁; 共享锁允许
            final FileLock lock = fileChannel.lock(0, 6, true);
            fileChannel.write(ByteBuffer.wrap("测试".getBytes(UTF_8)));
            lock.release();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
