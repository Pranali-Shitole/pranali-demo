import java.util.*;

public class QuadTree {
    private static final int MAX_POINTS = 4;

    static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    static class Boundary {
        int x, y, w, h; 

        Boundary(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        boolean contains(Point p) {
            return (p.x >= x - w && p.x <= x + w &&
                    p.y >= y - h && p.y <= y + h);
        }

        boolean intersects(Boundary range) {
            return !(range.x - range.w > x + w ||
                     range.x + range.w < x - w ||
                     range.y - range.h > y + h ||
                     range.y + range.h < y - h);
        }
    }

    static class QuadTreeNode {
        Boundary boundary;
        List<Point> points;
        boolean divided;

        QuadTreeNode northeast, northwest, southeast, southwest;

        QuadTreeNode(Boundary boundary) {
            this.boundary = boundary;
            this.points = new ArrayList<>();
            this.divided = false;
        }

        void subdivide() {
            int x = boundary.x;
            int y = boundary.y;
            int w = boundary.w / 2;
            int h = boundary.h / 2;

            northeast = new QuadTreeNode(new Boundary(x + w, y - h, w, h));
            northwest = new QuadTreeNode(new Boundary(x - w, y - h, w, h));
            southeast = new QuadTreeNode(new Boundary(x + w, y + h, w, h));
            southwest = new QuadTreeNode(new Boundary(x - w, y + h, w, h));

            divided = true;
        }

        boolean insert(Point p) {
            if (!boundary.contains(p)) return false;

            if (points.size() < MAX_POINTS) {
                points.add(p);
                return true;
            } else {
                if (!divided) subdivide();

                if (northeast.insert(p)) return true;
                if (northwest.insert(p)) return true;
                if (southeast.insert(p)) return true;
                if (southwest.insert(p)) return true;
            }
            return false;
        }

        void query(Boundary range, List<Point> found) {
            if (!boundary.intersects(range)) return;

            for (Point p : points) {
                if (range.contains(p)) {
                    found.add(p);
                }
            }

            if (divided) {
                northeast.query(range, found);
                northwest.query(range, found);
                southeast.query(range, found);
                southwest.query(range, found);
            }
        }
    }
    public static void main(String[] args) {
        Boundary boundary = new Boundary(200, 200, 200, 200);
        QuadTreeNode quadTree = new QuadTreeNode(boundary);
        quadTree.insert(new Point(100, 100));
        quadTree.insert(new Point(150, 150));
        quadTree.insert(new Point(180, 80));
        quadTree.insert(new Point(220, 210));
        quadTree.insert(new Point(300, 300));
        Boundary range = new Boundary(200, 200, 100, 100);
        List<Point> foundPoints = new ArrayList<>();
        quadTree.query(range, foundPoints);

        System.out.println("Points in range (" + range.x + "," + range.y + ") ± (" + range.w + "," + range.h + "):");
        for (Point p : foundPoints) {
            System.out.println(p);
        }
    }
}

