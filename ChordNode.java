import java.util.*;

public class ChordNode {
    private static final int M = 5; 
    private static final int RING_SIZE = (int) Math.pow(2, M);

    public int id;
    public ChordNode successor;
    public ChordNode predecessor;
    public ChordNode[] fingerTable;

    public ChordNode(int id) {
        this.id = id;
        this.successor = this;
        this.predecessor = null;
        this.fingerTable = new ChordNode[M];
        Arrays.fill(fingerTable, this);
    }

    public void join(ChordNode knownNode) {
        if (knownNode != null) {
            this.initFingerTable(knownNode);
            this.updateOthers();
        } else {
            for (int i = 0; i < M; i++) {
                fingerTable[i] = this;
            }
            predecessor = this;
            successor = this;
        }
    }

    private void initFingerTable(ChordNode knownNode) {
        fingerTable[0] = knownNode.findSuccessor((id + 1) % RING_SIZE);
        successor = fingerTable[0];
        predecessor = successor.predecessor;
        successor.predecessor = this;

        for (int i = 0; i < M - 1; i++) {
            int start = (id + (int)Math.pow(2, i + 1)) % RING_SIZE;
            if (inInterval(start, id, fingerTable[i].id)) {
                fingerTable[i + 1] = fingerTable[i];
            } else {
                fingerTable[i + 1] = knownNode.findSuccessor(start);
            }
        }
    }

    private void updateOthers() {
    }

    public ChordNode findSuccessor(int key) {
        if (inInterval(key, id, successor.id) || key == successor.id)
            return successor;
        else {
            ChordNode n0 = closestPrecedingNode(key);
            if (n0 == this)
                return this;
            return n0.findSuccessor(key);
        }
    }

    private ChordNode closestPrecedingNode(int key) {
        for (int i = M - 1; i >= 0; i--) {
            if (inInterval(fingerTable[i].id, id, key))
                return fingerTable[i];
        }
        return this;
    }

    private boolean inInterval(int key, int start, int end) {
        if (start < end)
            return key > start && key < end;
        else
            return key > start || key < end;
    }

    public void printFingerTable() {
        System.out.println("Node " + id + " Finger Table:");
        for (int i = 0; i < M; i++) {
            int start = (id + (int)Math.pow(2, i)) % RING_SIZE;
            System.out.printf("  Start: %2d | Node: %2d\n", start, fingerTable[i].id);
        }
        System.out.println("Successor: " + successor.id);
        System.out.println("Predecessor: " + (predecessor != null ? predecessor.id : "null"));
        System.out.println();
    }
}


public class ChordSimulation {
    public static void main(String[] args) {
        List<ChordNode> nodes = new ArrayList<>();
        ChordNode node0 = new ChordNode(1);
        node0.join(null);
        nodes.add(node0);
        int[] ids = {8, 14, 21};
        for (int id : ids) {
            ChordNode newNode = new ChordNode(id);
            newNode.join(nodes.get(0));
            nodes.add(newNode);
        }
        for (ChordNode node : nodes) {
            node.printFingerTable();
        }
        int key = 10;
        ChordNode nodeResponsible = node0.findSuccessor(key);
        System.out.println("Node responsible for key " + key + " is: " + nodeResponsible.id);
    }
}
