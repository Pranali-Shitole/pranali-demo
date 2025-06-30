import java.util.*;

public class GoEngine {
    static final int SIZE = 9;
    static final char EMPTY = '.';
    static final char BLACK = 'B';
    static final char WHITE = 'W';

    static char[][] board = new char[SIZE][SIZE];
    static boolean[][] visited;

    public static void main(String[] args) {
        initBoard();
        Scanner scanner = new Scanner(System.in);
        char currentPlayer = BLACK;

        while (true) {
            printBoard();
            System.out.println((currentPlayer == BLACK ? "Black" : "White") + "'s move (e.g., d4), or type 'pass' or 'exit': ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("exit")) break;
            if (input.equals("pass")) {
                currentPlayer = (currentPlayer == BLACK) ? WHITE : BLACK;
                continue;
            }

            if (!input.matches("[a-i][1-9]")) {
                System.out.println("Invalid move format.");
                continue;
            }

            int x = input.charAt(0) - 'a';
            int y = SIZE - Character.getNumericValue(input.charAt(1));

            if (!inBounds(x, y) || board[y][x] != EMPTY) {
                System.out.println("Illegal move.");
                continue;
            }

            board[y][x] = currentPlayer;
            removeCapturedStones(opposite(currentPlayer));
            if (!hasLiberty(y, x, currentPlayer)) {
                System.out.println("Suicide move. Reverting.");
                board[y][x] = EMPTY;
                continue;
            }

            currentPlayer = opposite(currentPlayer);
        }

        scanner.close();
    }

    static void initBoard() {
        for (int i = 0; i < SIZE; i++) {
            Arrays.fill(board[i], EMPTY);
        }
    }

    static void printBoard() {
        System.out.print("  ");
        for (char c = 'a'; c < 'a' + SIZE; c++) {
            System.out.print(c + " ");
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print((SIZE - i) + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println((SIZE - i));
        }
        System.out.print("  ");
        for (char c = 'a'; c < 'a' + SIZE; c++) {
            System.out.print(c + " ");
        }
        System.out.println("\n");
    }

    static boolean inBounds(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
    }

    static char opposite(char player) {
        return player == BLACK ? WHITE : BLACK;
    }

    static boolean hasLiberty(int y, int x, char color) {
        visited = new boolean[SIZE][SIZE];
        return dfsCheckLiberty(y, x, color);
    }

    static boolean dfsCheckLiberty(int y, int x, char color) {
        if (!inBounds(x, y) || visited[y][x]) return false;
        visited[y][x] = true;

        if (board[y][x] == EMPTY) return true;
        if (board[y][x] != color) return false;
        return (inBounds(x+1, y) && dfsCheckLiberty(y, x+1, color)) ||
               (inBounds(x-1, y) && dfsCheckLiberty(y, x-1, color)) ||
               (inBounds(x, y+1) && dfsCheckLiberty(y+1, x, color)) ||
               (inBounds(x, y-1) && dfsCheckLiberty(y-1, x, color));
    }

    static void removeCapturedStones(char colorToCheck) {
        visited = new boolean[SIZE][SIZE];
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (board[y][x] == colorToCheck && !visited[y][x]) {
                    List<int[]> group = new ArrayList<>();
                    if (!dfsCheckGroupLiberty(y, x, colorToCheck, group)) {
                        for (int[] pt : group) {
                            board[pt[0]][pt[1]] = EMPTY;
                        }
                    }
                }
            }
        }
    }

    static boolean dfsCheckGroupLiberty(int y, int x, char color, List<int[]> group) {
        if (!inBounds(x, y) || visited[y][x]) return false;
        visited[y][x] = true;

        if (board[y][x] == EMPTY) return true;
        if (board[y][x] != color) return false;

        group.add(new int[]{y, x});

        boolean hasLib = false;
        hasLib |= dfsCheckGroupLiberty(y, x + 1, color, group);
        hasLib |= dfsCheckGroupLiberty(y, x - 1, color, group);
        hasLib |= dfsCheckGroupLiberty(y + 1, x, color, group);
        hasLib |= dfsCheckGroupLiberty(y - 1, x, color, group);

        return hasLib;
    }
}

