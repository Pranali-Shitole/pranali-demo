public class BoyerMoore {

    static final int ALPHABET_SIZE = 256;
    static void badCharHeuristic(String pattern, int[] badChar) {
        for (int i = 0; i < ALPHABET_SIZE; i++) {
            badChar[i] = -1;  
        }
        for (int i = 0; i < pattern.length(); i++) {
            badChar[pattern.charAt(i)] = i;
        }
    }
    static void search(String text, String pattern) {
        int m = pattern.length();
        int n = text.length();

        int[] badChar = new int[ALPHABET_SIZE];
        badCharHeuristic(pattern, badChar);

        int shift = 0;

        while (shift <= (n - m)) {
            int j = m - 1;
            while (j >= 0 && pattern.charAt(j) == text.charAt(shift + j)) {
                j--;
            }
            if (j < 0) {
                System.out.println("Pattern found at index " + shift);
                shift += (shift + m < n) ? m - badChar[text.charAt(shift + m)] : 1;

            } else
                shift += Math.max(1, j - badChar[text.charAt(shift + j)]);
            }
        }
    }
    public static void main(String[] args) {
        String text = "ABAAABCD";
        String pattern = "ABC";
        search(text, pattern);
    }
}

