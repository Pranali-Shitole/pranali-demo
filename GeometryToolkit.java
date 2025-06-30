import java.util.*;

public class GeometryToolkit {
    public static class Point implements Comparable<Point> {
        public double x, y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public int compareTo(Point p) {
            if (this.y != p.y) return Double.compare(this.y, p.y);
            return Double.compare(this.x, p.x);
        }

        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
    public static int orientation(Point a, Point b, Point c) {
        double val = (b.y - a.y) * (c.x - b.x) - (b.x - a.x) * (c.y - b.y);
        if (val == 0) return 0;
        return (val > 0) ? 1 : -1;
    }
    public static List<Point> convexHull(List<Point> points) {
        if (points.size() <= 3) return new ArrayList<>(points);
        points.sort(Point::compareTo);
        Point base = points.get(0);

        points.sort((a, b) -> {
            int o = orientation(base, a, b);
            if (o == 0) {
                double distA = distanceSq(base, a);
                double distB = distanceSq(base, b);
                return Double.compare(distA, distB);
            }
            return -o; 
        });

        Stack<Point> hull = new Stack<>();
        for (Point p : points) {
            while (hull.size() >= 2 && orientation(hull.get(hull.size() - 2), hull.peek(), p) != -1) {
                hull.pop();
            }
            hull.push(p);
        }

        return new ArrayList<>(hull);
    }
    public static boolean doIntersect(Point p1, Point q1, Point p2, Point q2) {
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);
        if (o1 != o2 && o3 != o4) return true;
        return (o1 == 0 && onSegment(p1, p2, q1)) ||
               (o2 == 0 && onSegment(p1, q2, q1)) ||
               (o3 == 0 && onSegment(p2, p1, q2)) ||
               (o4 == 0 && onSegment(p2, q1, q2));
    }
    public static double polygonArea(List<Point> polygon) {
        int n = polygon.size();
        double area = 0;
        for (int i = 0; i < n; i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % n);
            area += (p1.x * p2.y - p2.x * p1.y);
        }
        return Math.abs(area) / 2.0;
    }
    public static boolean onSegment(Point p, Point q, Point r) {
        return q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
               q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y);
    }
    public static double distanceSq(Point a, Point b) {
        return (a.x - b.x)*(a.x - b.x) + (a.y - b.y)*(a.y - b.y);
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

        System.out.println("Convex Hull:");
        List<Point> hull = convexHull(points);
        for (Point p : hull) {
            System.out.println(p);
        }

        System.out.println("\nIntersection Test:");
        Point p1 = new Point(1, 1), q1 = new Point(4, 4);
        Point p2 = new Point(1, 8), q2 = new Point(2, 4);
        System.out.println("Do they intersect? " + doIntersect(p1, q1, p2, q2));

        System.out.println("\nPolygon Area:");
        System.out.println("Area: " + polygonArea(hull));
    }
}

