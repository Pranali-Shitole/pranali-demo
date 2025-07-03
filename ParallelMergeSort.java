import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelMergeSort extends RecursiveAction {

    private final int[] array;
    private final int left;
    private final int right;
    private static final int THRESHOLD = 1000; 

    public ParallelMergeSort(int[] array, int left, int right) {
        this.array = array;
        this.left = left;
        this.right = right;
    }

    @Override
    protected void compute() {
        if (right - left < THRESHOLD) {
            Arrays.sort(array, left, right + 1);
        } else {
            int mid = (left + right) / 2;
            ParallelMergeSort leftTask = new ParallelMergeSort(array, left, mid);
            ParallelMergeSort rightTask = new ParallelMergeSort(array, mid + 1, right);

            invokeAll(leftTask, rightTask);
            merge(array, left, mid, right);
        }
    }

    private void merge(int[] arr, int left, int mid, int right) {
        int[] temp = new int[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        while (i <= mid && j <= right) {
            temp[k++] = arr[i] <= arr[j] ? arr[i++] : arr[j++];
        }
        while (i <= mid) temp[k++] = arr[i++];
        while (j <= right) temp[k++] = arr[j++];

        System.arraycopy(temp, 0, arr, left, temp.length);
    }

    public static void main(String[] args) {
        int size = 10_000_000;
        int[] array = new Random().ints(size, 0, 1000000).toArray();

        System.out.println("Starting parallel merge sort...");

        ForkJoinPool pool = new ForkJoinPool();
        long startTime = System.currentTimeMillis();

        pool.invoke(new ParallelMergeSort(array, 0, array.length - 1));

        long endTime = System.currentTimeMillis();
        System.out.println("Sorting completed in " + (endTime - startTime) + " ms");
    }
}

