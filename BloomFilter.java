import java.util.BitSet;

public class BloomFilter {
    private final int size;
    private final int hashCount;
    private final BitSet bitSet;

    public BloomFilter(int size, int hashCount) {
        this.size = size;
        this.hashCount = hashCount;
        this.bitSet = new BitSet(size);
    }
    private int getHash(String item, int seed) {
        int hash = item.hashCode() ^ seed;
        hash = (hash < 0) ? -hash : hash;
        return hash % size;
    }
    public void add(String item) {
        for (int i = 0; i < hashCount; i++) {
            int hash = getHash(item, i);
            bitSet.set(hash);
        }
    }
    public boolean contains(String item) {
        for (int i = 0; i < hashCount; i++) {
            int hash = getHash(item, i);
            if (!bitSet.get(hash)) {
                return false; 
            }
        }
        return true; 
    }
    public static void main(String[] args) {
        BloomFilter filter = new BloomFilter(1000, 3); 

        String[] wordsToAdd = {"apple", "banana", "grape", "mango"};
        String[] testWords = {"apple", "grape", "peach", "orange"};

        for (String word : wordsToAdd) {
            filter.add(word);
        }

        System.out.println("Testing words:");
        for (String word : testWords) {
            System.out.println(word + " => " + (filter.contains(word) ? "Possibly present" : "Definitely not present"));
        }
    }
}

