import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Actor {
    private final BlockingQueue<Object> mailbox = new LinkedBlockingQueue<>();
    private final Thread worker;

    public Actor() {
        worker = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Object message = mailbox.take();
                    onReceive(message);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); 
            }
        });
        worker.start();
    }

    public void send(Object message) {
        mailbox.offer(message);
    }

    protected abstract void onReceive(Object message);

    public void stop() {
        worker.interrupt();
    }
}

