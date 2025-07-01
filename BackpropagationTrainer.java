import java.util.Random;

public class BackpropagationTrainer {
    int inputSize;
    int hiddenSize;
    int outputSize;

    double[][] weightsInputHidden;
    double[][] weightsHiddenOutput;
    double[] biasHidden;
    double[] biasOutput;
    double learningRate = 0.5;

    Random random = new Random();

    public BackpropagationTrainer(int inputSize, int hiddenSize, int outputSize) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.outputSize = outputSize;

        weightsInputHidden = new double[hiddenSize][inputSize];
        weightsHiddenOutput = new double[outputSize][hiddenSize];
        biasHidden = new double[hiddenSize];
        biasOutput = new double[outputSize];

        initializeWeights();
    }

    private void initializeWeights() {
        for (int i = 0; i < hiddenSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                weightsInputHidden[i][j] = random.nextDouble() * 2 - 1;
            }
            biasHidden[i] = random.nextDouble() * 2 - 1;
        }

        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                weightsHiddenOutput[i][j] = random.nextDouble() * 2 - 1;
            }
            biasOutput[i] = random.nextDouble() * 2 - 1;
        }
    }

    public double[] feedforward(double[] input) {
        double[] hidden = new double[hiddenSize];
        for (int i = 0; i < hiddenSize; i++) {
            double sum = biasHidden[i];
            for (int j = 0; j < inputSize; j++) {
                sum += weightsInputHidden[i][j] * input[j];
            }
            hidden[i] = sigmoid(sum);
        }

        double[] output = new double[outputSize];
        for (int i = 0; i < outputSize; i++) {
            double sum = biasOutput[i];
            for (int j = 0; j < hiddenSize; j++) {
                sum += weightsHiddenOutput[i][j] * hidden[j];
            }
            output[i] = sigmoid(sum);
        }

        return output;
    }

    public void train(double[] input, double[] target) {
        double[] hidden = new double[hiddenSize];
        double[] output = new double[outputSize];
        for (int i = 0; i < hiddenSize; i++) {
            double sum = biasHidden[i];
            for (int j = 0; j < inputSize; j++) {
                sum += weightsInputHidden[i][j] * input[j];
            }
            hidden[i] = sigmoid(sum);
        }

        for (int i = 0; i < outputSize; i++) {
            double sum = biasOutput[i];
            for (int j = 0; j < hiddenSize; j++) {
                sum += weightsHiddenOutput[i][j] * hidden[j];
            }
            output[i] = sigmoid(sum);
        }
        double[] outputErrors = new double[outputSize];
        for (int i = 0; i < outputSize; i++) {
            outputErrors[i] = target[i] - output[i];
        }

        double[] outputGradients = new double[outputSize];
        for (int i = 0; i < outputSize; i++) {
            outputGradients[i] = outputErrors[i] * sigmoidDerivative(output[i]) * learningRate;
        }

        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < hiddenSize; j++) {
                weightsHiddenOutput[i][j] += outputGradients[i] * hidden[j];
            }
            biasOutput[i] += outputGradients[i];
        }

        double[] hiddenErrors = new double[hiddenSize];
        for (int i = 0; i < hiddenSize; i++) {
            double error = 0;
            for (int j = 0; j < outputSize; j++) {
                error += weightsHiddenOutput[j][i] * outputErrors[j];
            }
            hiddenErrors[i] = error;
        }

        double[] hiddenGradients = new double[hiddenSize];
        for (int i = 0; i < hiddenSize; i++) {
            hiddenGradients[i] = hiddenErrors[i] * sigmoidDerivative(hidden[i]) * learningRate;
        }

        for (int i = 0; i < hiddenSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                weightsInputHidden[i][j] += hiddenGradients[i] * input[j];
            }
            biasHidden[i] += hiddenGradients[i];
        }
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    private double sigmoidDerivative(double y) {
        return y * (1 - y); 
    }

    public static void main(String[] args) {
        BackpropagationTrainer trainer = new BackpropagationTrainer(2, 2, 1);
        double[][] inputs = {
            {0, 0}, {0, 1}, {1, 0}, {1, 1}
        };
        double[][] targets = {
            {0}, {1}, {1}, {0}
        };
        for (int epoch = 0; epoch < 10000; epoch++) {
            for (int i = 0; i < inputs.length; i++) {
                trainer.train(inputs[i], targets[i]);
            }
        }
        for (int i = 0; i < inputs.length; i++) {
            double[] result = trainer.feedforward(inputs[i]);
            System.out.printf("Input: %s Output: %.4f\n",
                    java.util.Arrays.toString(inputs[i]), result[0]);
        }
    }
}

