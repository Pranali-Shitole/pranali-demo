import java.util.concurrent.*;

public class ProducerConsumerDemo {
    private static final int CAPACITY = 5;
    private static final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(CAPACITY);
    static class Producer implements Runnable {
        @Override
        public void run() {
            int item = 0;
            try {
                while (true) {
                    Thread.sleep(1000); 
                    queue.put(item); 
                    System.out.println("Produced: " + item);
                    item++;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Producer interrupted.");
            }
        }
    }
    static class Consumer implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    Integer item = queue.take(); 
                    System.out.println("Consumed: " + item);
                    Thread.sleep(1500); 
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Consumer interrupted.");
            }
        }
    }

    public static void main(String[] args) {
        Thread producerThread = new Thread(new Producer());
        Thread consumerThread = new Thread(new Consumer());

        producerThread.start();
        consumerThread.start();
    }
}
