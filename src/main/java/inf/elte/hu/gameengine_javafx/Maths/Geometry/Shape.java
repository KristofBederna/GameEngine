package inf.elte.hu.gameengine_javafx.Maths.Geometry;

import java.util.List;

public class Shape {
    protected List<Point> points;
    protected List<Edge> edges;

    public Shape(List<Point> points) {
        this.points = points;
        updateEdges();
    }

    private void updateEdges() {
        edges = List.of(new Edge(points.get(0), points.get(1)), new Edge(points.get(1), points.get(2)), new Edge(points.get(2), points.get(0)));
    }

    public List<Edge> getEdges() {
        return edges;
    }
    public List<Point> getPoints() {
        return points;
    }

    public static boolean intersect(Shape a, Shape b) {
        for (Edge edge1 : a.getEdges()) {
            for (Edge edge2 : b.getEdges()) {
                if (doIntersect(edge1.getBeginning(), edge1.getEnd(), edge2.getBeginning(), edge2.getEnd())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean doIntersect(Point p1, Point p2, Point q1, Point q2) {
        // Find the four orientations needed for general and special cases
        int o1 = orientation(p1, p2, q1);
        int o2 = orientation(p1, p2, q2);
        int o3 = orientation(q1, q2, p1);
        int o4 = orientation(q1, q2, p2);

        // General case
        if (o1 != o2 && o3 != o4) {
            return true;
        }

        // Special cases: check if the points are collinear and on the segment
        if (o1 == 0 && onSegment(p1, q1, p2)) return true;
        if (o2 == 0 && onSegment(p1, q2, p2)) return true;
        if (o3 == 0 && onSegment(q1, p1, q2)) return true;
        if (o4 == 0 && onSegment(q1, p2, q2)) return true;

        return false; // Doesn't intersect
    }

    // Function to calculate the orientation of the triplet (p, q, r)
    private static int orientation(Point p, Point q, Point r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX()) - (q.getX() - p.getX()) * (r.getY() - q.getY());
        if (Math.abs(val) < 1e-9) {
            return 0; // Collinear
        } else if (val > 0) {
            return 1; // Clockwise
        } else {
            return 2; // Counterclockwise
        }
    }

    // Function to check if point q lies on the segment pr
    private static boolean onSegment(Point p, Point q, Point r) {
        return q.getX() <= Math.max(p.getX(), r.getX()) && q.getX() >= Math.min(p.getX(), r.getX())
                && q.getY() <= Math.max(p.getY(), r.getY()) && q.getY() >= Math.min(p.getY(), r.getY());
    }
}
