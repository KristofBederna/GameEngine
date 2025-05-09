package inf.elte.hu.gameengine_javafx.Maths.Geometry;

import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import inf.elte.hu.gameengine_javafx.Misc.Configs.DisplayConfig;
import inf.elte.hu.gameengine_javafx.Misc.Configs.ResourceConfig;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class Rectangle extends Shape {
    public Rectangle(Point topLeft, Point topRight, Point bottomLeft, Point bottomRight) {
        this.points = List.of(topLeft, topRight, bottomRight, bottomLeft);
        updateEdges();
    }

    public Rectangle(Point topLeft, double width, double height) {
        this.points = List.of(topLeft, new Point(topLeft.getX() + width, topLeft.getY()), new Point(topLeft.getX() + width, topLeft.getY() + height), new Point(topLeft.getX(), topLeft.getY() + height));
        updateEdges();
    }

    public Rectangle(Rectangle rectangle) {
        this(rectangle.getTopLeft(), rectangle.getTopRight(), rectangle.getBottomLeft(), rectangle.getBottomRight());
    }

    public void updateEdges() {
        edges = List.of(
                new Edge(points.get(0), points.get(1)), // Top edge (topLeft -> topRight)
                new Edge(points.get(1), points.get(2)), // Right edge (topRight -> bottomRight)
                new Edge(points.get(2), points.get(3)), // Bottom edge (bottomRight -> bottomLeft)
                new Edge(points.get(3), points.get(0))  // Left edge (bottomLeft -> topLeft)
        );
    }


    public Point getTopLeft() {
        return points.getFirst();
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

    public void translate(double x, double y) {
        for (Point p : points) {
            p.translate(x, y);
        }
        updateEdges();
    }

    public void moveTo(Point newPoint) {
        double deltaX = newPoint.getX() - points.getFirst().getX();
        double deltaY = newPoint.getY() - points.getFirst().getY();

        for (Point p : points) {
            p.setX(p.getX() + deltaX);
            p.setY(p.getY() + deltaY);
        }
        updateEdges();
    }

    public void render(GraphicsContext gc, Color color) {
        gc.setStroke(color);
        gc.setLineWidth(2);

        double x = CameraEntity.getRenderX(points.get(0).getX());
        double y = CameraEntity.getRenderY(points.get(0).getY());
        double width = points.get(1).getX() - points.get(0).getX();
        double height = points.get(3).getY() - points.get(0).getY();

        gc.strokeRect(x* DisplayConfig.relativeWidthRatio, y* DisplayConfig.relativeHeightRatio, width* DisplayConfig.relativeWidthRatio, height* DisplayConfig.relativeHeightRatio);
    }

    public void render(GraphicsContext gc, Color color, double strokeWidth) {
        gc.setStroke(color);
        gc.setLineWidth(strokeWidth);

        double x = CameraEntity.getRenderX(points.get(0).getX());
        double y = CameraEntity.getRenderY(points.get(0).getY());
        double width = points.get(1).getX() - points.get(0).getX();
        double height = points.get(3).getY() - points.get(0).getY();

        gc.strokeRect(x* DisplayConfig.relativeWidthRatio, y* DisplayConfig.relativeHeightRatio, width* DisplayConfig.relativeWidthRatio, height* DisplayConfig.relativeHeightRatio);
    }

    public void renderFill(GraphicsContext gc, Color color) {
        double x = CameraEntity.getRenderX(points.get(0).getX());
        double y = CameraEntity.getRenderY(points.get(0).getY());
        double width = points.get(1).getX() - points.get(0).getX();
        double height = points.get(3).getY() - points.get(0).getY();

        gc.setFill(color);
        gc.fillRect(x* DisplayConfig.relativeWidthRatio, y* DisplayConfig.relativeHeightRatio, width* DisplayConfig.relativeWidthRatio, height* DisplayConfig.relativeHeightRatio);
    }

    public void renderFillWithStroke(GraphicsContext gc, double radius, Color color, Color strokeColor, double outerStrokeWidth) {
        double x = CameraEntity.getRenderX(points.get(0).getX());
        double y = CameraEntity.getRenderY(points.get(0).getY());
        double width = points.get(1).getX() - points.get(0).getX();
        double height = points.get(3).getY() - points.get(0).getY();

        gc.setFill(color);
        gc.fillRect(x* DisplayConfig.relativeWidthRatio, y* DisplayConfig.relativeHeightRatio, width* DisplayConfig.relativeWidthRatio, height* DisplayConfig.relativeHeightRatio);

        gc.setStroke(strokeColor);
        gc.setLineWidth(outerStrokeWidth);
        gc.strokeRect(x* DisplayConfig.relativeWidthRatio, y* DisplayConfig.relativeHeightRatio, width* DisplayConfig.relativeWidthRatio, height* DisplayConfig.relativeHeightRatio);
    }
}
