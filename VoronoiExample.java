import org.locationtech.jts.geom.*;
import org.locationtech.jts.triangulate.VoronoiDiagramBuilder;

import java.util.ArrayList;
import java.util.List;

public class VoronoiExample {
    public static void main(String[] args) {
        GeometryFactory gf = new GeometryFactory();
        List<Coordinate> coords = new ArrayList<>();

    
        coords.add(new Coordinate(100, 100));
        coords.add(new Coordinate(200, 200));
        coords.add(new Coordinate(300, 100));
        coords.add(new Coordinate(150, 300));

        MultiPoint sites = gf.createMultiPointFromCoords(coords.toArray(new Coordinate[0]));
        VoronoiDiagramBuilder builder = new VoronoiDiagramBuilder();
        builder.setSites(sites);
        builder.setTolerance(0.0001);
        Geometry diagram = builder.getDiagram(gf);
        for (int i = 0; i < diagram.getNumGeometries(); i++) {
            Polygon poly = (Polygon) diagram.getGeometryN(i);
            System.out.println("Voronoi cell " + i + ": " + poly);
        }
    }
}

