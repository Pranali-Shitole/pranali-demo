import java.util.*;
import java.util.concurrent.*;

public class CustomThreadPool {
    private static class Worker extends Thread {
        private final BlockingQueue<Runnable> taskQueue;

        public Worker(BlockingQueue<Runnable> queue) {
            this.taskQueue = queue;
        }

        public void run() {
            try {
                while (true) {
                    Runnable task = taskQueue.take(); 
                    if (task == POISON_PILL) {
                        break; 
                    }
                    task.run();
                }
            } catch (InterruptedException ignored) {}
        }
    }

    private final BlockingQueue<Runnable> taskQueue;
    private final List<Worker> workers;
    private final int nThreads;
    private static final Runnable POISON_PILL = () -> {};

    public CustomThreadPool(int nThreads) {
        this.nThreads = nThreads;
        this.taskQueue = new LinkedBlockingQueue<>();
        this.workers = new ArrayList<>();

        for (int i = 0; i < nThreads; i++) {
            Worker worker = new Worker(taskQueue);
            workers.add(worker);
            worker.start();
        }
    }

    public void submit(Runnable task) {
        try {
            taskQueue.put(task); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void shutdown() {
        for (int i = 0; i < nThreads; i++) {
            submit(POISON_PILL);
        }
    }
    public static void main(String[] args) {
        CustomThreadPool pool = new CustomThreadPool(3);
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            pool.submit(() -> {
                System.out.println("Executing Task " + taskId + " by " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000); 
                } catch (InterruptedException ignored) {}
            });
        }

        pool.shutdown();
    }
}

