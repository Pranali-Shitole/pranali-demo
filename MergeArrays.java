public import java.util.Scanner;

public class MergeArrays {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of elements in the first array: ");
        int n1 = scanner.nextInt();
        int[] arr1 = new int[n1];
        System.out.println("Enter " + n1 + " elements for the first array:");
        for (int i = 0; i < n1; i++) {
            arr1[i] = scanner.nextInt();
        }
        System.out.print("Enter the number of elements in the second array: ");
        int n2 = scanner.nextInt();
        int[] arr2 = new int[n2];
        System.out.println("Enter " + n2 + " elements for the second array:");
        for (int i = 0; i < n2; i++) {
            arr2[i] = scanner.nextInt();
        }

        int[] mergedArray = new int[n1 + n2];
        for (int i = 0; i < n1; i++) {
            mergedArray[i] = arr1[i];
        }
        for (int i = 0; i < n2; i++) {
            mergedArray[n1 + i] = arr2[i];
        }
        System.out.println("Merged array:");
        for (int num : mergedArray) {
            System.out.print(num + " ");
        }

        scanner.close();
    }
}
