import java.util.Arrays;
import java.util.Random;

public class SimpleRNN {

    int inputSize = 1;
    int hiddenSize = 4;
    int outputSize = 1;

    double[][] Wxh; 
    double[][] Whh; 
    double[][] Why; 

    double[] bh; 
    double[] by; 

    double learningRate = 0.1;

    Random rand = new Random();

    public SimpleRNN() {
        Wxh = new double[hiddenSize][inputSize];
        Whh = new double[hiddenSize][hiddenSize];
        Why = new double[outputSize][hiddenSize];
        bh = new double[hiddenSize];
        by = new double[outputSize];
        init(Wxh);
        init(Whh);
        init(Why);
        init(bh);
        init(by);
    }

    private void init(double[][] m) {
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                m[i][j] = rand.nextDouble() * 2 - 1;
    }

    private void init(double[] v) {
        for (int i = 0; i < v.length; i++)
            v[i] = rand.nextDouble() * 2 - 1;
    }

    public double[] forward(double[] inputs, double[][] hs) {
        int T = inputs.length;
        double[] outputs = new double[T];
        Arrays.fill(hs[0], 0.0);

        for (int t = 0; t < T; t++) {
            double[] x = { inputs[t] };
            double[] hPrev = hs[t];
            double[] h = new double[hiddenSize];

            for (int i = 0; i < hiddenSize; i++) {
                double sum = bh[i];
                for (int j = 0; j < inputSize; j++) sum += Wxh[i][j] * x[j];
                for (int j = 0; j < hiddenSize; j++) sum += Whh[i][j] * hPrev[j];
                h[i] = Math.tanh(sum);
            }
            hs[t + 1] = h;

            double y = by[0];
            for (int j = 0; j < hiddenSize; j++) y += Why[0][j] * h[j];
            outputs[t] = sigmoid(y);
        }
        return outputs;
    }

    public void train(double[] inputs, double[] targets) {
        int T = inputs.length;

        double[][] hs = new double[T + 1][hiddenSize];
        double[] ys = forward(inputs, hs);
        double[][] dWxh = new double[hiddenSize][inputSize];
        double[][] dWhh = new double[hiddenSize][hiddenSize];
        double[][] dWhy = new double[outputSize][hiddenSize];
        double[] dbh = new double[hiddenSize];
        double[] dby = new double[outputSize];

        double[] dhNext = new double[hiddenSize];

        for (int t = T - 1; t >= 0; t--) {
            double dy = ys[t] - targets[t];
            for (int j = 0; j < hiddenSize; j++) {
                dWhy[0][j] += dy * hs[t + 1][j];
            }
            dby[0] += dy;

            double[] dh = new double[hiddenSize];
            for (int j = 0; j < hiddenSize; j++) {
                dh[j] = dy * Why[0][j] + dhNext[j];
                dh[j] *= (1 - hs[t + 1][j] * hs[t + 1][j]); 
            }

            for (int i = 0; i < hiddenSize; i++) {
                dbh[i] += dh[i];
                for (int j = 0; j < inputSize; j++) {
                    dWxh[i][j] += dh[i] * inputs[t];
                }
                for (int j = 0; j < hiddenSize; j++) {
                    dWhh[i][j] += dh[i] * hs[t][j];
                }
            }

            dhNext = dh;
        }
        for (int i = 0; i < hiddenSize; i++) {
            for (int j = 0; j < inputSize; j++)
                Wxh[i][j] -= learningRate * dWxh[i][j];
            for (int j = 0; j < hiddenSize; j++)
                Whh[i][j] -= learningRate * dWhh[i][j];
            bh[i] -= learningRate * dbh[i];
        }

        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < hiddenSize; j++)
                Why[i][j] -= learningRate * dWhy[i][j];
            by[i] -= learningRate * dby[i];
        }
    }

    private double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public static void main(String[] args) {
        SimpleRNN rnn = new SimpleRNN();
        double[] seq = { 0, 1, 0, 1, 0, 1 };
        double[] targets = { 1, 0, 1, 0, 1, 0 };

        for (int i = 0; i < 5000; i++) {
            rnn.train(seq, targets);
        }

        double[][] hiddenStates = new double[seq.length + 1][rnn.hiddenSize];
        double[] prediction = rnn.forward(seq, hiddenStates);

        System.out.println("Input:     " + Arrays.toString(seq));
        System.out.println("Target:    " + Arrays.toString(targets));
        System.out.println("Predicted: " + Arrays.toString(prediction));
    }
}

