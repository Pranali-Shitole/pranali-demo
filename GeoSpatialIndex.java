import java.util.*;

public class GeoSpatialIndex {

    public static void main(String[] args) {
        Quadtree quadtree = new Quadtree(new BoundingBox(-180, -90, 180, 90), 4);
        quadtree.insert(new GeoPoint("A", 37.7749, -122.4194)); 
        quadtree.insert(new GeoPoint("B", 34.0522, -118.2437)); 
        quadtree.insert(new GeoPoint("C", 40.7128, -74.0060));  
        quadtree.insert(new GeoPoint("D", 41.8781, -87.6298));  
        BoundingBox queryBox = new BoundingBox(-125, 30, -100, 40); 
        List<GeoPoint> results = quadtree.query(queryBox);

        System.out.println("Points in bounding box:");
        for (GeoPoint p : results) {
            System.out.println(p);
        }
    }
}

class GeoPoint {
    String id;
    double lat;
    double lon;

    GeoPoint(String id, double lat, double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public String toString() {
        return id + " (" + lat + ", " + lon + ")";
    }
}
class BoundingBox {
    double minX, minY, maxX, maxY;

    BoundingBox(double minX, double minY, double maxX, double maxY) {
        this.minX = minX; this.minY = minY;
        this.maxX = maxX; this.maxY = maxY;
    }

    boolean contains(GeoPoint p) {
        return p.lon >= minX && p.lon <= maxX &&
               p.lat >= minY && p.lat <= maxY;
    }

    boolean intersects(BoundingBox other) {
        return !(other.minX > maxX || other.maxX < minX ||
                 other.minY > maxY || other.maxY < minY);
    }
}
class Quadtree {
    private final BoundingBox boundary;
    private final int capacity;
    private final List<GeoPoint> points;
    private boolean divided;

    private Quadtree northeast;
    private Quadtree northwest;
    private Quadtree southeast;
    private Quadtree southwest;

    Quadtree(BoundingBox boundary, int capacity) {
        this.boundary = boundary;
        this.capacity = capacity;
        this.points = new ArrayList<>();
        this.divided = false;
    }

    public boolean insert(GeoPoint point) {
        if (!boundary.contains(point)) return false;

        if (points.size() < capacity) {
            points.add(point);
            return true;
        } else {
            if (!divided) subdivide();
            return (northeast.insert(point) || northwest.insert(point) ||
                    southeast.insert(point) || southwest.insert(point));
        }
    }

    private void subdivide() {
        double midX = (boundary.minX + boundary.maxX) / 2;
        double midY = (boundary.minY + boundary.maxY) / 2;

        northeast = new Quadtree(new BoundingBox(midX, midY, boundary.maxX, boundary.maxY), capacity);
        northwest = new Quadtree(new BoundingBox(boundary.minX, midY, midX, boundary.maxY), capacity);
        southeast = new Quadtree(new BoundingBox(midX, boundary.minY, boundary.maxX, midY), capacity);
        southwest = new Quadtree(new BoundingBox(boundary.minX, boundary.minY, midX, midY), capacity);

        divided = true;
    }

    public List<GeoPoint> query(BoundingBox range) {
        List<GeoPoint> found = new ArrayList<>();

        if (!boundary.intersects(range)) return found;

        for (GeoPoint p : points) {
            if (range.contains(p)) {
                found.add(p);
            }
        }

        if (divided) {
            found.addAll(northeast.query(range));
            found.addAll(northwest.query(range));
            found.addAll(southeast.query(range));
            found.addAll(southwest.query(range));
        }

        return found;
    }
}
