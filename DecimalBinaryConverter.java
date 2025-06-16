import java.util.Scanner;

public class DecimalBinaryConverter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose conversion type:");
        System.out.println("1. Decimal to Binary");
        System.out.println("2. Binary to Decimal");
        System.out.print("Enter your choice (1 or 2): ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.print("Enter a decimal number: ");
                int decimal = scanner.nextInt();
                String binary = "";

                if (decimal == 0) {
                    binary = "0";
                } else {
                    while (decimal > 0) {
                        int remainder = decimal % 2;
                        binary = remainder + binary; 
                        decimal = decimal / 2;
                    }
                }

                System.out.println("Binary representation: " + binary);
                break;

            case 2:
                System.out.print("Enter a binary number: ");
                String binaryInput = scanner.next();

                int decimalResult = 0;
                int power = 0;

                for (int i = binaryInput.length() - 1; i >= 0; i--) {
                    char bit = binaryInput.charAt(i);
                    if (bit == '1') {
                        decimalResult += Math.pow(2, power);
                    } else if (bit != '0') {
                        System.out.println("Invalid binary number.");
                        return;
                    }
                    power++;
                }

                System.out.println("Decimal representation: " + decimalResult);
                break;

            default:
                System.out.println("Invalid choice.");
        }

        scanner.close();
    }
}

