import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class DeadlockDetector {

    private static final Object Lock1 = new Object();
    private static final Object Lock2 = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (Lock1) {
                System.out.println("Thread 1: Holding Lock1...");
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
                System.out.println("Thread 1: Waiting for Lock2...");
                synchronized (Lock2) {
                    System.out.println("Thread 1: Acquired Lock2!");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (Lock2) {
                System.out.println("Thread 2: Holding Lock2...");
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
                System.out.println("Thread 2: Waiting for Lock1...");
                synchronized (Lock1) {
                    System.out.println("Thread 2: Acquired Lock1!");
                }
            }
        });

        t1.start();
        t2.start();
        new Thread(() -> {
            ThreadMXBean mbean = ManagementFactory.getThreadMXBean();

            while (true) {
                try {
                    long[] deadlockedThreads = mbean.findDeadlockedThreads();
                    if (deadlockedThreads != null) {
                        System.out.println("\n⚠️ Deadlock detected!");
                        ThreadInfo[] threadInfos = mbean.getThreadInfo(deadlockedThreads);
                        for (ThreadInfo ti : threadInfos) {
                            System.out.println("Thread name: " + ti.getThreadName());
                            System.out.println("Blocked on: " + ti.getLockName());
                            System.out.println("Owner: " + ti.getLockOwnerName());
                        }
                        break;
                    }
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

