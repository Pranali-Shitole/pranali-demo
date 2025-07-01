import java.util.*;

public class KMeansClustering {
    static final int K = 3; 
    static final int MAX_ITERATIONS = 100;

    static class Point {
        double x, y;
        int cluster = -1;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        double distance(Point other) {
            return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
        }

        @Override
        public String toString() {
            return String.format("(%.2f, %.2f) -> Cluster %d", x, y, cluster);
        }
    }

    public static void main(String[] args) {
        List<Point> points = Arrays.asList(
            new Point(1.0, 2.0), new Point(1.5, 1.8),
            new Point(5.0, 8.0), new Point(6.0, 9.0),
            new Point(9.0, 10.0), new Point(8.0, 9.5),
            new Point(1.2, 0.8), new Point(0.5, 2.2),
            new Point(7.5, 8.5), new Point(3.0, 3.5)
        );

        List<Point> centroids = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < K; i++) {
            Point randomPoint = points.get(rand.nextInt(points.size()));
            centroids.add(new Point(randomPoint.x, randomPoint.y));
        }

        boolean changed = true;
        int iterations = 0;

        while (changed && iterations < MAX_ITERATIONS) {
            changed = false;
            for (Point p : points) {
                int nearest = 0;
                double minDist = p.distance(centroids.get(0));

                for (int i = 1; i < K; i++) {
                    double dist = p.distance(centroids.get(i));
                    if (dist < minDist) {
                        minDist = dist;
                        nearest = i;
                    }
                }

                if (p.cluster != nearest) {
                    changed = true;
                    p.cluster = nearest;
                }
            }
            for (int i = 0; i < K; i++) {
                double sumX = 0, sumY = 0;
                int count = 0;
                for (Point p : points) {
                    if (p.cluster == i) {
                        sumX += p.x;
                        sumY += p.y;
                        count++;
                    }
                }
                if (count > 0) {
                    centroids.get(i).x = sumX / count;
                    centroids.get(i).y = sumY / count;
                }
            }

            iterations++;
        }
        System.out.println("K-Means clustering complete in " + iterations + " iterations.");
        for (Point p : points) {
            System.out.println(p);
        }
        System.out.println("\nFinal centroids:");
        for (int i = 0; i < K; i++) {
            System.out.printf("Centroid %d: (%.2f, %.2f)%n", i, centroids.get(i).x, centroids.get(i).y);
        }
    }
}

