import java.util.*;

public class KDTree {
    private static class Node {
        int[] point; 
        Node left, right;

        Node(int[] point) {
            this.point = point;
        }
    }

    private Node root;
    private final int k = 2; 

    public void insert(int[] point) {
        root = insertRec(root, point, 0);
    }

    private Node insertRec(Node node, int[] point, int depth) {
        if (node == null) return new Node(point);

        int axis = depth % k;
        if (point[axis] < node.point[axis]) {
            node.left = insertRec(node.left, point, depth + 1);
        } else {
            node.right = insertRec(node.right, point, depth + 1);
        }
        return node;
    }

    public boolean search(int[] point) {
        return searchRec(root, point, 0);
    }

    private boolean searchRec(Node node, int[] point, int depth) {
        if (node == null) return false;
        if (Arrays.equals(node.point, point)) return true;

        int axis = depth % k;
        if (point[axis] < node.point[axis]) {
            return searchRec(node.left, point, depth + 1);
        } else {
            return searchRec(node.right, point, depth + 1);
        }
    }

    public List<int[]> rangeSearch(int[] lower, int[] upper) {
        List<int[]> result = new ArrayList<>();
        rangeSearchRec(root, lower, upper, 0, result);
        return result;
    }

    private void rangeSearchRec(Node node, int[] lower, int[] upper, int depth, List<int[]> result) {
        if (node == null) return;

        boolean inside = true;
        for (int i = 0; i < k; i++) {
            if (node.point[i] < lower[i] || node.point[i] > upper[i]) {
                inside = false;
                break;
            }
        }
        if (inside) result.add(node.point);

        int axis = depth % k;
        if (node.point[axis] >= lower[axis]) {
            rangeSearchRec(node.left, lower, upper, depth + 1, result);
        }
        if (node.point[axis] <= upper[axis]) {
            rangeSearchRec(node.right, lower, upper, depth + 1, result);
        }
    }
    private static void printPoint(int[] point) {
        System.out.println("(" + point[0] + ", " + point[1] + ")");
    }
    public static void main(String[] args) {
        KDTree tree = new KDTree();

        int[][] points = {
            {3, 6},
            {17, 15},
            {13, 15},
            {6, 12},
            {9, 1},
            {2, 7},
            {10, 19}
        };

        for (int[] point : points) {
            tree.insert(point);
        }

        System.out.println("Search (10, 19): " + tree.search(new int[]{10, 19}));
        System.out.println("Search (12, 19): " + tree.search(new int[]{12, 19}));

        System.out.println("\nRange search [(0, 0) to (10, 10)]:");
        List<int[]> result = tree.rangeSearch(new int[]{0, 0}, new int[]{10, 10});
        for (int[] p : result) {
            printPoint(p);
        }
    }
}

