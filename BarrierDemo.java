import java.util.concurrent.*;

public class BarrierDemo {

    private static final int NUM_THREADS = 4;

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(NUM_THREADS, () -> {
            System.out.println("All threads reached the barrier. Proceeding...");
        });
        for (int i = 0; i < NUM_THREADS; i++) {
            Thread t = new Thread(new Worker(i + 1, barrier));
            t.start();
        }
    }

    static class Worker implements Runnable {
        private final int id;
        private final CyclicBarrier barrier;

        public Worker(int id, CyclicBarrier barrier) {
            this.id = id;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                System.out.println("Thread " + id + " is working...");
                Thread.sleep((long)(Math.random() * 2000));

                System.out.println("Thread " + id + " is waiting at the barrier.");
                barrier.await();

                System.out.println("Thread " + id + " passed the barrier and continues.");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}

