import org.junit.Test;

import java.util.concurrent.locks.LockSupport;

public class LockSupportTest {


    // 请生成 LockSupport 测试用例
    @Test
    public void testLockSupport() {
        final Thread mainThread = Thread.currentThread();
        Thread thread1 = new Thread(() -> {
            System.out.println("Thread is waiting...");
            LockSupport.park(); //   阻塞当前线程
//            LockSupport.unpark(mainThread); // 唤醒main线程
//            mainThread.interrupt(); // 中断main线程
            System.out.println("Thread is resumed.");
        });
        thread1.start();

        System.out.println("thread1 unparked.");
        LockSupport.unpark(thread1); // 唤醒thread1线程

        System.out.println("main thread parked.");
//        LockSupport.parkNanos(10_000_000_000L); //  阻塞main线程
        LockSupport.parkUntil(System.currentTimeMillis() + 10_000);
        System.out.println("main thread resumed.");
    }


    @Test
    public void testLockSupport2() {
        Thread thread = new Thread(() -> {
            System.out.println("do something start");

            LockSupport.unpark(Thread.currentThread());
            System.out.println("unpark 1");
            LockSupport.unpark(Thread.currentThread());
            System.out.println("unpark 2");

            LockSupport.park();
            System.out.println("park 1");
            LockSupport.park();
            System.out.println("park 2");

            System.out.println("do something end");
        });
        thread.start();
    }
}
