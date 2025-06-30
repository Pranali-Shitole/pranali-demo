import java.util.*;

public class FibonacciHeap {

    private Node min;
    private int n;

    private static class Node {
        int key;
        int degree;
        Node parent;
        Node child;
        Node left;
        Node right;
        boolean mark;

        Node(int key) {
            this.key = key;
            this.left = this;
            this.right = this;
        }
    }

    public boolean isEmpty() {
        return min == null;
    }

    public void insert(int key) {
        Node node = new Node(key);
        min = mergeLists(min, node);
        n++;
    }

    public int findMin() {
        if (min == null) throw new NoSuchElementException("Heap is empty");
        return min.key;
    }

    public int extractMin() {
        if (min == null) throw new NoSuchElementException("Heap is empty");

        Node oldMin = min;

        if (min.child != null) {
            Node child = min.child;
            do {
                Node next = child.right;
                child.parent = null;
                min = mergeLists(min, child);
                child = next;
            } while (child != min.child);
        }

        removeNodeFromList(min);
        n--;

        if (min == min.right) {
            min = null;
        } else {
            min = min.right;
            consolidate();
        }

        return oldMin.key;
    }

    private void consolidate() {
        int size = ((int) Math.floor(Math.log(n) / Math.log(2))) + 1;
        Node[] A = new Node[size];

        List<Node> rootList = new ArrayList<>();
        Node x = min;
        if (x != null) {
            do {
                rootList.add(x);
                x = x.right;
            } while (x != min);
        }

        for (Node w : rootList) {
            x = w;
            int d = x.degree;
            while (A[d] != null) {
                Node y = A[d];
                if (x.key > y.key) {
                    Node temp = x;
                    x = y;
                    y = temp;
                }
                link(y, x);
                A[d] = null;
                d++;
            }
            A[d] = x;
        }

        min = null;
        for (Node node : A) {
            if (node != null) {
                min = mergeLists(min, node);
            }
        }
    }

    private void link(Node y, Node x) {
        removeNodeFromList(y);
        y.left = y.right = y;
        x.child = mergeLists(x.child, y);
        y.parent = x;
        x.degree++;
        y.mark = false;
    }

    public void decreaseKey(Node x, int k) {
        if (k > x.key)
            throw new IllegalArgumentException("new key is greater than current key");

        x.key = k;
        Node y = x.parent;

        if (y != null && x.key < y.key) {
            cut(x, y);
            cascadingCut(y);
        }

        if (x.key < min.key)
            min = x;
    }

    private void cut(Node x, Node y) {
        removeNodeFromList(x);
        y.degree--;
        if (y.child == x)
            y.child = (x.right != x) ? x.right : null;

        x.parent = null;
        x.mark = false;
        min = mergeLists(min, x);
    }

    private void cascadingCut(Node y) {
        Node z = y.parent;
        if (z != null) {
            if (!y.mark)
                y.mark = true;
            else {
                cut(y, z);
                cascadingCut(z);
            }
        }
    }

    public void delete(Node x) {
        decreaseKey(x, Integer.MIN_VALUE);
        extractMin();
    }

    private Node mergeLists(Node a, Node b) {
        if (a == null) return b;
        if (b == null) return a;

        Node aRight = a.right;
        Node bLeft = b.left;

        a.right = b;
        b.left = a;
        aRight.left = bLeft;
        bLeft.right = aRight;

        return a.key < b.key ? a : b;
    }

    private void removeNodeFromList(Node node) {
        node.left.right = node.right;
        node.right.left = node.left;
    }
    public Node getMinNode() {
        return min;
    }

    public static void main(String[] args) {
        FibonacciHeap heap = new FibonacciHeap();
        heap.insert(3);
        heap.insert(1);
        heap.insert(4);
        heap.insert(2);

        System.out.println("Min: " + heap.findMin());
        System.out.println("Extracted Min: " + heap.extractMin());
        System.out.println("New Min: " + heap.findMin());
    }
}

