public class MinHeap {
    private int[] heap;
    private int size;
    private final int capacity;

    public MinHeap(int capacity) {
        this.capacity = capacity;
        heap = new int[capacity];
        size = 0;
    }

    private int parent(int i)  { return (i - 1) / 2; }
    private int left(int i)    { return 2 * i + 1; }
    private int right(int i)   { return 2 * i + 2; }

    private void swap(int i, int j) {
        int tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }

    public void insert(int key) {
        if (size == capacity) {
            throw new IllegalStateException("Heap is full");
        }

        int i = size;
        heap[i] = key;
        size++;


        while (i != 0 && heap[parent(i)] > heap[i]) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    public int extractMin() {
        if (size <= 0)
            throw new IllegalStateException("Heap is empty");
        if (size == 1) {
            return heap[--size];
        }

        int root = heap[0];
        heap[0] = heap[--size];
        minHeapify(0);

        return root;
    }

    public int getMin() {
        if (size <= 0)
            throw new IllegalStateException("Heap is empty");
        return heap[0];
    }

    private void minHeapify(int i) {
        int smallest = i;
        int l = left(i);
        int r = right(i);

        if (l < size && heap[l] < heap[smallest])
            smallest = l;
        if (r < size && heap[r] < heap[smallest])
            smallest = r;

        if (smallest != i) {
            swap(i, smallest);
            minHeapify(smallest);
        }
    }

    public void printHeap() {
        System.out.print("Heap array: ");
        for (int i = 0; i < size; i++) {
            System.out.print(heap[i] + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        MinHeap minHeap = new MinHeap(10);

        minHeap.insert(3);
        minHeap.insert(2);
        minHeap.insert(15);
        minHeap.insert(5);
        minHeap.insert(4);
        minHeap.insert(45);

        minHeap.printHeap();

        System.out.println("Extracted min: " + minHeap.extractMin());
        minHeap.printHeap();

        System.out.println("Current min: " + minHeap.getMin());
    }
}

