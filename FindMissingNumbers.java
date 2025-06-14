
public class FindMissingNumbers {
    public static void main(String[] args) {
        int[] arr = {1, 2, 4, 6, 3, 7, 8};
        int n = 8; 

        findMissingNumbers(arr, n);
    }

    public static void findMissingNumbers(int[] arr, int n) {
        boolean[] present = new boolean[n + 1]; 
        for (int num : arr) {
            if (num >= 1 && num <= n) {
                present[num] = true;
            }
        }

        System.out.println("Missing numbers:");
        for (int i = 1; i <= n; i++) {
            if (!present[i]) {
                System.out.print(i + " ");
            }
        }
    }
}

