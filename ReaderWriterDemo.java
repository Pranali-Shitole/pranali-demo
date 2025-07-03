import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReaderWriterDemo {
    static class SharedData {
        private String data = "Initial Data";
        private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

        public void read(int readerId) {
            lock.readLock().lock();
            try {
                System.out.println("Reader " + readerId + " reading: " + data);
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("Reader " + readerId + " finished reading.");
                lock.readLock().unlock();
            }
        }

        public void write(String newData, int writerId) {
            lock.writeLock().lock();
            try {
                System.out.println("Writer " + writerId + " writing: " + newData);
                Thread.sleep(1000);
                this.data = newData;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("Writer " + writerId + " finished writing.");
                lock.writeLock().unlock();
            }
        }
    }
    static class Reader implements Runnable {
        private final SharedData data;
        private final int id;

        public Reader(SharedData data, int id) {
            this.data = data;
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                data.read(id);
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
    static class Writer implements Runnable {
        private final SharedData data;
        private final int id;
        private int writeCount = 1;

        public Writer(SharedData data, int id) {
            this.data = data;
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                String newData = "Data by Writer " + id + " #" + writeCount++;
                data.write(newData, id);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    public static void main(String[] args) {
        SharedData sharedData = new SharedData();
        for (int i = 1; i <= 3; i++) {
            new Thread(new Reader(sharedData, i)).start();
        }
        for (int i = 1; i <= 2; i++) {
            new Thread(new Writer(sharedData, i)).start();
        }
    }
}

