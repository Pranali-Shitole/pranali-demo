import java.util.Scanner;

public class FindDuplicates {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of elements: ");
        int n = scanner.nextInt();

        int[] arr = new int[n];
        System.out.println("Enter the elements:");
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }

        System.out.println("Duplicate elements in the array:");

        boolean hasDuplicates = false;
        for (int i = 0; i < n; i++) {
            boolean isDuplicate = false;
            for (int j = 0; j < i; j++) {
                if (arr[i] == arr[j]) {
                    isDuplicate = true; 
                    break;
                }
            }
            if (!isDuplicate) {
                for (int j = i + 1; j < n; j++) {
                    if (arr[i] == arr[j]) {
                        System.out.println(arr[i]);
                        hasDuplicates = true;
                        break;
                    }
                }
            }
        }

        if (!hasDuplicates) {
            System.out.println("No duplicates found.");
        }

        scanner.close();
    }
}

