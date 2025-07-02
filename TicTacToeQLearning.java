import java.util.*;

public class TicTacToeQLearning {

    static final int EPISODES = 100000;
    static final double ALPHA = 0.1;     
    static final double GAMMA = 0.9;     
    static final double EPSILON = 0.2;   

    static final char EMPTY = '_';
    static final char AGENT = 'X';
    static final char OPPONENT = 'O';

    static Map<String, double[]> qTable = new HashMap<>();
    static Random random = new Random();

    public static void main(String[] args) {
        train();
        Scanner sc = new Scanner(System.in);
        char[] board = initBoard();

        while (true) {
            printBoard(board);
            System.out.println("Your move (0-8): ");
            int move = sc.nextInt();
            if (board[move] != EMPTY) {
                System.out.println("Invalid move. Try again.");
                continue;
            }
            board[move] = OPPONENT;
            if (isWinner(board, OPPONENT)) {
                printBoard(board);
                System.out.println("You win!");
                break;
            }
            if (isFull(board)) {
                printBoard(board);
                System.out.println("It's a draw!");
                break;
            }

            int agentMove = bestAction(board);
            board[agentMove] = AGENT;
            if (isWinner(board, AGENT)) {
                printBoard(board);
                System.out.println("Agent wins!");
                break;
            }
            if (isFull(board)) {
                printBoard(board);
                System.out.println("It's a draw!");
                break;
            }
        }
    }

    static void train() {
        for (int ep = 0; ep < EPISODES; ep++) {
            char[] board = initBoard();
            List<String> stateHistory = new ArrayList<>();
            List<Integer> actionHistory = new ArrayList<>();

            while (true) {
                int action = chooseAction(board);
                String state = new String(board);
                stateHistory.add(state);
                actionHistory.add(action);
                board[action] = AGENT;

                if (isWinner(board, AGENT)) {
                    updateQ(stateHistory, actionHistory, 1.0);
                    break;
                }
                if (isFull(board)) {
                    updateQ(stateHistory, actionHistory, 0.5);
                    break;
                }
                List<Integer> oppMoves = availableMoves(board);
                if (oppMoves.isEmpty()) {
                    updateQ(stateHistory, actionHistory, 0.5);
                    break;
                }
                int oppMove = oppMoves.get(random.nextInt(oppMoves.size()));
                board[oppMove] = OPPONENT;

                if (isWinner(board, OPPONENT)) {
                    updateQ(stateHistory, actionHistory, 0.0);
                    break;
                }
                if (isFull(board)) {
                    updateQ(stateHistory, actionHistory, 0.5);
                    break;
                }
            }
        }
    }

    static void updateQ(List<String> states, List<Integer> actions, double reward) {
        for (int i = states.size() - 1; i >= 0; i--) {
            String state = states.get(i);
            int action = actions.get(i);
            qTable.putIfAbsent(state, new double[9]);
            double[] qValues = qTable.get(state);

            if (i == states.size() - 1) {
                qValues[action] += ALPHA * (reward - qValues[action]);
            } else {
                String nextState = states.get(i + 1);
                qTable.putIfAbsent(nextState, new double[9]);
                double[] nextQ = qTable.get(nextState);
                double maxNextQ = Arrays.stream(nextQ).max().orElse(0);
                qValues[action] += ALPHA * (GAMMA * maxNextQ - qValues[action]);
            }
        }
    }

    static int chooseAction(char[] board) {
        String state = new String(board);
        qTable.putIfAbsent(state, new double[9]);
        double[] qValues = qTable.get(state);

        if (random.nextDouble() < EPSILON) {
            List<Integer> moves = availableMoves(board);
            return moves.get(random.nextInt(moves.size()));
        }

        double maxQ = -Double.MAX_VALUE;
        int bestAction = -1;
        for (int i = 0; i < 9; i++) {
            if (board[i] == EMPTY && qValues[i] > maxQ) {
                maxQ = qValues[i];
                bestAction = i;
            }
        }
        return bestAction != -1 ? bestAction : availableMoves(board).get(random.nextInt(availableMoves(board).size()));
    }

    static int bestAction(char[] board) {
        String state = new String(board);
        qTable.putIfAbsent(state, new double[9]);
        double[] qValues = qTable.get(state);

        double maxQ = -Double.MAX_VALUE;
        int bestAction = -1;
        for (int i = 0; i < 9; i++) {
            if (board[i] == EMPTY && qValues[i] > maxQ) {
                maxQ = qValues[i];
                bestAction = i;
            }
        }
        return bestAction != -1 ? bestAction : availableMoves(board).get(0);
    }

    static char[] initBoard() {
        char[] board = new char[9];
        Arrays.fill(board, EMPTY);
        return board;
    }

    static boolean isWinner(char[] board, char player) {
        int[][] wins = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                {0, 4, 8}, {2, 4, 6}
        };
        for (int[] line : wins) {
            if (board[line[0]] == player && board[line[1]] == player && board[line[2]] == player) {
                return true;
            }
        }
        return false;
    }

    static boolean isFull(char[] board) {
        for (char c : board) {
            if (c == EMPTY) return false;
        }
        return true;
    }

    static List<Integer> availableMoves(char[] board) {
        List<Integer> moves = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (board[i] == EMPTY) moves.add(i);
        }
        return moves;
    }

    static void printBoard(char[] board) {
        for (int i = 0; i < 9; i++) {
            System.out.print(board[i] + " ");
            if (i % 3 == 2) System.out.println();
        }
    }
}

