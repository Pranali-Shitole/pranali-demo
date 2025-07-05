public class VirtualThreadExample {
    public static void main(String[] args) throws InterruptedException {
        Runnable task = () -> {
            System.out.println("Running in: " + Thread.currentThread());
            try {
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Done: " + Thread.currentThread());
        };

        Thread vThread = Thread.startVirtualThread(task); 
        vThread.join();
    }
}

