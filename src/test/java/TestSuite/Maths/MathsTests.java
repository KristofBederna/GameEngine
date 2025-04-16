package TestSuite.Maths;

import inf.elte.hu.gameengine_javafx.Maths.Geometry.*;
import inf.elte.hu.gameengine_javafx.Misc.Configs.PhysicsConfig;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MathsTests {
    @Test
    void testPointTranslation() {
        Point p = new Point(1, 1);
        p.translate(3, 4);
        assertEquals(4, p.getX());
        assertEquals(5, p.getY());
    }

    @Test
    void testPointDistanceTo() {
        Point p1 = new Point(0, 0);
        Point p2 = new Point(3, 4);
        assertEquals(5.0, p1.distanceTo(p2), 0.0001);
    }

    @Test
    void testEdgeLength() {
        Point p1 = new Point(0, 0);
        Point p2 = new Point(0, 5);
        Edge edge = new Edge(p1, p2);
        assertEquals(5.0, edge.getLength(), 0.0001);
    }

    @Test
    void testRectangleTranslation() {
        Rectangle rect = new Rectangle(new Point(0, 0), 2, 3);
        rect.translate(1, 1);
        assertEquals(1, rect.getTopLeft().getX());
        assertEquals(1, rect.getTopLeft().getY());
    }

    @Test
    void testTriangleMoveTo() {
        Triangle tri = new Triangle(new Point(1, 1), new Point(2, 1), new Point(1.5, 2));
        tri.moveTo(new Point(4, 4));
        assertEquals(4, tri.getA().getX());
        assertEquals(4, tri.getA().getY());
    }

    @Test
    void testLineUpdateEdges() {
        Line line = new Line(new Point(0, 0), new Point(2, 2));
        List<Edge> edges = line.getEdges();
        assertEquals(1, edges.size());
        assertEquals(2.8284, edges.get(0).getLength(), 0.0001);
    }

    @Test
    void testNSidedShapeRotation() {
        NSidedShape shape = new NSidedShape(new Point(0, 0), 10, 4);
        List<Point> before = List.copyOf(shape.getPoints());
        shape.rotate(90);
        List<Point> after = shape.getPoints();
        assertEquals(before.size(), after.size());
        assertNotEquals(before.get(0).getX(), after.get(0).getX(), 0.001);
    }

    @Test
    void testComplexShapeTranslation() {
        ComplexShape shape = new ComplexShape(List.of(
                new Point(0, 0),
                new Point(1, 0),
                new Point(1, 1),
                new Point(0, 1)
        ));
        shape.translate(2, 2);
        assertEquals(2, shape.getPoints().get(0).getX());
        assertEquals(2, shape.getPoints().get(0).getY());
    }

    @Test
    void testComplexShapePointInside() {
        ComplexShape shape = new ComplexShape(List.of(
                new Point(0, 0),
                new Point(4, 0),
                new Point(4, 4),
                new Point(0, 4)
        ));
        assertTrue(shape.isPointInside(new Point(2, 2)));
        assertFalse(shape.isPointInside(new Point(5, 5)));
    }

    @Test
    void testRectangleCopyConstructor() {
        Rectangle r1 = new Rectangle(new Point(0, 0), 2, 2);
        Rectangle r2 = new Rectangle(r1);
        assertEquals(r1.getTopLeft().getX(), r2.getTopLeft().getX());
        assertEquals(r1.getBottomRight().getY(), r2.getBottomRight().getY());
    }

    @Test
    void testTriangleSetters() {
        Triangle tri = new Triangle(new Point(0, 0), new Point(1, 0), new Point(0, 1));
        tri.setA(new Point(2, 2));
        assertEquals(2, tri.getA().getX());
        assertEquals(2, tri.getA().getY());
    }

    @Test
    void testNSidedShapeMoveTo() {
        NSidedShape shape = new NSidedShape(new Point(0, 0), 10, 5);
        shape.moveTo(new Point(10, 10));
        assertEquals(10, shape.getCenter().getX());
        assertEquals(10, shape.getCenter().getY());
    }

    @Test
    void testPointCompareCoordinates() {
        Point p1 = new Point(0, 0);
        Point p2 = new Point(5, 5);
        assertTrue(p1.compareCoordinates(p2, 10));
        assertFalse(p1.compareCoordinates(p2, 2));
    }

    @Test
    void testDistanceTo_WhenPointsAreSame_ShouldReturnZero() {
        Point p1 = new Point(5, 5);
        Point p2 = new Point(5, 5);

        assertEquals(0.0, p1.distanceTo(p2), 1e-6);
    }

    @Test
    void testDistanceTo_WhenPointsAreDiagonal_ShouldReturnCorrectValue() {
        Point p1 = new Point(0, 0);
        Point p2 = new Point(3, 4); // 3-4-5 háromszög

        assertEquals(5.0, p1.distanceTo(p2), 1e-6);
    }

    @Test
    void testDistanceTo_WithNegativeCoordinates_ShouldReturnCorrectValue() {
        Point p1 = new Point(-2, -3);
        Point p2 = new Point(1, 1);

        double expected = Math.sqrt(9 + 16); // sqrt(25) = 5
        assertEquals(5.0, p1.distanceTo(p2), 1e-6);
    }

    @Test
    void testCompareCoordinates_WithoutDelta_TrueWithin10() {
        Point p1 = new Point(50, 50);
        Point p2 = new Point(58, 55); // dx = 8, dy = 5

        assertTrue(p1.compareCoordinates(p2));
    }

    @Test
    void testCompareCoordinates_WithoutDelta_FalseOutside10() {
        Point p1 = new Point(50, 50);
        Point p2 = new Point(61, 50); // dx = 11

        assertFalse(p1.compareCoordinates(p2));
    }

    @Test
    void testCompareCoordinates_WithDelta_TrueWithinCustomDelta() {
        Point p1 = new Point(0, 0);
        Point p2 = new Point(3, 4); // dx = 3, dy = 4

        assertTrue(p1.compareCoordinates(p2, 5));
    }

    @Test
    void testCompareCoordinates_WithDelta_FalseOutsideCustomDelta() {
        Point p1 = new Point(0, 0);
        Point p2 = new Point(6, 6); // dx = 6, dy = 6

        assertFalse(p1.compareCoordinates(p2, 5.9));
    }

    @Test
    void testCompareCoordinates_WhenOtherIsNull_ShouldReturnFalse() {
        Point p = new Point(0, 0);

        assertFalse(p.compareCoordinates(null));
        assertFalse(p.compareCoordinates(null, 5.0));
    }

    @Test
    void testEdgesIntersect_WhenEdgesCross_ShouldReturnTrue() {
        Edge e1 = new Edge(new Point(0, 0), new Point(2, 2));
        Edge e2 = new Edge(new Point(0, 2), new Point(2, 0));

        assertTrue(Shape.edgesIntersect(e1, e2));
    }

    @Test
    void testEdgesIntersect_WhenEdgesDoNotCross_ShouldReturnFalse() {
        Edge e1 = new Edge(new Point(0, 0), new Point(1, 0));
        Edge e2 = new Edge(new Point(0, 1), new Point(1, 1));

        assertFalse(Shape.edgesIntersect(e1, e2));
    }

    @Test
    void testEdgesIntersect_WhenCollinearAndOverlapping_ShouldReturnTrue() {
        Edge e1 = new Edge(new Point(0, 0), new Point(2, 0));
        Edge e2 = new Edge(new Point(1, 0), new Point(3, 0));

        assertTrue(Shape.edgesIntersect(e1, e2));
    }

    @Test
    void testEdgesIntersect_WhenCollinearButSeparate_ShouldReturnFalse() {
        Edge e1 = new Edge(new Point(0, 0), new Point(1, 0));
        Edge e2 = new Edge(new Point(2, 0), new Point(3, 0));

        assertFalse(Shape.edgesIntersect(e1, e2));
    }

    @Test
    void testIntersect_WhenShapesShareIntersectingEdges_ShouldReturnTrue() {
        Shape s1 = createShape(new Point(0, 0), new Point(2, 2));
        Shape s2 = createShape(new Point(0, 2), new Point(2, 0));

        assertTrue(Shape.intersect(s1, s2));
    }

    @Test
    void testIntersect_WhenShapesAreDisjoint_ShouldReturnFalse() {
        Shape s1 = createShape(new Point(0, 0), new Point(1, 1));
        Shape s2 = createShape(new Point(2, 2), new Point(3, 3));

        assertFalse(Shape.intersect(s1, s2));
    }

    @Test
    void testGetIntersection_WhenEdgesIntersectInsideSegments_ShouldReturnIntersectionPoint() {
        Edge e1 = new Edge(new Point(0, 0), new Point(2, 2));
        Edge e2 = new Edge(new Point(0, 2), new Point(2, 0));

        Point intersection = Shape.getIntersection(e1, e2);
        assertNotNull(intersection);
        assertEquals(1.0, intersection.getX(), PhysicsConfig.EPSILON);
        assertEquals(1.0, intersection.getY(), PhysicsConfig.EPSILON);
    }

    @Test
    void testGetIntersection_WhenEdgesDoNotIntersect_ShouldReturnNull() {
        Edge e1 = new Edge(new Point(0, 0), new Point(1, 0));
        Edge e2 = new Edge(new Point(0, 1), new Point(1, 1));

        Point intersection = Shape.getIntersection(e1, e2);
        assertNull(intersection);
    }

    @Test
    void testGetIntersection_WhenEdgesAreParallel_ShouldReturnNull() {
        Edge e1 = new Edge(new Point(0, 0), new Point(2, 0));
        Edge e2 = new Edge(new Point(0, 1), new Point(2, 1));

        Point intersection = Shape.getIntersection(e1, e2);
        assertNull(intersection);
    }

    @Test
    void testGetIntersection_WhenCollinearAndOverlapping_ShouldReturnNull() {
        Edge e1 = new Edge(new Point(0, 0), new Point(2, 0));
        Edge e2 = new Edge(new Point(1, 0), new Point(3, 0));

        Point intersection = Shape.getIntersection(e1, e2);
        assertNull(intersection);
    }

    private Shape createShape(Point p1, Point p2) {
        return new Shape() {{
            points = List.of(p1, p2);
            edges = List.of(new Edge(p1, p2));
        }};
    }
}
