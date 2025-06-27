import java.util.*;

class AStarPathfinding {
    static class Node implements Comparable<Node> {
        int x, y;
        int gCost, hCost; 
        Node parent;

        Node(int x, int y, int gCost, int hCost, Node parent) {
            this.x = x;
            this.y = y;
            this.gCost = gCost;
            this.hCost = hCost;
            this.parent = parent;
        }

        int fCost() {
            return gCost + hCost;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.fCost(), other.fCost());
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Node)) return false;
            Node o = (Node) obj;
            return this.x == o.x && this.y == o.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    static int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    static List<Node> aStar(int[][] grid, int[] start, int[] end) {
        int rows = grid.length, cols = grid[0].length;

        PriorityQueue<Node> openSet = new PriorityQueue<>();
        Set<Node> closedSet = new HashSet<>();

        Node startNode = new Node(start[0], start[1], 0, manhattan(start[0], start[1], end[0], end[1]), null);
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (current.x == end[0] && current.y == end[1]) {
                return reconstructPath(current);
            }

            closedSet.add(current);

            for (int[] dir : directions) {
                int nx = current.x + dir[0], ny = current.y + dir[1];

                if (nx < 0 || ny < 0 || nx >= rows || ny >= cols || grid[nx][ny] == 1)
                    continue;

                Node neighbor = new Node(nx, ny,
                        current.gCost + 1,
                        manhattan(nx, ny, end[0], end[1]),
                        current);

                if (closedSet.contains(neighbor))
                    continue;

                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }
            }
        }

        return null; 
    }

    static int manhattan(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    static List<Node> reconstructPath(Node end) {
        List<Node> path = new ArrayList<>();
        for (Node at = end; at != null; at = at.parent)
            path.add(at);
        Collections.reverse(path);
        return path;
    }

    public static void main(String[] args) {
        int[][] grid = {
                {0, 0, 0, 0, 0},
                {1, 1, 0, 1, 0},
                {0, 0, 0, 0, 0},
                {0, 1, 1, 1, 0},
                {0, 0, 0, 1, 0}
        };

        int[] start = {0, 0};
        int[] end = {4, 4};

        List<Node> path = aStar(grid, start, end);

        if (path != null) {
            System.out.println("Path:");
            for (Node node : path) {
                System.out.printf("(%d, %d) ", node.x, node.y);
            }
        } else {
            System.out.println("No path found.");
        }
    }
}

