import java.util.*;

public class TarjanSCC {

    private int time = 0;
    private List<List<Integer>> sccList = new ArrayList<>();
    private Stack<Integer> stack = new Stack<>();
    private boolean[] onStack;
    private int[] low;
    private int[] ids;

    public List<List<Integer>> findSCCs(List<List<Integer>> graph) {
        int n = graph.size();
        onStack = new boolean[n];
        low = new int[n];
        ids = new int[n];
        Arrays.fill(ids, -1); 

        for (int i = 0; i < n; i++) {
            if (ids[i] == -1) {
                dfs(i, graph);
            }
        }

        return sccList;
    }

    private void dfs(int at, List<List<Integer>> graph) {
        stack.push(at);
        onStack[at] = true;
        ids[at] = low[at] = time++;

        for (int to : graph.get(at)) {
            if (ids[to] == -1) {
                dfs(to, graph);
                low[at] = Math.min(low[at], low[to]);
            } else if (onStack[to]) {
                low[at] = Math.min(low[at], ids[to]);
            }
        }
        if (ids[at] == low[at]) {
            List<Integer> scc = new ArrayList<>();
            while (true) {
                int node = stack.pop();
                onStack[node] = false;
                scc.add(node);
                if (node == at) break;
            }
            sccList.add(scc);
        }
    }

    public static void main(String[] args) {
        int V = 8;
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < V; i++) graph.add(new ArrayList<>());
        graph.get(0).add(1);
        graph.get(1).add(2);
        graph.get(2).add(0);
        graph.get(3).add(1);
        graph.get(3).add(2);
        graph.get(4).add(3);
        graph.get(4).add(5);
        graph.get(5).add(4);
        graph.get(6).add(5);
        graph.get(6).add(7);

        TarjanSCC tarjan = new TarjanSCC();
        List<List<Integer>> sccs = tarjan.findSCCs(graph);

        System.out.println("Strongly Connected Components:");
        for (List<Integer> scc : sccs) {
            System.out.println(scc);
        }
    }
}
