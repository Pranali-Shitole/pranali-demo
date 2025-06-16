import java.util.Scanner;

public class reversestring1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a string: ");
        String input = scanner.nextLine();
        char[] chars = input.toCharArray();
        int length = chars.length;
        for (int i = 0; i < length / 2; i++) {
            char temp = chars[i];
            chars[i] = chars[length - 1 - i];
            chars[length - 1 - i] = temp;
        }
        String reversed = "";
        for (char c : chars) {
            reversed += c; 
        }

        System.out.println("Reversed string: " + reversed);

        scanner.close();
    }
}

