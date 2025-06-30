import java.util.*;

public class ConvexHull {

    static class Point implements Comparable<Point> {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public int compareTo(Point p) {
            if (this.y != p.y)
                return this.y - p.y;
            return this.x - p.x;
        }

        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
    static int cross(Point a, Point b, Point c) {
        int y1 = b.y - a.y;
        int x1 = b.x - a.x;
        int y2 = c.y - a.y;
        int x2 = c.x - a.x;
        return x1 * y2 - x2 * y1;
    }

    public static List<Point> convexHull(List<Point> points) {
        int n = points.size();
        if (n < 3) return points;
        Collections.sort(points);
        Point start = points.get(0);
        points.sort((a, b) -> {
            int cp = cross(start, a, b);
            if (cp == 0) {
                int distA = (a.x - start.x) * (a.x - start.x) + (a.y - start.y) * (a.y - start.y);
                int distB = (b.x - start.x) * (b.x - start.x) + (b.y - start.y) * (b.y - start.y);
                return Integer.compare(distA, distB);
            }
            return -Integer.compare(cp, 0); 
        });
        Stack<Point> hull = new Stack<>();
        for (Point p : points) {
            while (hull.size() >= 2 && cross(hull.get(hull.size() - 2), hull.peek(), p) <= 0) {
                hull.pop();
            }
            hull.push(p);
        }

        return new ArrayList<>(hull);
    }

    public static void main(String[] args) {
        List<Point> points = Arrays.asList(
                new Point(0, 3),
                new Point(2, 2),
                new Point(1, 1),
                new Point(2, 1),
                new Point(3, 0),
                new Point(0, 0),
                new Point(3, 3)
        );

        List<Point> hull = convexHull(points);

        System.out.println("Convex Hull:");
        for (Point p : hull) {
            System.out.println(p);
        }
    }
}

