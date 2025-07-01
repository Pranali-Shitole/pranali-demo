import java.util.*;

public class PCA {

    public static void main(String[] args) {
        double[][] data = {
            {2.5, 2.4},
            {0.5, 0.7},
            {2.2, 2.9},
            {1.9, 2.2},
            {3.1, 3.0},
            {2.3, 2.7},
            {2.0, 1.6},
            {1.0, 1.1},
            {1.5, 1.6},
            {1.1, 0.9}
        };

        System.out.println("Original Data:");
        printMatrix(data);

        double[][] reduced = pca(data, 1); 

        System.out.println("\nReduced Data (1D):");
        printMatrix(reduced);
    }

    public static double[][] pca(double[][] data, int numComponents) {
        int n = data.length;
        int dim = data[0].length;
        double[] mean = new double[dim];
        for (double[] row : data) {
            for (int i = 0; i < dim; i++) {
                mean[i] += row[i];
            }
        }
        for (int i = 0; i < dim; i++) {
            mean[i] /= n;
        }

        double[][] centered = new double[n][dim];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < dim; j++) {
                centered[i][j] = data[i][j] - mean[j];
            }
        }
        double[][] cov = new double[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = i; j < dim; j++) {
                double sum = 0;
                for (int k = 0; k < n; k++) {
                    sum += centered[k][i] * centered[k][j];
                }
                cov[i][j] = cov[j][i] = sum / (n - 1);
            }
        }
        EigenDecomposition eigen = new EigenDecomposition(cov);
        double[][] eigenVectors = eigen.getEigenvectors();
        double[] eigenValues = eigen.getEigenvalues();
        Integer[] indices = new Integer[eigenValues.length];
        for (int i = 0; i < eigenValues.length; i++) indices[i] = i;

        Arrays.sort(indices, (i, j) -> Double.compare(eigenValues[j], eigenValues[i]));
        double[][] selectedVectors = new double[numComponents][dim];
        for (int i = 0; i < numComponents; i++) {
            selectedVectors[i] = eigenVectors[indices[i]];
        }

        double[][] result = new double[n][numComponents];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < numComponents; j++) {
                for (int k = 0; k < dim; k++) {
                    result[i][j] += centered[i][k] * selectedVectors[j][k];
                }
            }
        }

        return result;
    }

    public static void printMatrix(double[][] m) {
        for (double[] row : m) {
            System.out.println(Arrays.toString(row));
        }
    }
    public static class EigenDecomposition {
        private final double[][] eigenVectors;
        private final double[] eigenValues;

        public EigenDecomposition(double[][] matrix) {
            if (matrix.length == 2) {
                double a = matrix[0][0], b = matrix[0][1], c = matrix[1][1];

                double trace = a + c;
                double det = a * c - b * b;
                double temp = Math.sqrt(trace * trace - 4 * det);

                double lambda1 = (trace + temp) / 2;
                double lambda2 = (trace - temp) / 2;

                double[] v1 = { b, lambda1 - a };
                double[] v2 = { b, lambda2 - a };

                normalize(v1);
                normalize(v2);

                eigenValues = new double[]{ lambda1, lambda2 };
                eigenVectors = new double[][]{ v1, v2 };
            } else {
                throw new UnsupportedOperationException("Only 2D matrices supported in this example.");
            }
        }

        public double[] getEigenvalues() {
            return eigenValues;
        }

        public double[][] getEigenvectors() {
            return eigenVectors;
        }

        private void normalize(double[] v) {
            double norm = 0;
            for (double x : v) norm += x * x;
            norm = Math.sqrt(norm);
            for (int i = 0; i < v.length; i++) v[i] /= norm;
        }
    }
}

