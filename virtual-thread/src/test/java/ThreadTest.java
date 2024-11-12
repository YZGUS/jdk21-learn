import org.junit.Test;

public class ThreadTest {

    @Test
    public void test() throws InterruptedException {
        final Thread thread = new Thread(() -> {
//            try {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("aaa");
                }
//            } catch (Throwable t) {
//                t.printStackTrace();
//            }
        });
        thread.start();

        Thread.sleep(10L);
        thread.interrupt();
    }
}
