import java.util.*;

public class SimObject {
    public final String id;
    public final List<SimObject> references = new ArrayList<>();

    public SimObject(String id) {
        this.id = id;
    }

    public void addReference(SimObject obj) {
        references.add(obj);
    }

    @Override
    public String toString() {
        return id;
    }
}

public class SimulatedHeap {
    private final Map<String, SimObject> heap = new HashMap<>();
    private final Set<SimObject> roots = new HashSet<>();

    public SimObject allocate(String id) {
        SimObject obj = new SimObject(id);
        heap.put(id, obj);
        return obj;
    }

    public void addRoot(SimObject obj) {
        roots.add(obj);
    }

    public void removeRoot(SimObject obj) {
        roots.remove(obj);
    }

    public void gc() {
        System.out.println("=== Starting GC ===");
        Set<SimObject> reachable = new HashSet<>();
        for (SimObject root : roots) {
            mark(root, reachable);
        }

        Iterator<Map.Entry<String, SimObject>> iter = heap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, SimObject> entry = iter.next();
            if (!reachable.contains(entry.getValue())) {
                System.out.println("GC: Collecting " + entry.getKey());
                iter.remove();
            }
        }
        System.out.println("=== GC Complete ===");
    }

    private void mark(SimObject obj, Set<SimObject> visited) {
        if (!visited.add(obj)) return;
        for (SimObject ref : obj.references) {
            mark(ref, visited);
        }
    }

    public void printHeap() {
        System.out.println("Heap objects: " + heap.keySet());
    }
}
public class Main {
    public static void main(String[] args) {
        SimulatedHeap heap = new SimulatedHeap();
        SimObject a = heap.allocate("A");
        SimObject b = heap.allocate("B");
        SimObject c = heap.allocate("C");
        SimObject d = heap.allocate("D");
        SimObject e = heap.allocate("E");
        a.addReference(b);
        b.addReference(c);
        c.addReference(d);
        heap.addRoot(a);
        heap.addRoot(e); 

        heap.printHeap();
        heap.gc();
        heap.printHeap();
        heap.removeRoot(e);
        System.out.println("\nAfter removing root E:");
        heap.gc();
        heap.printHeap();
    }
}

