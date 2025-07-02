import java.util.*;
import java.util.stream.Collectors;

public class RandomForest {

    static class Node {
        String attribute;
        Map<String, Node> children = new HashMap<>();
        String label;

        boolean isLeaf() {
            return label != null;
        }
    }

    static class Instance {
        Map<String, String> attributes = new HashMap<>();
        String label;

        Instance(Map<String, String> attrs, String lbl) {
            attributes.putAll(attrs);
            label = lbl;
        }
    }

    static class DecisionTree {
        Node root;

        DecisionTree(List<Instance> data, Set<String> attributes) {
            root = buildTree(data, attributes);
        }

        private Node buildTree(List<Instance> data, Set<String> attributes) {
            Node node = new Node();

            String firstLabel = data.get(0).label;
            if (data.stream().allMatch(d -> d.label.equals(firstLabel))) {
                node.label = firstLabel;
                return node;
            }

            if (attributes.isEmpty()) {
                node.label = majorityLabel(data);
                return node;
            }

            String bestAttr = chooseBestAttribute(data, attributes);
            node.attribute = bestAttr;

            Map<String, List<Instance>> partitions = new HashMap<>();
            for (Instance instance : data) {
                String attrValue = instance.attributes.get(bestAttr);
                partitions.computeIfAbsent(attrValue, k -> new ArrayList<>()).add(instance);
            }

            for (Map.Entry<String, List<Instance>> entry : partitions.entrySet()) {
                Set<String> remainingAttrs = new HashSet<>(attributes);
                remainingAttrs.remove(bestAttr);
                node.children.put(entry.getKey(), buildTree(entry.getValue(), remainingAttrs));
            }

            return node;
        }

        private String chooseBestAttribute(List<Instance> data, Set<String> attributes) {
            double baseEntropy = entropy(data);
            double bestGain = -1;
            String bestAttr = null;

            for (String attr : attributes) {
                double gain = baseEntropy;
                Map<String, List<Instance>> splits = new HashMap<>();

                for (Instance instance : data) {
                    String value = instance.attributes.get(attr);
                    splits.computeIfAbsent(value, k -> new ArrayList<>()).add(instance);
                }

                for (List<Instance> subset : splits.values()) {
                    gain -= ((double) subset.size() / data.size()) * entropy(subset);
                }

                if (gain > bestGain) {
                    bestGain = gain;
                    bestAttr = attr;
                }
            }

            return bestAttr;
        }

        private double entropy(List<Instance> data) {
            Map<String, Long> freq = data.stream()
                    .collect(Collectors.groupingBy(d -> d.label, Collectors.counting()));
            double entropy = 0.0;
            for (long count : freq.values()) {
                double p = (double) count / data.size();
                entropy -= p * (Math.log(p) / Math.log(2));
            }
            return entropy;
        }

        private String majorityLabel(List<Instance> data) {
            return data.stream()
                    .collect(Collectors.groupingBy(d -> d.label, Collectors.counting()))
                    .entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        }

        public String classify(Instance instance) {
            Node node = root;
            while (!node.isLeaf()) {
                String value = instance.attributes.get(node.attribute);
                node = node.children.getOrDefault(value, null);
                if (node == null) return "Unknown";
            }
            return node.label;
        }
    }

    static class RandomForestModel {
        List<DecisionTree> trees = new ArrayList<>();

        RandomForestModel(List<Instance> data, Set<String> attributes, int numTrees) {
            Random random = new Random();
            for (int i = 0; i < numTrees; i++) {
                List<Instance> sample = new ArrayList<>();
                for (int j = 0; j < data.size(); j++) {
                    sample.add(data.get(random.nextInt(data.size()))); 
                }
                trees.add(new DecisionTree(sample, attributes));
            }
        }

        public String classify(Instance instance) {
            Map<String, Integer> voteCount = new HashMap<>();
            for (DecisionTree tree : trees) {
                String prediction = tree.classify(instance);
                voteCount.put(prediction, voteCount.getOrDefault(prediction, 0) + 1);
            }
            return voteCount.entrySet().stream()
                    .max(Map.Entry.comparingByValue()).get().getKey();
        }
    }

    public static void main(String[] args) {
        List<Instance> data = new ArrayList<>();
        data.add(new Instance(Map.of("Outlook", "Sunny", "Temperature", "Hot", "Humidity", "High", "Wind", "Weak"), "No"));
        data.add(new Instance(Map.of("Outlook", "Sunny", "Temperature", "Hot", "Humidity", "High", "Wind", "Strong"), "No"));
        data.add(new Instance(Map.of("Outlook", "Overcast", "Temperature", "Hot", "Humidity", "High", "Wind", "Weak"), "Yes"));
        data.add(new Instance(Map.of("Outlook", "Rain", "Temperature", "Mild", "Humidity", "High", "Wind", "Weak"), "Yes"));
        data.add(new Instance(Map.of("Outlook", "Rain", "Temperature", "Cool", "Humidity", "Normal", "Wind", "Weak"), "Yes"));
        data.add(new Instance(Map.of("Outlook", "Rain", "Temperature", "Cool", "Humidity", "Normal", "Wind", "Strong"), "No"));
        data.add(new Instance(Map.of("Outlook", "Overcast", "Temperature", "Cool", "Humidity", "Normal", "Wind", "Strong"), "Yes"));
        data.add(new Instance(Map.of("Outlook", "Sunny", "Temperature", "Mild", "Humidity", "High", "Wind", "Weak"), "No"));
        data.add(new Instance(Map.of("Outlook", "Sunny", "Temperature", "Cool", "Humidity", "Normal", "Wind", "Weak"), "Yes"));
        data.add(new Instance(Map.of("Outlook", "Rain", "Temperature", "Mild", "Humidity", "Normal", "Wind", "Weak"), "Yes"));

        Set<String> attributes = Set.of("Outlook", "Temperature", "Humidity", "Wind");

        RandomForestModel forest = new RandomForestModel(data, attributes, 5); 

        Instance testInstance = new Instance(Map.of(
                "Outlook", "Sunny",
                "Temperature", "Cool",
                "Humidity", "High",
                "Wind", "Strong"
        ), "");

        String prediction = forest.classify(testInstance);
        System.out.println("Prediction for test instance: " + prediction);
    }
}

