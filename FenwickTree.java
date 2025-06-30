public class FenwickTree {
    private int[] tree;
    private int n;
    public FenwickTree(int size) {
        n = size;
        tree = new int[n + 1]; 
    }
    public FenwickTree(int[] arr) {
        n = arr.length;
        tree = new int[n + 1];
        for (int i = 0; i < n; i++) {
            update(i, arr[i]);
        }
    }
    public void update(int i, int val) {
        i++; 
        while (i <= n) {
            tree[i] += val;
            i += i & -i;
        }
    }
    public int prefixSum(int i) {
        i++; 
        int sum = 0;
        while (i > 0) {
            sum += tree[i];
            i -= i & -i;
        }
        return sum;
    }
    public int rangeSum(int l, int r) {
        return prefixSum(r) - prefixSum(l - 1);
    }
    public void printTree() {
        for (int i = 1; i <= n; i++) {
            System.out.print(tree[i] + " ");
        }
        System.out.println();
    }
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        FenwickTree ft = new FenwickTree(arr);

        System.out.println("Prefix sum of index 3: " + ft.prefixSum(3)); 
        System.out.println("Range sum [1, 3]: " + ft.rangeSum(1, 3));  

        ft.update(2, 5); 
        System.out.println("After update, prefix sum of index 3: " + ft.prefixSum(3)); 
    }
}

