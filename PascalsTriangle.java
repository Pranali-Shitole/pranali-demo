import java.util.Scanner;

public class PascalsTriangle {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of rows: ");
        int rows = scanner.nextInt();

        for (int i = 0; i < rows; i++) {
            for (int space = 0; space < rows - i - 1; space++) {
                System.out.print("  ");
            }

            int number = 1;
            for (int j = 0; j <= i; j++) {
                System.out.printf("%4d", number);
                number = number * (i - j) / (j + 1);
            }

            System.out.println();
        }

        scanner.close();
    }
}

