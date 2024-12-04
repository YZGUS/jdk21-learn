package org.zengyi.buffer;

import jdk.internal.misc.Unsafe;

import java.nio.BufferUnderflowException;

public class HeapByteBuffer {

    private static final Unsafe UNSAFE = Unsafe.getUnsafe();
    //  https://zhuanlan.zhihu.com/p/648108631
    //  private static final ScopedMemoryAccess SCOPED_MEMORY_ACCESS = ScopedMemoryAccess.getScopedMemoryAccess();
    private final byte[] hb;
    private int mark = -1;
    private int position;
    private int limit;
    private int capacity;
    private int offset;

    public HeapByteBuffer(byte[] hb, int mark, int position, int limit, int capacity, int offset) {
        this.mark = mark;
        this.position = position;
        this.limit = limit;
        this.capacity = capacity;
        this.hb = hb;
        this.offset = offset;
    }

    public HeapByteBuffer(byte[] hb, int position, int limit, int capacity, int offset) {
        this(hb, -1, position, limit, capacity, offset);
    }

    public HeapByteBuffer flip() {
        limit = position;
        position = 0;
        mark = -1;
        return this;
    }

    public HeapByteBuffer duplicate() {
        return new HeapByteBuffer(hb, mark, position, limit, capacity, offset);
    }

    public HeapByteBuffer slice() {
        final int pos = position;
        final int lim = limit;
        final int rem = pos <= lim ? lim - pos : 0;
        return new HeapByteBuffer(hb, -1, 0, rem, rem, pos + offset);
    }

    public HeapByteBuffer mark() {
        mark = position;
        return this;
    }

    public HeapByteBuffer reset() {
        final int m = mark;
        if (m < 0) {
            throw new BufferUnderflowException();
        }
        position = m;
        return this;
    }

    public HeapByteBuffer clear() {
        position = 0;
        limit = capacity;
        mark = -1;
        return this;
    }

    public HeapByteBuffer rewind() {
        position = 0;
        mark = -1;
        return this;
    }

    public HeapByteBuffer compact() {
        final int pos = position;
        final int lim = limit;
        assert (pos < lim);
        final int rem = pos <= lim ? lim - pos : 0;
        System.arraycopy(hb, ix(pos), hb, 0, rem);
        position = rem;
        limit = capacity;
        mark = -1;
        return this;
    }

    public int remaining() {
        final int rem = limit - position;
        return rem > 0 ? rem : 0;
    }

    public byte get() {
        return hb[ix(nextGetIndex())];
    }

    private int nextGetIndex() {
        final int p = position;
        if (p > limit) {
            throw new BufferUnderflowException();
        }
        position = p + 1;
        return p;
    }

    private int ix(int i) {
        return i + offset;
    }

    public int getInt() {
        return UNSAFE.getIntUnaligned(hb, nextGetIndex(4), true);
    }

    private int nextGetIndex(int nb) {
        final int p = position;
        if (p > limit) {
            throw new BufferUnderflowException();
        }
        position = p + nb;
        return p;
    }

    public HeapByteBuffer put(byte b) {
        hb[ix(nextPutIndex())] = b;
        return this;
    }

    private int nextPutIndex() {
        final int p = position;
        if (p > limit) {
            throw new BufferUnderflowException();
        }
        position = p + 1;
        return p;
    }

    public HeapByteBuffer putInt(int x) {
        UNSAFE.putIntUnaligned(hb, nextPutIndex(4), x, true);
        return this;
    }

    private int nextPutIndex(int nb) {
        final int p = position;
        if (limit - p < nb) {
            throw new BufferUnderflowException();
        }
        position = p + nb;
        return p;
    }

    public static HeapByteBuffer allocate(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException();
        }
        return new HeapByteBuffer(new byte[capacity], 0, capacity, capacity, 0);
    }

    public static HeapByteBuffer wrap(byte[] array) {
        return wrap(array, 0, array.length);
    }

    public static HeapByteBuffer wrap(byte[] array, int offset, int length) {
        try {
            return new HeapByteBuffer(array, 0, length, length, offset);
        } catch (Throwable t) {
            throw new IndexOutOfBoundsException();
        }
    }
}
