package inf.elte.hu.gameengine_javafx.Maths.Geometry;

import java.util.List;

public class Shape {
    protected List<Point> points;
    protected List<Edge> edges;

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
        // Check if the two segments are parallel
        if (areParallel(p1, p2, q1, q2)) {
            return false; // Parallel segments don't intersect in this case
        }

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

        return false;
    }

    private static boolean areParallel(Point p1, Point p2, Point q1, Point q2) {
        double crossProduct = (p2.getX() - p1.getX()) * (q2.getY() - q1.getY())
                - (p2.getY() - p1.getY()) * (q2.getX() - q1.getX());
        return Math.abs(crossProduct) < 1e-9;
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

    public static Point getIntersection(Line line, Edge edge) {
        Point p1 = line.getEdges().getFirst().getBeginning();
        Point p2 = line.getEdges().getFirst().getEnd();
        Point q1 = edge.getBeginning();
        Point q2 = edge.getEnd();

        // Calculate the direction of the two line segments (vectors)
        double x1 = p2.getX() - p1.getX();
        double y1 = p2.getY() - p1.getY();
        double x2 = q2.getX() - q1.getX();
        double y2 = q2.getY() - q1.getY();

        // Calculate the determinant (the denominator of the intersection formula)
        double denominator = x1 * y2 - y1 * x2;

        if (Math.abs(denominator) < 1e-9) {
            return null; // Lines are parallel or collinear, no intersection
        }

        // Calculate the intersection point using the determinant formula
        double t = ((q1.getX() - p1.getX()) * y2 - (q1.getY() - p1.getY()) * x2) / denominator;
        double u = ((q1.getX() - p1.getX()) * y1 - (q1.getY() - p1.getY()) * x1) / denominator;

        // Check if the intersection point lies within the bounds of both segments
        if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {
            double intersectX = p1.getX() + t * x1;
            double intersectY = p1.getY() + t * y1;
            return new Point(intersectX, intersectY);
        }

        return null; // No intersection within the segments
    }
}
