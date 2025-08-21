import java.util.*;

public class ExpiringLRUCache<K, V> {
    private final int maxSize;
    private final long ttlMillis;

    private final Map<K, CacheEntry<V>> cache;

    public ExpiringLRUCache(int maxSize, long ttlMillis) {
        this.maxSize = maxSize;
        this.ttlMillis = ttlMillis;
        this.cache = new LinkedHashMap<>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, CacheEntry<V>> eldest) {
                return size() > ExpiringLRUCache.this.maxSize;
            }
        };
    }

    public synchronized void put(K key, V value) {
        cache.put(key, new CacheEntry<>(value, System.currentTimeMillis()));
    }

    public synchronized V get(K key) {
        CacheEntry<V> entry = cache.get(key);

        if (entry == null) return null;
        if (System.currentTimeMillis() - entry.timestamp > ttlMillis) {
            cache.remove(key);
            return null;
        }

        return entry.value;
    }

    public synchronized int size() {
        cleanUp();
        return cache.size();
    }
    private void cleanUp() {
        Iterator<Map.Entry<K, CacheEntry<V>>> it = cache.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<K, CacheEntry<V>> entry = it.next();
            if (System.currentTimeMillis() - entry.getValue().timestamp > ttlMillis) {
                it.remove();
            }
        }
    }

    private static class CacheEntry<V> {
        V value;
        long timestamp;

        CacheEntry(V value, long timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }
    }
    public static void main(String[] args) throws InterruptedException {
        ExpiringLRUCache<String, String> cache = new ExpiringLRUCache<>(3, 3000); // 3 sec TTL

        cache.put("a", "Apple");
        Thread.sleep(1000);
        cache.put("b", "Banana");
        Thread.sleep(1000);
        cache.put("c", "Cherry");

        System.out.println("Initial Cache:");
        System.out.println("a = " + cache.get("a")); 
        System.out.println("b = " + cache.get("b")); 
        System.out.println("c = " + cache.get("c")); 

        Thread.sleep(2000); 

        System.out.println("\nAfter 2 more seconds:");
        System.out.println("a = " + cache.get("a")); 
        System.out.println("b = " + cache.get("b")); 
        System.out.println("c = " + cache.get("c")); 

        cache.put("d", "Date"); 

        System.out.println("\nAfter inserting 'd':");
        System.out.println("Cache size: " + cache.size());
        System.out.println("b = " + cache.get("b"));
        System.out.println("c = " + cache.get("c"));
        System.out.println("d = " + cache.get("d"));
    }
}
