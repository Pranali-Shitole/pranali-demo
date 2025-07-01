import java.util.*;

public class NaiveBayesClassifier {

    static class Instance {
        String[] features;
        String label;

        Instance(String[] features, String label) {
            this.features = features;
            this.label = label;
        }
    }

    static class NaiveBayes {
        List<Instance> data;
        Set<String> labels;
        Map<String, Double> labelProbabilities;
        Map<String, Map<Integer, Map<String, Double>>> featureLikelihoods;
        int featureCount;

        public NaiveBayes(List<Instance> data) {
            this.data = data;
            this.labels = new HashSet<>();
            this.labelProbabilities = new HashMap<>();
            this.featureLikelihoods = new HashMap<>();
            if (!data.isEmpty()) {
                this.featureCount = data.get(0).features.length;
            }
        }

        public void train() {
            Map<String, Integer> labelCounts = new HashMap<>();
            for (Instance inst : data) {
                labels.add(inst.label);
                labelCounts.put(inst.label, labelCounts.getOrDefault(inst.label, 0) + 1);
            }
            int total = data.size();
            for (String label : labels) {
                labelProbabilities.put(label, labelCounts.get(label) / (double) total);
                featureLikelihoods.put(label, new HashMap<>());
            }
            for (String label : labels) {
                List<Instance> labelData = new ArrayList<>();
                for (Instance inst : data) {
                    if (inst.label.equals(label)) {
                        labelData.add(inst);
                    }
                }

                for (int i = 0; i < featureCount; i++) {
                    Map<String, Integer> valueCounts = new HashMap<>();

                    for (Instance inst : labelData) {
                        String value = inst.features[i];
                        valueCounts.put(value, valueCounts.getOrDefault(value, 0) + 1);
                    }
                    int totalLabelCount = labelData.size();
                    int distinctValues = (int) data.stream().map(d -> d.features[i]).distinct().count();
                    Map<String, Double> probabilities = new HashMap<>();
                    for (String value : valueCounts.keySet()) {
                        double prob = (valueCounts.get(value) + 1.0) / (totalLabelCount + distinctValues);
                        probabilities.put(value, prob);
                    }
                    for (Instance inst : data) {
                        String val = inst.features[i];
                        probabilities.putIfAbsent(val, 1.0 / (totalLabelCount + distinctValues));
                    }

                    featureLikelihoods.get(label).put(i, probabilities);
                }
            }
        }

        public String predict(String[] features) {
            double maxProb = -1;
            String bestLabel = null;

            for (String label : labels) {
                double prob = labelProbabilities.get(label);
                for (int i = 0; i < features.length; i++) {
                    String val = features[i];
                    Map<String, Double> likelihoods = featureLikelihoods.get(label).get(i);
                    prob *= likelihoods.getOrDefault(val, 1e-6); 
                }

                if (prob > maxProb) {
                    maxProb = prob;
                    bestLabel = label;
                }
            }

            return bestLabel;
        }
    }

    public static void main(String[] args) {
        List<Instance> trainingData = List.of(
            new Instance(new String[]{"Sunny", "Hot", "High", "Weak"}, "No"),
            new Instance(new String[]{"Sunny", "Hot", "High", "Strong"}, "No"),
            new Instance(new String[]{"Overcast", "Hot", "High", "Weak"}, "Yes"),
            new Instance(new String[]{"Rain", "Mild", "High", "Weak"}, "Yes"),
            new Instance(new String[]{"Rain", "Cool", "Normal", "Weak"}, "Yes"),
            new Instance(new String[]{"Rain", "Cool", "Normal", "Strong"}, "No"),
            new Instance(new String[]{"Overcast", "Cool", "Normal", "Strong"}, "Yes"),
            new Instance(new String[]{"Sunny", "Mild", "High", "Weak"}, "No"),
            new Instance(new String[]{"Sunny", "Cool", "Normal", "Weak"}, "Yes"),
            new Instance(new String[]{"Rain", "Mild", "Normal", "Weak"}, "Yes"),
            new Instance(new String[]{"Sunny", "Mild", "Normal", "Strong"}, "Yes"),
            new Instance(new String[]{"Overcast", "Mild", "High", "Strong"}, "Yes"),
            new Instance(new String[]{"Overcast", "Hot", "Normal", "Weak"}, "Yes"),
            new Instance(new String[]{"Rain", "Mild", "High", "Strong"}, "No")
        );

        NaiveBayes nb = new NaiveBayes(trainingData);
        nb.train();

        String[] input = new String[]{"Sunny", "Cool", "High", "Strong"};
        String prediction = nb.predict(input);

        System.out.println("Predicted class for input " + Arrays.toString(input) + ": " + prediction);
    }
}
