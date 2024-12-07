package org.zengyi.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

public class ByteBufDemo {

    @Test
    public void testInit() {
        final ByteBuf buf = Unpooled.buffer(16);
        buf.writeInt(-128);
        System.out.println("buf=" + Arrays.toString(buf.array()));

        final short readShort = buf.readShort();
        System.out.println(readShort);
        System.out.println("buf" + Arrays.toString(buf.array()));

        buf.discardReadBytes();
        System.out.println("buf" + Arrays.toString(buf.array()));


        final ByteBuf buf2 = Unpooled.wrappedBuffer("test".getBytes()); // 这样创建的 maxCapacity 是初始数组的大小
        System.out.println("buf2=" + Arrays.toString(buf2.array()));

        buf2.clear();
        buf2.writeInt(333);
        System.out.println("buf2=" + Arrays.toString(buf2.array()));


        final byte readByte = buf2.readByte();
        System.out.println("readByte=" + readByte);

        final ByteBuf slice = buf2.slice();
        System.out.println("offset=" + slice.arrayOffset());
        System.out.println("slice=" + Arrays.toString(slice.array()));
    }

    @Test
    public void testResize() {
        final ByteBuf buf = Unpooled.buffer(8); // 最大容量为 Integer.MAX_VALUE
        buf.writeCharSequence("测试字符串", UTF_8); //  64 <= newCapacity is a power of 2 <= threshold
        System.out.println("buf=" + Arrays.toString(buf.array()));
        System.out.println("buf.capacity=" + buf.capacity());
    }

    @Test
    public void testDirectBuf() {
        final ByteBuf buf = Unpooled.directBuffer(8);
        // java.lang.UnsupportedOperationException: direct buffer
        // System.out.println("buf=" + Arrays.toString(buf.array()));
        buf.writeCharSequence("123456789", UTF_8);
        System.out.println("buf=" + buf.readCharSequence(9, UTF_8));
    }

    @Test
    public void testCompositeBuffer() {
        //创建一个复合缓冲区
        CompositeByteBuf buf = Unpooled.compositeBuffer();
        buf.addComponent(Unpooled.copiedBuffer("abc".getBytes()));
        buf.addComponent(Unpooled.copiedBuffer("def".getBytes()));

        //  System.out.println("buf=" + Arrays.toString(buf.array())); // 不支持
        for (int i = 0; i < buf.capacity(); i++) {
            System.out.println((char) buf.getByte(i));
        }
    }

    @Test
    public void testPoolBuf() {
        final PooledByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;
        final ByteBuf buf = allocator.directBuffer(8);
        buf.writeCharSequence("1234567890", UTF_8);
        System.out.println("buf=" + buf.readCharSequence(16, UTF_8));

        buf.release(); // 将 buf 放回池子中, 下次可能重新拿到
        final ByteBuf buf2 = allocator.buffer(10);
        System.out.println(buf == buf2);
    }
}
