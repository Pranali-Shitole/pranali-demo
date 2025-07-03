import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {
    static class Fork extends ReentrantLock {}
    static class Philosopher implements Runnable {
        private final int id;
        private final Fork leftFork;
        private final Fork rightFork;

        public Philosopher(int id, Fork leftFork, Fork rightFork) {
            this.id = id;
            this.leftFork = leftFork;
            this.rightFork = rightFork;
        }

        private void think() throws InterruptedException {
            System.out.println("Philosopher " + id + " is thinking.");
            Thread.sleep((long)(Math.random() * 1000));
        }

        private void eat() throws InterruptedException {
            System.out.println("Philosopher " + id + " is eating.");
            Thread.sleep((long)(Math.random() * 1000));
        }

        @Override
        public void run() {
            try {
                while (true) {
                    think();
                    Fork first = id % 2 == 0 ? leftFork : rightFork;
                    Fork second = id % 2 == 0 ? rightFork : leftFork;

                    first.lock();
                    try {
                        second.lock();
                        try {
                            eat();
                        } finally {
                            second.unlock();
                        }
                    } finally {
                        first.unlock();
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Philosopher " + id + " was interrupted.");
            }
        }
    }

    public static void main(String[] args) {
        int numPhilosophers = 5;
        Philosopher[] philosophers = new Philosopher[numPhilosophers];
        Fork[] forks = new Fork[numPhilosophers];
        for (int i = 0; i < numPhilosophers; i++) {
            forks[i] = new Fork();
        }
        for (int i = 0; i < numPhilosophers; i++) {
            Fork left = forks[i];
            Fork right = forks[(i + 1) % numPhilosophers];
            philosophers[i] = new Philosopher(i, left, right);
            new Thread(philosophers[i]).start();
        }
    }
}

