import java.util.*;

public class ChessEngine {
    static char[][] board = new char[8][8];

    public static void main(String[] args) {
        setupBoard();
        Scanner scanner = new Scanner(System.in);
        boolean whiteTurn = true;

        while (true) {
            printBoard();
            System.out.println((whiteTurn ? "White" : "Black") + "'s move (e.g., e2e4): ");
            String move = scanner.nextLine().trim();

            if (move.equalsIgnoreCase("exit")) break;
            if (move.length() != 4) {
                System.out.println("Invalid move format.");
                continue;
            }

            int fromX = 8 - Character.getNumericValue(move.charAt(1));
            int fromY = move.charAt(0) - 'a';
            int toX = 8 - Character.getNumericValue(move.charAt(3));
            int toY = move.charAt(2) - 'a';

            if (!isValidMove(fromX, fromY, toX, toY, whiteTurn)) {
                System.out.println("Illegal move.");
                continue;
            }

            board[toX][toY] = board[fromX][fromY];
            board[fromX][fromY] = '.';
            whiteTurn = !whiteTurn;
        }
        scanner.close();
    }

    static void setupBoard() {
        String[] initial = {
            "rnbqkbnr",
            "pppppppp",
            "........",
            "........",
            "........",
            "........",
            "PPPPPPPP",
            "RNBQKBNR"
        };
        for (int i = 0; i < 8; i++)
            board[i] = initial[i].toCharArray();
    }

    static void printBoard() {
        System.out.println("  a b c d e f g h");
        for (int i = 0; i < 8; i++) {
            System.out.print((8 - i) + " ");
            for (int j = 0; j < 8; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println(8 - i);
        }
        System.out.println("  a b c d e f g h\n");
    }

    static boolean isValidMove(int fx, int fy, int tx, int ty, boolean whiteTurn) {
        if (!inBounds(fx, fy) || !inBounds(tx, ty)) return false;

        char piece = board[fx][fy];
        char target = board[tx][ty];

        if (piece == '.') return false;
        if (Character.isUpperCase(piece) != whiteTurn) return false;
        if (target != '.' && Character.isUpperCase(target) == whiteTurn) return false;

        int dx = tx - fx, dy = ty - fy;
        piece = Character.toLowerCase(piece);

        return switch (piece) {
            case 'p' -> validatePawn(fx, fy, tx, ty, whiteTurn);
            case 'r' -> (dy == 0 || dx == 0) && clearPath(fx, fy, tx, ty);
            case 'n' -> Math.abs(dx * dy) == 2;
            case 'b' -> Math.abs(dx) == Math.abs(dy) && clearPath(fx, fy, tx, ty);
            case 'q' -> (Math.abs(dx) == Math.abs(dy) || dx == 0 || dy == 0) && clearPath(fx, fy, tx, ty);
            case 'k' -> Math.abs(dx) <= 1 && Math.abs(dy) <= 1;
            default -> false;
        };
    }

    static boolean validatePawn(int fx, int fy, int tx, int ty, boolean white) {
        int direction = white ? -1 : 1;
        if (fy == ty) {
            if (board[tx][ty] != '.') return false;
            if (tx - fx == direction) return true;
            if ((white && fx == 6 || !white && fx == 1) && tx - fx == 2 * direction && board[fx + direction][fy] == '.') return true;
        } else if (Math.abs(ty - fy) == 1 && tx - fx == direction && board[tx][ty] != '.') {
            return true;
        }
        return false;
    }

    static boolean clearPath(int fx, int fy, int tx, int ty) {
        int dx = Integer.compare(tx, fx);
        int dy = Integer.compare(ty, fy);
        fx += dx;
        fy += dy;
        while (fx != tx || fy != ty) {
            if (board[fx][fy] != '.') return false;
            fx += dx;
            fy += dy;
        }
        return true;
    }

    static boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < 8 && y < 8;
    }
}

