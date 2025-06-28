import java.util.*;

class SuffixTreeNode {
    Map<Character, SuffixTreeNode> children = new HashMap<>();
    List<Integer> indexes = new ArrayList<>();

    void insertSuffix(String s, int index) {
        indexes.add(index);
        if (s.length() > 0) {
            char firstChar = s.charAt(0);
            SuffixTreeNode child = children.get(firstChar);
            if (child == null) {
                child = new SuffixTreeNode();
                children.put(firstChar, child);
            }
            child.insertSuffix(s.substring(1), index);
        }
    }

    List<Integer> search(String pattern) {
        if (pattern.length() == 0) return indexes;
        char firstChar = pattern.charAt(0);
        SuffixTreeNode child = children.get(firstChar);
        if (child != null) {
            return child.search(pattern.substring(1));
        } else {
            return null;
        }
    }
}

class SuffixTree {
    SuffixTreeNode root = new SuffixTreeNode();
    String text;

    SuffixTree(String text) {
        this.text = text;
        for (int i = 0; i < text.length(); i++) {
            root.insertSuffix(text.substring(i), i);
        }
    }

    void search(String pattern) {
        List<Integer> result = root.search(pattern);
        if (result != null) {
            System.out.println("Pattern found at indexes: " + result);
        } else {
            System.out.println("Pattern not found.");
        }
    }
}

public class SuffixTreeDemo {
    public static void main(String[] args) {
        String text = "banana";
        SuffixTree tree = new SuffixTree(text);

        tree.search("ana");    
        tree.search("nana");  
        tree.search("apple");  
    }
}

