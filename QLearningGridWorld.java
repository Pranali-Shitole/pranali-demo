import java.util.*;

public class QLearningGridWorld {

    static final int SIZE = 5;
    static final int ACTIONS = 4;
    static final double ALPHA = 0.1; 
    static final double GAMMA = 0.9; 
    static final double EPSILON = 0.2; 
    static final int EPISODES = 500;

    static final int[][] directions = {
        {-1, 0}, 
        {1, 0},  
        {0, -1},
        {0, 1}   
    };

    static double[][][] qTable = new double[SIZE][SIZE][ACTIONS];
    static Random random = new Random();

    public static void main(String[] args) {
        for (int episode = 0; episode < EPISODES; episode++) {
            int x = 0, y = 0; 
            while (!(x == SIZE - 1 && y == SIZE - 1)) {
                int action = chooseAction(x, y);
                int newX = x + directions[action][0];
                int newY = y + directions[action][1];
                if (newX < 0 || newX >= SIZE || newY < 0 || newY >= SIZE) {
                    newX = x;
                    newY = y;
                }

                double reward = (newX == SIZE - 1 && newY == SIZE - 1) ? 100 : -1;
                double maxQ = Arrays.stream(qTable[newX][newY]).max().getAsDouble();
                qTable[x][y][action] += ALPHA * (reward + GAMMA * maxQ - qTable[x][y][action]);

                x = newX;
                y = newY;
            }
        }

        printPolicy();
    }

    static int chooseAction(int x, int y) {
        if (random.nextDouble() < EPSILON) {
            return random.nextInt(ACTIONS);
        } else {
            double[] qValues = qTable[x][y];
            List<Integer> bestActions = new ArrayList<>();
            double maxQ = Arrays.stream(qValues).max().getAsDouble();

            for (int i = 0; i < ACTIONS; i++) {
                if (qValues[i] == maxQ) bestActions.add(i);
            }
            return bestActions.get(random.nextInt(bestActions.size()));
        }
    }

    static void printPolicy() {
        System.out.println("Learned Policy:");
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (i == SIZE - 1 && j == SIZE - 1) {
                    System.out.print(" G ");
                    continue;
                }
                int bestAction = 0;
                double maxQ = qTable[i][j][0];
                for (int a = 1; a < ACTIONS; a++) {
                    if (qTable[i][j][a] > maxQ) {
                        maxQ = qTable[i][j][a];
                        bestAction = a;
                    }
                }
                System.out.print(getActionSymbol(bestAction) + " ");
            }
            System.out.println();
        }
    }

    static String getActionSymbol(int action) {
        switch (action) {
            case 0: return "↑";
            case 1: return "↓";
            case 2: return "←";
            case 3: return "→";
            default: return "?";
        }
    }
}

