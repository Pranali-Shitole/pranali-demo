import java.util.*;

class DecisionTree {

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
        Node root = buildTree(data, attributes);

        printTree(root, "");
    }
    static Node buildTree(List<Instance> data, Set<String> attributes) {
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
    static String chooseBestAttribute(List<Instance> data, Set<String> attributes) {
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
    static double entropy(List<Instance> data) {
        Map<String, Long> freq = data.stream().collect(
                Collectors.groupingBy(d -> d.label, Collectors.counting()));

        double entropy = 0.0;
        for (long count : freq.values()) {
            double p = (double) count / data.size();
            entropy -= p * (Math.log(p) / Math.log(2));
        }
        return entropy;
    }
    static String majorityLabel(List<Instance> data) {
        return data.stream()
                .collect(Collectors.groupingBy(d -> d.label, Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }
    static void printTree(Node node, String indent) {
        if (node.isLeaf()) {
            System.out.println(indent + "Label: " + node.label);
        } else {
            for (Map.Entry<String, Node> entry : node.children.entrySet()) {
                System.out.println(indent + node.attribute + " = " + entry.getKey() + ":");
                printTree(entry.getValue(), indent + "  ");
            }
        }
    }
}

