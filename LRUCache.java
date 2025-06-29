import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;
    public LRUCache(int capacity) {
        super(capacity, 0.75f, true); 
        this.capacity = capacity;
    }
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
    public static void main(String[] args) {
        LRUCache<Integer, String> cache = new LRUCache<>(3);

        cache.put(1, "One");
        cache.put(2, "Two");
        cache.put(3, "Three");

        System.out.println("Cache: " + cache);
        cache.get(2);
        System.out.println("Accessed key 2: " + cache);
        cache.put(4, "Four");
        System.out.println("After adding key 4 (evicts key 1): " + cache);
        cache.get(3);
        System.out.println("Accessed key 3: " + cache);
        cache.put(5, "Five");
        System.out.println("After adding key 5 (evicts key 2): " + cache);
    }
}

