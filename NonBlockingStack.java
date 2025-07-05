import java.util.concurrent.atomic.AtomicReference;

public class NonBlockingStack<T> {
    private static class Node<T> {
        final T value;
        final Node<T> next;

        Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }
    }

    private final AtomicReference<Node<T>> top = new AtomicReference<>(null);

    public void push(T value) {
        Node<T> newNode;
        Node<T> oldTop;
        do {
            oldTop = top.get();
            newNode = new Node<>(value, oldTop);
        } while (!top.compareAndSet(oldTop, newNode));
    }

    public T pop() {
        Node<T> oldTop;
        Node<T> newTop;
        do {
            oldTop = top.get();
            if (oldTop == null) {
                return null; 
            }
            newTop = oldTop.next;
        } while (!top.compareAndSet(oldTop, newTop));
        return oldTop.value;
    }

    public boolean isEmpty() {
        return top.get() == null;
    }
    public static void main(String[] args) {
        NonBlockingStack<Integer> stack = new NonBlockingStack<>();

    
        stack.push(10);
        stack.push(20);
        stack.push(30);
        System.out.println(stack.pop()); 
        System.out.println(stack.pop()); 
        System.out.println(stack.pop());
        System.out.println(stack.pop()); 
    }
}

