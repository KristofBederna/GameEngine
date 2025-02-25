package inf.elte.hu.gameengine_javafx.Maths.Geometry;

import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class Rectangle extends Shape {
    public Rectangle(Point topLeft, Point topRight, Point bottomLeft, Point bottomRight) {
        this.points = List.of(topLeft, topRight, bottomRight, bottomLeft);
        updateEdges();
    }

    public Rectangle(Point topLeft, double width, double height) {
        this.points = List.of(topLeft, new Point(topLeft.getX()+width, topLeft.getY()), new Point(topLeft.getX()+width, topLeft.getY()+height), new Point(topLeft.getX(), topLeft.getY()+height));
        updateEdges();
    }

    public Rectangle(Rectangle rectangle) {
        this(rectangle.getTopLeft(), rectangle.getTopRight(), rectangle.getBottomLeft(), rectangle.getBottomRight());
    }

    private void updateEdges() {
        edges = List.of(
                new Edge(points.get(0), points.get(1)), // Top edge (topLeft -> topRight)
                new Edge(points.get(1), points.get(2)), // Right edge (topRight -> bottomRight)
                new Edge(points.get(2), points.get(3)), // Bottom edge (bottomRight -> bottomLeft)
                new Edge(points.get(3), points.get(0))  // Left edge (bottomLeft -> topLeft)
        );
    }


    public Point getTopLeft() {
        return points.get(0);
    }

    public void setTopLeft(Point topLeft) {
        points.set(0, topLeft);
        updateEdges();
    }

    public Point getTopRight() {
        return points.get(1);
    }

    public void setTopRight(Point topRight) {
        points.set(1, topRight);
        updateEdges();
    }

    public Point getBottomLeft() {
        return points.get(2);
    }

    public void setBottomLeft(Point bottomLeft) {
        points.set(2, bottomLeft);
        updateEdges();
    }

    public Point getBottomRight() {
        return points.get(3);
    }

    public void setBottomRight(Point bottomRight) {
        points.set(3, bottomRight);
        updateEdges();
    }

    public Edge getTop() {
        return edges.get(0);
    }

    public Edge getRight() {
        return edges.get(1);
    }

    public Edge getBottom() {
        return edges.get(2);
    }

    public Edge getLeft() {
        return edges.get(3);
    }

    public void translate(double x, double y) {
        for (Point p : points) {
            p.translate(x, y);
        }
        updateEdges();
    }

    public void moveTo(Point newPoint) {
        double deltaX = newPoint.getX() - points.get(0).getX();
        double deltaY = newPoint.getY() - points.get(0).getY();

        for (Point p : points) {
            p.setX(p.getX() + deltaX);
            p.setY(p.getY() + deltaY);
        }
        updateEdges();
    }

    public double getArea() {
        double width = points.get(0).distanceTo(points.get(1));
        double height = points.get(0).distanceTo(points.get(2));
        return width * height;
    }

    public boolean contains(Point p) {
        return p.getX() >= points.get(0).getX() && p.getX() <= points.get(1).getX() &&
                p.getY() >= points.get(0).getY() && p.getY() <= points.get(2).getY();
    }

    public void render(GraphicsContext gc) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);

        CameraEntity cameraEntity = CameraEntity.getInstance();
        double cameraX = cameraEntity.getComponent(PositionComponent.class).getGlobalX();
        double cameraY = cameraEntity.getComponent(PositionComponent.class).getGlobalY();

        double renderTopLeftX = points.get(0).getX() - cameraX;
        double renderTopLeftY = points.get(0).getY() - cameraY;
        double renderTopRightX = points.get(1).getX() - cameraX;
        double renderTopRightY = points.get(1).getY() - cameraY;
        double renderBottomRightX = points.get(2).getX() - cameraX; // Corrected index
        double renderBottomRightY = points.get(2).getY() - cameraY; // Corrected index
        double renderBottomLeftX = points.get(3).getX() - cameraX;  // Corrected index
        double renderBottomLeftY = points.get(3).getY() - cameraY;  // Corrected index

        gc.strokeLine(renderTopLeftX, renderTopLeftY, renderTopRightX, renderTopRightY);
        gc.strokeLine(renderTopRightX, renderTopRightY, renderBottomRightX, renderBottomRightY);
        gc.strokeLine(renderBottomRightX, renderBottomRightY, renderBottomLeftX, renderBottomLeftY);
        gc.strokeLine(renderBottomLeftX, renderBottomLeftY, renderTopLeftX, renderTopLeftY);
    }
}
