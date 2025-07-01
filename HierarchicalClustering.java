import java.util.*;

public class HierarchicalClustering {

    static class Point {
        double x, y;
        String label;

        Point(double x, double y, String label) {
            this.x = x;
            this.y = y;
            this.label = label;
        }

        double distance(Point other) {
            return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
        }

        @Override
        public String toString() {
            return label + "(" + x + "," + y + ")";
        }
    }

    static class Cluster {
        List<Point> points = new ArrayList<>();
        String name;

        Cluster(Point p) {
            points.add(p);
            name = p.label;
        }

        Cluster(Cluster c1, Cluster c2) {
            points.addAll(c1.points);
            points.addAll(c2.points);
            name = "(" + c1.name + "+" + c2.name + ")";
        }

        double distance(Cluster other) {
            double minDist = Double.MAX_VALUE;
            for (Point p1 : points) {
                for (Point p2 : other.points) {
                    minDist = Math.min(minDist, p1.distance(p2));
                }
            }
            return minDist;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) {
        List<Point> data = List.of(
            new Point(1.0, 1.0, "A"),
            new Point(1.5, 1.5, "B"),
            new Point(5.0, 5.0, "C"),
            new Point(3.0, 4.5, "D"),
            new Point(4.0, 4.0, "E")
        );

        List<Cluster> clusters = new ArrayList<>();
        for (Point p : data) {
            clusters.add(new Cluster(p));
        }

        int step = 1;
        while (clusters.size() > 1) {
            double minDistance = Double.MAX_VALUE;
            int mergeA = -1, mergeB = -1;
            for (int i = 0; i < clusters.size(); i++) {
                for (int j = i + 1; j < clusters.size(); j++) {
                    double dist = clusters.get(i).distance(clusters.get(j));
                    if (dist < minDistance) {
                        minDistance = dist;
                        mergeA = i;
                        mergeB = j;
                    }
                }
            }
            Cluster c1 = clusters.remove(Math.max(mergeA, mergeB));
            Cluster c2 = clusters.remove(Math.min(mergeA, mergeB));
            Cluster merged = new Cluster(c1, c2);
            clusters.add(merged);

            System.out.printf("Step %d: Merged %s and %s -> %s [Distance: %.2f]%n",
                    step++, c1, c2, merged, minDistance);
        }

        System.out.println("\nFinal cluster: " + clusters.get(0));
    }
}

