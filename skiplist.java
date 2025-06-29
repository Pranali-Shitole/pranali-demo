import java.util.Random;

class Node {
    int value;
    Node[] forward;

    public Node(int level, int value) {
        forward = new Node[level + 1];  
        this.value = value;
    }
}

public class SkipList {
    private static final int MAX_LEVEL = 6; 
    private final Node head = new Node(MAX_LEVEL, Integer.MIN_VALUE);
    private int level = 0;
    private final Random random = new Random();
    private int randomLevel() {
        int lvl = 0;
        while (random.nextBoolean() && lvl < MAX_LEVEL) {
            lvl++;
        }
        return lvl;
    }
    public void insert(int value) {
        Node current = head;
        Node[] update = new Node[MAX_LEVEL + 1];
        for (int i = level; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].value < value) {
                current = current.forward[i];
            }
            update[i] = current;
        }

        current = current.forward[0];

        if (current == null || current.value != value) {
            int newLevel = randomLevel();

            if (newLevel > level) {
                for (int i = level + 1; i <= newLevel; i++) {
                    update[i] = head;
                }
                level = newLevel;
            }

            Node newNode = new Node(newLevel, value);
            for (int i = 0; i <= newLevel; i++) {
                newNode.forward[i] = update[i].forward[i];
                update[i].forward[i] = newNode;
            }
        }
    }
    public boolean search(int value) {
        Node current = head;
        for (int i = level; i >= 0; i--) {
            while (current.forward[i] != null && current.forward[i].value < value) {
                current = current.forward[i];
            }
        }

        current = current.forward[0];
        return current != null && current.value == value;
    }
    public void display() {
        System.out.println("Skip List:");
        for (int i = level; i >= 0; i--) {
            Node node = head.forward[i];
            System.out.print("Level " + i + ": ");
            while (node != null) {
                System.out.print(node.value + " ");
                node = node.forward[i];
            }
            System.out.println();
        }
    }
    public static void main(String[] args) {
        SkipList skipList = new SkipList();
        int[] values = {3, 6, 7, 9, 12, 19, 17, 26, 21, 25};

        for (int value : values) {
            skipList.insert(value);
        }

        skipList.display();

        int searchVal = 19;
        System.out.println("Searching for " + searchVal + ": " + (skipList.search(searchVal) ? "Found" : "Not Found"));
    }
}

