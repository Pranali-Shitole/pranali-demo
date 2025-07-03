import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelQuickSort extends RecursiveAction {
    private final int[] array;
    private final int left;
    private final int right;
    private static final int THRESHOLD = 1000;

    public ParallelQuickSort(int[] array, int left, int right) {
        this.array = array;
        this.left = left;
        this.right = right;
    }

    @Override
    protected void compute() {
        if (right - left < THRESHOLD) {
            Arrays.sort(array, left, right + 1); 
        } else {
            int pivotIndex = partition(array, left, right);
            ParallelQuickSort leftTask = new ParallelQuickSort(array, left, pivotIndex - 1);
            ParallelQuickSort rightTask = new ParallelQuickSort(array, pivotIndex + 1, right);

            invokeAll(leftTask, rightTask);
        }
    }

    private int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high);
        return i + 1;
    }

    private void swap(int[] arr, int i, int j) {
        if (i != j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    public static void main(String[] args) {
        int size = 10_000_000;
        int[] array = new Random().ints(size, 0, 1000000).toArray();

        System.out.println("Starting parallel quick sort...");

        ForkJoinPool pool = new ForkJoinPool();
        long start = System.currentTimeMillis();

        pool.invoke(new ParallelQuickSort(array, 0, array.length - 1));

        long end = System.currentTimeMillis();
        System.out.println("Sorting completed in " + (end - start) + " ms");

        
    }
}

