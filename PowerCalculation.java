import java.util.Scanner;

public class PowerCalculation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the base number: ");
        int base = scanner.nextInt();

        System.out.print("Enter the exponent (non-negative integer): ");
        int exponent = scanner.nextInt();

        long result = 1;

        if (exponent < 0) {
            System.out.println("Exponent should be a non-negative integer.");
        } else {
            for (int i = 1; i <= exponent; i++) {
                result *= base;
            }
            System.out.println(base + " raised to the power " + exponent + " is: " + result);
        }

        scanner.close();
    }
}

