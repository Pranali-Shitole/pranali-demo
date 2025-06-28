public import java.util.*;

class HuffmanNode {
    char character;
    int frequency;
    HuffmanNode left;
    HuffmanNode right;

    HuffmanNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
        this.character = '\0';
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }
}

class HuffmanComparator implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode x, HuffmanNode y) {
        return x.frequency - y.frequency;
    }
}

public class HuffmanCoding {
    public static void printCodes(HuffmanNode root, String code) {
        if (root == null)
            return;
        if (root.left == null && root.right == null && root.character != '\0') {
            System.out.println(root.character + ": " + code);
            return;
        }

        printCodes(root.left, code + "0");
        printCodes(root.right, code + "1");
    }

    public static void buildHuffmanTree(char[] characters, int[] frequencies) {
        PriorityQueue<HuffmanNode> queue = new PriorityQueue<>(new HuffmanComparator());

    
        for (int i = 0; i < characters.length; i++) {
            queue.add(new HuffmanNode(characters[i], frequencies[i]));
        }
        while (queue.size() > 1) {
            HuffmanNode left = queue.poll();
            HuffmanNode right = queue.poll();

            HuffmanNode sum = new HuffmanNode(left.frequency + right.frequency, left, right);
            queue.add(sum);
        }
        HuffmanNode root = queue.peek();
        System.out.println("Character Huffman Codes:");
        printCodes(root, "");
    }

    public static void main(String[] args) {
        char[] characters = {'a', 'b', 'c', 'd', 'e', 'f'};
        int[] frequencies = {5, 9, 12, 13, 16, 45};

        buildHuffmanTree(characters, frequencies);
    }
}
