package org.zengyi.buffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

public class IntBufferDemo {

    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
        test5();
    }

    private static void test5() {
        final ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.putInt(Integer.MAX_VALUE);
        System.out.println(buffer.remaining());
    }

    private static void test4() {
        final IntBuffer buffer = IntBuffer.allocate(10);
        final int[] ints = {1, 2, 3, 4};
        buffer.put(ints, 0, ints.length);

        System.out.println(Arrays.toString(buffer.array()));
        buffer.compact();
        System.out.println(Arrays.toString(buffer.array()));
    }

    private static void test3() {
        final IntBuffer buffer = IntBuffer.allocate(10);
        final int[] ints = {1, 2, 3, 4};
        buffer.put(ints, 0, 2);
        System.out.println(Arrays.toString(buffer.array()));
        System.out.println(buffer.get());
        System.out.println(buffer.get());
        buffer.put(999);
        System.out.println(Arrays.toString(buffer.array()));
    }

    private static void test2() {
        final IntBuffer buffer1 = IntBuffer.allocate(10);
        buffer1.put(new int[]{1, 2, 3, 4});

        final IntBuffer buffer2 = IntBuffer.allocate(10);
        buffer2.put(new int[]{5, 6, 7, 8});
        buffer2.flip();

        buffer1.put(buffer2);
        System.out.println(Arrays.toString(buffer1.array()));
        System.out.println(Arrays.toString(buffer2.array()));
    }

    private static void test1() {
        final IntBuffer buffer = IntBuffer.allocate(10);
        final int[] ints = {1, 2, 3, 4};
        buffer.put(ints, 1, 2);
        buffer.put(2, 10);
        buffer.put(3, 20);
        buffer.put(1, 30);
        System.out.println(Arrays.toString(buffer.array()));
    }
}
