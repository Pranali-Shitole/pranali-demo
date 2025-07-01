import java.util.Random;

public class SimpleCNN {

    static final int IMG_SIZE = 5;
    static final int KERNEL_SIZE = 3;
    static final int FC_SIZE = (IMG_SIZE - KERNEL_SIZE + 1) * (IMG_SIZE - KERNEL_SIZE + 1);

    double[][] kernel;
    double[] fcWeights;
    double bias;
    double learningRate = 0.01;

    Random rand = new Random();

    public SimpleCNN() {
        kernel = new double[KERNEL_SIZE][KERNEL_SIZE];
        fcWeights = new double[FC_SIZE];
        bias = rand.nextDouble();

        for (int i = 0; i < KERNEL_SIZE; i++)
            for (int j = 0; j < KERNEL_SIZE; j++)
                kernel[i][j] = rand.nextDouble() * 2 - 1;

        for (int i = 0; i < FC_SIZE; i++)
            fcWeights[i] = rand.nextDouble() * 2 - 1;
    }

    public double[][] convolve(double[][] input) {
        int outSize = IMG_SIZE - KERNEL_SIZE + 1;
        double[][] output = new double[outSize][outSize];

        for (int i = 0; i < outSize; i++) {
            for (int j = 0; j < outSize; j++) {
                double sum = 0;
                for (int ki = 0; ki < KERNEL_SIZE; ki++) {
                    for (int kj = 0; kj < KERNEL_SIZE; kj++) {
                        sum += input[i + ki][j + kj] * kernel[ki][kj];
                    }
                }
                output[i][j] = relu(sum);
            }
        }

        return output;
    }

    public double forward(double[][] image) {
        double[][] conv = convolve(image);
        double[] flattened = flatten(conv);
        double sum = bias;
        for (int i = 0; i < FC_SIZE; i++) {
            sum += fcWeights[i] * flattened[i];
        }
        return sigmoid(sum);
    }

    public void train(double[][] image, double target) {
        double[][] conv = convolve(image);
        double[] flat = flatten(conv);
        double prediction = forward(image);
        double error = target - prediction;
        double dPred = sigmoidDerivative(prediction) * error;
        for (int i = 0; i < FC_SIZE; i++) {
            fcWeights[i] += learningRate * dPred * flat[i];
        }

        bias += learningRate * dPred;
    }

    private double[] flatten(double[][] matrix) {
        double[] flat = new double[matrix.length * matrix[0].length];
        int idx = 0;
        for (double[] row : matrix) {
            for (double val : row) {
                flat[idx++] = val;
            }
        }
        return flat;
    }

    private double relu(double x) {
        return Math.max(0, x);
    }

    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    private double sigmoidDerivative(double y) {
        return y * (1 - y);
    }

    public static void main(String[] args) {
        SimpleCNN cnn = new SimpleCNN();
        double[][] image = {
            {0, 0, 1, 0, 0},
            {0, 1, 1, 1, 0},
            {1, 0, 1, 0, 1},
            {0, 1, 1, 1, 0},
            {0, 0, 1, 0, 0}
        };

        double target = 1.0;

        for (int epoch = 0; epoch < 1000; epoch++) {
            cnn.train(image, target);
        }

        double output = cnn.forward(image);
        System.out.println("Predicted Output: " + output);
    }
}

