import java.util.concurrent.ConcurrentHashMap;

public class InMemoryKeyValueStore<K, V> {
    private final ConcurrentHashMap<K, V> store;

    public InMemoryKeyValueStore() {
        this.store = new ConcurrentHashMap<>();
    }

    public void put(K key, V value) {
        store.put(key, value);
    }

    public V get(K key) {
        return store.get(key);
    }

    public V delete(K key) {
        return store.remove(key);
    }

    public boolean containsKey(K key) {
        return store.containsKey(key);
    }

    public int size() {
        return store.size();
    }

    public void clear() {
        store.clear();
    }

    @Override
    public String toString() {
        return store.toString();
    }
}
public class Main {
    public static void main(String[] args) {
        InMemoryKeyValueStore<String, String> kvStore = new InMemoryKeyValueStore<>();

        kvStore.put("user:1", "Alice");
        kvStore.put("user:2", "Bob");

        System.out.println("user:1 = " + kvStore.get("user:1"));  
        System.out.println("user:2 = " + kvStore.get("user:2")); 

        kvStore.delete("user:1");
        System.out.println("user:1 exists? " + kvStore.containsKey("user:1"));  

        System.out.println("Store contents: " + kvStore);
    }
}
