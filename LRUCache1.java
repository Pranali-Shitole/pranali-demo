import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache1<K, V> {
    private final int capacity;
    private final Map<K, V> cacheMap;

    public LRUCache1(final int capacity) {
        this.capacity = capacity;
        this.cacheMap = new LinkedHashMap<K, V>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > LRUCache1.this.capacity;
            }
        };
    }

    public synchronized V get(K key) {
        return cacheMap.get(key);
    }

    public synchronized void put(K key, V value) {
        cacheMap.put(key, value);
    }

    public synchronized boolean containsKey(K key) {
        return cacheMap.containsKey(key);
    }

    public synchronized void remove(K key) {
        cacheMap.remove(key);
    }

    public synchronized int size() {
        return cacheMap.size();
    }

    public synchronized void clear() {
        cacheMap.clear();
    }

    public synchronized String toString() {
        return cacheMap.toString();
    }
}

