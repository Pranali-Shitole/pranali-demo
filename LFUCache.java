import java.util.*;

class LFUCache {

    private class Node {
        int key, value, freq;
        Node prev, next;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
            this.freq = 1;
        }
    }

    private class DoublyLinkedList {
        Node head, tail;
        int size;

        DoublyLinkedList() {
            head = new Node(0, 0); 
            tail = new Node(0, 0); 
            head.next = tail;
            tail.prev = head;
        }

        void addNode(Node node) {
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
            size++;
        }

        void removeNode(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            size--;
        }

        Node removeLast() {
            if (size > 0) {
                Node node = tail.prev;
                removeNode(node);
                return node;
            }
            return null;
        }
    }

    private final int capacity;
    private int minFreq;
    private final Map<Integer, Node> keyNodeMap;
    private final Map<Integer, DoublyLinkedList> freqMap;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.minFreq = 0;
        this.keyNodeMap = new HashMap<>();
        this.freqMap = new HashMap<>();
    }

    public int get(int key) {
        if (!keyNodeMap.containsKey(key)) return -1;
        Node node = keyNodeMap.get(key);
        updateFrequency(node);
        return node.value;
    }

    public void put(int key, int value) {
        if (capacity == 0) return;

        if (keyNodeMap.containsKey(key)) {
            Node node = keyNodeMap.get(key);
            node.value = value;
            updateFrequency(node);
        } else {
            if (keyNodeMap.size() == capacity) {
                DoublyLinkedList minList = freqMap.get(minFreq);
                Node evicted = minList.removeLast();
                keyNodeMap.remove(evicted.key);
            }

            Node newNode = new Node(key, value);
            keyNodeMap.put(key, newNode);
            freqMap.computeIfAbsent(1, k -> new DoublyLinkedList()).addNode(newNode);
            minFreq = 1;
        }
    }

    private void updateFrequency(Node node) {
        int freq = node.freq;
        DoublyLinkedList list = freqMap.get(freq);
        list.removeNode(node);

        if (freq == minFreq && list.size == 0) {
            minFreq++;
        }

        node.freq++;
        freqMap.computeIfAbsent(node.freq, k -> new DoublyLinkedList()).addNode(node);
    }
    public static void main(String[] args) {
        LFUCache cache = new LFUCache(3);

        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(3, 30);

        System.out.println("Get 1: " + cache.get(1)); 
        cache.put(4, 40); 

        System.out.println("Get 2 (should be -1): " + cache.get(2)); 
        System.out.println("Get 3: " + cache.get(3)); 
        System.out.println("Get 4: " + cache.get(4)); 
    }
}

