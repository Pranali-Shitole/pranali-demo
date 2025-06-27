import java.util.*;

public class KosarajuSCC {

    private int V;  
    private List<List<Integer>> adj;       
    private List<List<Integer>> revAdj;    

    KosarajuSCC(int v) {
        V = v;
        adj = new ArrayList<>();
        revAdj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
            revAdj.add(new ArrayList<>());
        }
    }
    void addEdge(int u, int v) {
        adj.get(u).add(v);
        revAdj.get(v).add(u);  
    }
    private void fillOrder(int v, boolean[] visited, Stack<Integer> stack) {
        visited[v] = true;
        for (int n : adj.get(v)) {
            if (!visited[n]) fillOrder(n, visited, stack);
        }
        stack.push(v);
    }
    private void dfsOnTranspose(int v, boolean[] visited, List<Integer> component) {
        visited[v] = true;
        component.add(v);
        for (int n : revAdj.get(v)) {
            if (!visited[n]) dfsOnTranspose(n, visited, component);
        }
    }
    void printSCCs() {
        Stack<Integer> stack = new Stack<>();
        boolean[] visited = new boolean[V];
        for (int i = 0; i < V; i++) {
            if (!visited[i]) fillOrder(i, visited, stack);
        }
        Arrays.fill(visited, false);
        System.out.println("Strongly Connected Components:");
        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (!visited[v]) {
                List<Integer> component = new ArrayList<>();
                dfsOnTranspose(v, visited, component);
                System.out.println(component);
            }
        }
    }
    public static void main(String[] args) {
        KosarajuSCC g = new KosarajuSCC(8);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        g.addEdge(3, 1);
        g.addEdge(3, 2);
        g.addEdge(4, 3);
        g.addEdge(4, 5);
        g.addEdge(5, 4);
        g.addEdge(6, 5);
        g.addEdge(6, 7);

        g.printSCCs();
    }
}

