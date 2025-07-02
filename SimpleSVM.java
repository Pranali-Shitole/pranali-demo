import java.util.*;

public class SimpleSVM {

    static class DataPoint {
        double[] features;
        int label;

        DataPoint(double[] features, int label) {
            this.features = features;
            this.label = label;
        }
    }

    static class SVM {
        double[] weights;
        double bias;
        double learningRate = 0.001;
        double lambda = 0.01;  
        int epochs = 1000;

        SVM(int featureSize) {
            weights = new double[featureSize];
            bias = 0;
        }

        void train(List<DataPoint> data) {
            for (int epoch = 0; epoch < epochs; epoch++) {
                for (DataPoint point : data) {
                    double output = predictRaw(point.features);
                    if (point.label * output < 1) {
                        for (int i = 0; i < weights.length; i++) {
                            weights[i] += learningRate * (point.label * point.features[i] - lambda * weights[i]);
                        }
                        bias += learningRate * point.label;
                    } else {
                        for (int i = 0; i < weights.length; i++) {
                            weights[i] += learningRate * (-lambda * weights[i]);
                        }
                    }
                }
            }
        }

        double predictRaw(double[] features) {
            double sum = bias;
            for (int i = 0; i < weights.length; i++) {
                sum += weights[i] * features[i];
            }
            return sum;
        }

        int predict(double[] features) {
            return predictRaw(features) >= 0 ? 1 : -1;
        }
    }

    public static void main(String[] args) {
        List<DataPoint> trainingData = new ArrayList<>();
        trainingData.add(new DataPoint(new double[]{2.0, 3.0}, 1));
        trainingData.add(new DataPoint(new double[]{1.0, 5.0}, 1));
        trainingData.add(new DataPoint(new double[]{2.5, 4.5}, 1));
        trainingData.add(new DataPoint(new double[]{-1.0, -1.0}, -1));
        trainingData.add(new DataPoint(new double[]{-2.0, -3.0}, -1));
        trainingData.add(new DataPoint(new double[]{-3.0, -2.0}, -1));

        SVM svm = new SVM(2);
        svm.train(trainingData);
        double[] testPoint = {1.5, 2.0};
        int prediction = svm.predict(testPoint);
        System.out.println("Prediction for point " + Arrays.toString(testPoint) + ": " + (prediction == 1 ? "+1" : "-1"));
    }
}

