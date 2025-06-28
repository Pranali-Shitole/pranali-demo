import java.util.*;

public class Dinicsalgorithm {
    static class Edge {
        int to, rev;
        int flow, capacity;

        Edge(int to, int rev, int capacity) {
            this.to = to;
            this.rev = rev;
            this.capacity = capacity;
        }
    }

    static class Dinic {
        int V;
        List<Edge>[] graph;
        int[] level;
        int[] ptr;
        int source, sink;

        @SuppressWarnings("unchecked")
        Dinic(int V, int source, int sink) {
            this.V = V;
            this.source = source;
            this.sink = sink;
            graph = new ArrayList[V];
            for (int i = 0; i < V; i++) {
                graph[i] = new ArrayList<>();
            }
            level = new int[V];
            ptr = new int[V];
        }

        void addEdge(int u, int v, int capacity) {
            graph[u].add(new Edge(v, graph[v].size(), capacity));
            graph[v].add(new Edge(u, graph[u].size() - 1, 0)); // reverse edge
        }

        boolean bfs() {
            Arrays.fill(level, -1);
            Queue<Integer> queue = new LinkedList<>();
            queue.add(source);
            level[source] = 0;

            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (Edge edge : graph[u]) {
                    if (edge.capacity - edge.flow > 0 && level[edge.to] == -1) {
                        level[edge.to] = level[u] + 1;
                        queue.add(edge.to);
                    }
                }
            }

            return level[sink] != -1;
        }

        int dfs(int u, int pushed) {
            if (pushed == 0) return 0;
            if (u == sink) return pushed;

            for (; ptr[u] < graph[u].size(); ptr[u]++) {
                Edge edge = graph[u].get(ptr[u]);
                if (level[edge.to] == level[u] + 1 && edge.capacity - edge.flow > 0) {
                    int tr = dfs(edge.to, Math.min(pushed, edge.capacity - edge.flow));
                    if (tr > 0) {
                        edge.flow += tr;
                        graph[edge.to].get(edge.rev).flow -= tr;
                        return tr;
                    }
                }
            }
            return 0;
        }

        int maxFlow() {
            int flow = 0;
            while (bfs()) {
                Arrays.fill(ptr, 0);
                int pushed;
                while ((pushed = dfs(source, Integer.MAX_VALUE)) != 0) {
                    flow += pushed;
                }
            }
            return flow;
        }
    }
    public static void main(String[] args) {
        int V = 6;
        int source = 0;
        int sink = 5;

        Dinic dinic = new Dinic(V, source, sink);

        // Sample graph
        dinic.addEdge(0, 1, 16);
        dinic.addEdge(0, 2, 13);
        dinic.addEdge(1, 2, 10);
        dinic.addEdge(1, 3, 12);
        dinic.addEdge(2, 1, 4);
        dinic.addEdge(2, 4, 14);
        dinic.addEdge(3, 2, 9);
        dinic.addEdge(3, 5, 20);
        dinic.addEdge(4, 3, 7);
        dinic.addEdge(4, 5, 4);

        System.out.println("The maximum possible flow is " + dinic.maxFlow());
    }
}

