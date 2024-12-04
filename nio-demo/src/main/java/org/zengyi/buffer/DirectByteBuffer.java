package org.zengyi.buffer;

import jdk.internal.misc.Unsafe;
import jdk.internal.misc.VM;
import jdk.internal.ref.Cleaner;

import java.lang.ref.Reference;
import java.nio.BufferOverflowException;

public class DirectByteBuffer {

    private static final Unsafe UNSAFE = Unsafe.getUnsafe();
    private final Cleaner cleaner;
    private final Object att;
    private int mark = -1;
    private int position;
    private int limit;
    private int capacity;
    private int offset;
    private long address;

    public DirectByteBuffer(int capacity) {
        this.mark = -1;
        this.position = 0;
        this.limit = capacity;
        this.capacity = capacity;
        this.offset = 0;

        final boolean pageAligned = VM.isDirectMemoryPageAligned();
        final int pageSize = UNSAFE.pageSize();
        final long size = Math.max(1, (long) capacity + (pageAligned ? pageSize : 0));
        Bits.reserveMemory(size, capacity); // 尝试预留缓存, 但并非分配

        long base;
        try {
            base = UNSAFE.allocateMemory(size);
        } catch (OutOfMemoryError x) {
            Bits.unreserveMemory(size, capacity);
            throw x;
        }

        UNSAFE.setMemory(base, size, (byte) 0); // 初始化
        if (pageAligned && (base % pageSize != 0)) {
            address = base + pageSize - (base & (pageSize - 1));
        } else {
            address = base;
        }

        try {
            cleaner = Cleaner.create(this, new Deallocator(address, size, capacity));
        } catch (Throwable t) {
            // Prevent leak if the Deallocator or Cleaner fail for any reason
            UNSAFE.freeMemory(base);
            Bits.unreserveMemory(size, capacity);
            throw t;
        }
        att = null;
    }


    public DirectByteBuffer put(byte b) {
        try {
            UNSAFE.putByte(ix(nextPutIndex()), b);
        } finally {
            Reference.reachabilityFence(this); // 作用？
        }
        return this;
    }

    final int nextPutIndex() {                          // package-private
        int p = position;
        if (p >= limit)
            throw new BufferOverflowException();
        position = p + 1;
        return p;
    }

    private long ix(int i) {
        return address + ((long) i << 0);
    }

    public DirectByteBuffer putInt(int i) {
        putInt(ix(nextPutIndex(4)), i);
        return this;
    }

    final int nextPutIndex(int nb) {                          // package-private
        int p = position;
        if (p >= limit)
            throw new BufferOverflowException();
        position = p + nb;
        return p;
    }

    private DirectByteBuffer putInt(long a, int x) {
        try {
            UNSAFE.putIntUnaligned(null, a, x, true);
        } finally {
            Reference.reachabilityFence(this);
        }
        return this;
    }


    public byte get() {
        try {
            return UNSAFE.getByte(null, ix(nextGetIndex()));
        } finally {
            Reference.reachabilityFence(this);
        }
    }

    final int nextGetIndex() {                          // package-private
        int p = position;
        if (p >= limit)
            throw new BufferOverflowException();
        position = p + 1;
        return p;
    }


    public static DirectByteBuffer allocator(int capacity) {
        return new DirectByteBuffer(capacity);
    }

    private record Deallocator(long address, long size, int capacity) implements Runnable {
        private Deallocator {
            assert address != 0;
        }

        public void run() {
            UNSAFE.freeMemory(address);
            Bits.unreserveMemory(size, capacity);
        }
    }
}
