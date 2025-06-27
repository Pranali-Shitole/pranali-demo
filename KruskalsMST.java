public import java.util.*;

public class KruskalsMST {

    static class Edge implements Comparable<Edge> {
        int src, dest, weight;

        Edge(int s, int d, int w) {
            this.src = s;
            this.dest = d;
            this.weight = w;
        }

        public int compareTo(Edge other) {
            return this.weight - other.weight;
        }
    }

    static class DisjointSet {
        int[] parent, rank;

        DisjointSet(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++)
                parent[i] = i;
        }

        int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]); // Path compression
            return parent[x];
        }

        boolean union(int x, int y) {
            int xRoot = find(x);
            int yRoot = find(y);

            if (xRoot == yRoot) return false;

            // Union by rank
            if (rank[xRoot] < rank[yRoot]) {
                parent[xRoot] = yRoot;
            } else if (rank[yRoot] < rank[xRoot]) {
                parent[yRoot] = xRoot;
            } else {
                parent[yRoot] = xRoot;
                rank[xRoot]++;
            }
            return true;
        }
    }

    public static void kruskalMST(List<Edge> edges, int V) {
        Collections.sort(edges); // Sort edges by weight
        DisjointSet ds = new DisjointSet(V);

        int mstWeight = 0;
        List<Edge> mst = new ArrayList<>();

        for (Edge edge : edges) {
            if (ds.union(edge.src, edge.dest)) {
                mst.add(edge);
                mstWeight += edge.weight;
            }
        }

        System.out.println("Edges in MST:");
        for (Edge e : mst)
            System.out.println(e.src + " - " + e.dest + " : " + e.weight);
        System.out.println("Total weight of MST: " + mstWeight);
    }

    public static void main(String[] args) {
        int V = 6; // number of vertices
        List<Edge> edges = new ArrayList<>();

        edges.add(new Edge(0, 1, 4));
        edges.add(new Edge(0, 2, 4));
        edges.add(new Edge(1, 2, 2));
        edges.add(new Edge(1, 0, 4));
        edges.add(new Edge(2, 0, 4));
        edges.add(new Edge(2, 1, 2));
        edges.add(new Edge(2, 3, 3));
        edges.add(new Edge(2, 5, 2));
        edges.add(new Edge(2, 4, 4));
        edges.add(new Edge(3, 2, 3));
        edges.add(new Edge(3, 4, 3));
        edges.add(new Edge(4, 2, 4));
        edges.add(new Edge(4, 3, 3));
        edges.add(new Edge(5, 2, 2));
        edges.add(new Edge(5, 4, 3));

        kruskalMST(edges, V);
    }
}
 
