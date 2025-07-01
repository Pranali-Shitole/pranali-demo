import java.util.Random;

public class NeuralNetwork {
    int inputNodes, hiddenNodes, outputNodes;
    double[][] weightsInputHidden;
    double[][] weightsHiddenOutput;
    double[] hiddenBias;
    double[] outputBias;
    double learningRate = 0.1;

    Random rand = new Random();

    public NeuralNetwork(int inputNodes, int hiddenNodes, int outputNodes) {
        this.inputNodes = inputNodes;
        this.hiddenNodes = hiddenNodes;
        this.outputNodes = outputNodes;

        weightsInputHidden = new double[hiddenNodes][inputNodes];
        weightsHiddenOutput = new double[outputNodes][hiddenNodes];
        hiddenBias = new double[hiddenNodes];
        outputBias = new double[outputNodes];

        // Random weight initialization
        for (int i = 0; i < hiddenNodes; i++) {
            for (int j = 0; j < inputNodes; j++) {
                weightsInputHidden[i][j] = rand.nextDouble() * 2 - 1;
            }
            hiddenBias[i] = rand.nextDouble() * 2 - 1;
        }

        for (int i = 0; i < outputNodes; i++) {
            for (int j = 0; j < hiddenNodes; j++) {
                weightsHiddenOutput[i][j] = rand.nextDouble() * 2 - 1;
            }
            outputBias[i] = rand.nextDouble() * 2 - 1;
        }
    }

    public double[] feedforward(double[] input) {
        // Hidden layer
        double[] hidden = new double[hiddenNodes];
        for (int i = 0; i < hiddenNodes; i++) {
            double sum = hiddenBias[i];
            for (int j = 0; j < inputNodes; j++) {
                sum += weightsInputHidden[i][j] * input[j];
            }
            hidden[i] = sigmoid(sum);
        }

        // Output layer
        double[] output = new double[outputNodes];
        for (int i = 0; i < outputNodes; i++) {
            double sum = outputBias[i];
            for (int j = 0; j < hiddenNodes; j++) {
                sum += weightsHiddenOutput[i][j] * hidden[j];
            }
            output[i] = sigmoid(sum);
        }

        return output;
    }

    public void train(double[] input, double[] target) {
        // Forward pass
        double[] hidden = new double[hiddenNodes];
        for (int i = 0; i < hiddenNodes; i++) {
            double sum = hiddenBias[i];
            for (int j = 0; j < inputNodes; j++) {
                sum += weightsInputHidden[i][j] * input[j];
            }
            hidden[i] = sigmoid(sum);
        }

        double[] output = new double[outputNodes];
        for (int i = 0; i < outputNodes; i++) {
            double sum = outputBias[i];
            for (int j = 0; j < hiddenNodes; j++) {
                sum += weightsHiddenOutput[i][j] * hidden[j];
            }
            output[i] = sigmoid(sum);
        }

        // Calculate output errors
        double[] outputErrors = new double[outputNodes];
        for (int i = 0; i < outputNodes; i++) {
            outputErrors[i] = target[i] - output[i];
        }

        // Calculate gradients and update weightsHiddenOutput
        for (int i = 0; i < outputNodes; i++) {
            double gradient = outputErrors[i] * sigmoidDerivative(output[i]) * learningRate;
            for (int j = 0; j < hiddenNodes; j++) {
                weightsHiddenOutput[i][j] += gradient * hidden[j];
            }
            outputBias[i] += gradient;
        }

        // Calculate hidden layer errors
        double[] hiddenErrors = new double[hiddenNodes];
        for (int i = 0; i < hiddenNodes; i++) {
            double error = 0;
            for (int j = 0; j < outputNodes; j++) {
                error += weightsHiddenOutput[j][i] * outputErrors[j];
            }
            hiddenErrors[i] = error;
        }

        // Update weightsInputHidden
        for (int i = 0; i < hiddenNodes; i++) {
            double gradient = hiddenErrors[i] * sigmoidDerivative(hidden[i]) * learningRate;
            for (int j = 0; j < inputNodes; j++) {
                weightsInputHidden[i][j] += gradient * input[j];
            }
            hiddenBias[i] += gradient;
        }
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    private double sigmoidDerivative(double x) {
        return x * (1 - x); // assuming x = sigmoid(x)
    }

    public static void main(String[] args) {
        NeuralNetwork nn = new NeuralNetwork(2, 2, 1);

        // XOR problem
        double[][] inputs = {
            {0, 0}, {0, 1}, {1, 0}, {1, 1}
        };
        double[][] targets = {
            {0}, {1}, {1}, {0}
        };

        // Training
        for (int i = 0; i < 10000; i++) {
            int index = i % 4;
            nn.train(inputs[index], targets[index]);
        }

        // Testin
        for (int i = 0; i < 4; i++) {
            double[] output = nn.feedforward(inputs[i]);
            System.out.printf("Input: %s, Output: %.4f\n",
                    java.util.Arrays.toString(inputs[i]), output[0]);
        }
    }
}

