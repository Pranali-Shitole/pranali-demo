import java.util.*;

public class MemoryObject {
    public final String id;
    public final List<MemoryObject> references = new ArrayList<>();
    public boolean marked = false; 

    public MemoryObject(String id) {
        this.id = id;
    }

    public void addReference(MemoryObject obj) {
        references.add(obj);
    }

    public void removeReference(MemoryObject obj) {
        references.remove(obj);
    }

    @Override
    public String toString() {
        return id;
    }
}

public class MemoryManager {
    private final Map<String, MemoryObject> heap = new LinkedHashMap<>();
    private final Set<MemoryObject> roots = new HashSet<>();

    public MemoryObject allocate(String id) {
        MemoryObject obj = new MemoryObject(id);
        heap.put(id, obj);
        return obj;
    }

    public void addRoot(MemoryObject obj) {
        roots.add(obj);
    }

    public void removeRoot(MemoryObject obj) {
        roots.remove(obj);
    }

    public void gc() {
        for (MemoryObject obj : heap.values()) {
            obj.marked = false;
        }

        for (MemoryObject root : roots) {
            mark(root);
        }

        heap.entrySet().removeIf(entry -> {
            if (!entry.getValue().marked) {
                System.out.println("GC: Collecting " + entry.getKey());
                return true;
            }
            return false;
        });
    }

    private void mark(MemoryObject obj) {
        if (obj.marked) return;
        obj.marked = true;
        for (MemoryObject ref : obj.references) {
            mark(ref);
        }
    }

    public Collection<MemoryObject> getHeapObjects() {
        return heap.values();
    }

    public Set<MemoryObject> getRoots() {
        return roots;
    }
}

public class MemoryVisualizer extends JPanel {
    private final MemoryManager memoryManager;

    public MemoryVisualizer(MemoryManager memoryManager) {
        this.memoryManager = memoryManager;
        setPreferredSize(new Dimension(600, 400));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        List<MemoryObject> objects = memoryManager.getHeapObjects().stream().toList();
        int x = 50;
        int y = 100;

        for (MemoryObject obj : objects) {
            g.setColor(memoryManager.getRoots().contains(obj) ? Color.GREEN : (obj.marked ? Color.CYAN : Color.LIGHT_GRAY));
            g.fillRect(x, y, 80, 40);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, 80, 40);
            g.drawString(obj.id, x + 20, y + 25);

            int refX = x + 40;
            int refY = y + 40;

            for (MemoryObject ref : obj.references) {
                int targetIndex = objects.indexOf(ref);
                if (targetIndex >= 0) {
                    int targetX = 50 + targetIndex * 100 + 40;
                    int targetY = y + 40;
                    g.drawLine(refX, refY, targetX, targetY);
                }
            }

            x += 100;
        }
    }

    public void refresh() {
        repaint();
    }
}

public class Main {
    public static void main(String[] args) {
        MemoryManager manager = new MemoryManager();
        MemoryVisualizer visualizer = new MemoryVisualizer(manager);

        JFrame frame = new JFrame("Memory Management Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(visualizer);
        frame.pack();
        frame.setVisible(true);
        MemoryObject a = manager.allocate("A");
        MemoryObject b = manager.allocate("B");
        MemoryObject c = manager.allocate("C");
        MemoryObject d = manager.allocate("D");

        a.addReference(b);
        b.addReference(c);

        manager.addRoot(a);
        manager.addRoot(d);

        visualizer.refresh();
        JButton gcButton = new JButton("Run GC");
        gcButton.addActionListener(e -> {
            manager.gc();
            visualizer.refresh();
        });

        frame.getContentPane().add(gcButton, "South");
        new Timer(3000, e -> {
            manager.removeRoot(d);
            System.out.println("Removed D from roots.");
            visualizer.refresh();
        }).start();
    }
}

