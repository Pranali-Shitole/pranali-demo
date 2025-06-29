public class MaxHeap {
    private int[] heap;
    private int size;
    private final int capacity;

    public MaxHeap(int capacity) {
        this.capacity = capacity;
        heap = new int[capacity];
        size = 0;
    }

    private int parent(int i)  { return (i - 1) / 2; }
    private int left(int i)    { return 2 * i + 1; }
    private int right(int i)   { return 2 * i + 2; }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    public void insert(int key) {
        if (size == capacity)
            throw new IllegalStateException("Heap is full");

        int i = size;
        heap[i] = key;
        size++;
        while (i != 0 && heap[parent(i)] < heap[i]) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    public int extractMax() {
        if (size <= 0)
            throw new IllegalStateException("Heap is empty");
        if (size == 1)
            return heap[--size];

        int root = heap[0];
        heap[0] = heap[--size];
        maxHeapify(0);

        return root;
    }

    public int getMax() {
        if (size <= 0)
            throw new IllegalStateException("Heap is empty");
        return heap[0];
    }

    private void maxHeapify(int i) {
        int largest = i;
        int l = left(i);
        int r = right(i);

        if (l < size && heap[l] > heap[largest])
            largest = l;
        if (r < size && heap[r] > heap[largest])
            largest = r;

        if (largest != i) {
            swap(i, largest);
            maxHeapify(largest);
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
        MaxHeap maxHeap = new MaxHeap(10);

        maxHeap.insert(10);
        maxHeap.insert(20);
        maxHeap.insert(5);
        maxHeap.insert(30);
        maxHeap.insert(40);

        maxHeap.printHeap();

        System.out.println("Max value extracted: " + maxHeap.extractMax());
        maxHeap.printHeap();

        System.out.println("Current max: " + maxHeap.getMax());
    }
}

