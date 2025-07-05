import java.util.concurrent.atomic.AtomicReference;

public class LockFreeQueue<T> {
    private static class Node<T> {
        final T value;
        final AtomicReference<Node<T>> next;

        Node(T value) {
            this.value = value;
            this.next = new AtomicReference<>(null);
        }
    }

    private final AtomicReference<Node<T>> head;
    private final AtomicReference<Node<T>> tail;

    public LockFreeQueue() {
        Node<T> dummy = new Node<>(null); 
        head = new AtomicReference<>(dummy);
        tail = new AtomicReference<>(dummy);
    }

    public void enqueue(T value) {
        Node<T> newNode = new Node<>(value);
        while (true) {
            Node<T> currentTail = tail.get();
            Node<T> tailNext = currentTail.next.get();

            if (currentTail == tail.get()) {
                if (tailNext != null) {
            
                    tail.compareAndSet(currentTail, tailNext);
                } else {
                
                    if (currentTail.next.compareAndSet(null, newNode)) {
                    
                        tail.compareAndSet(currentTail, newNode);
                        return;
                    }
                }
            }
        }
    }

    public T dequeue() {
        while (true) {
            Node<T> currentHead = head.get();
            Node<T> currentTail = tail.get();
            Node<T> headNext = currentHead.next.get();

            if (currentHead == head.get()) {
                if (currentHead == currentTail) {
                    if (headNext == null) {
                        return null; 
                    }
                    
                    tail.compareAndSet(currentTail, headNext);
                } else {
                    T value = headNext.value;
                    if (head.compareAndSet(currentHead, headNext)) {
                        return value;
                    }
                }
            }
        }
    }

    public boolean isEmpty() {
        return head.get().next.get() == null;
    }
    public static void main(String[] args) {
        LockFreeQueue<Integer> queue = new LockFreeQueue<>();

        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        System.out.println(queue.dequeue());
        System.out.println(queue.dequeue()); 
        System.out.println(queue.dequeue()); 
        System.out.println(queue.dequeue()); 
    }
}

