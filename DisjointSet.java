
public class DisjointSet {
    private final int[] parent;
    private final int[] rank;
    public DisjointSet(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }
    public int find(int x) {
        if (parent[x] != x)
            parent[x] = find(parent[x]);  
        return parent[x];
    }
    public void union(int x, int y) {
        int xRoot = find(x);
        int yRoot = find(y);

        if (xRoot == yRoot)
            return;
        if (rank[xRoot] < rank[yRoot]) {
            parent[xRoot] = yRoot;
        } else if (rank[xRoot] > rank[yRoot]) {
            parent[yRoot] = xRoot;
        } else {
            parent[yRoot] = xRoot;
            rank[xRoot]++;
        }
    }
    public void displaySets() {
        System.out.println("Element : Set Representative");
        for (int i = 0; i < parent.length; i++) {
            System.out.println("   " + i + "    :     " + find(i));
        }
    }

    public static void main(String[] args) {
        DisjointSet ds = new DisjointSet(7);

        ds.union(0, 1);
        ds.union(1, 2);
        ds.union(3, 4);
        ds.union(5, 6);
        ds.union(4, 5);
        ds.union(2, 6);

        ds.displaySets();
    }
}

