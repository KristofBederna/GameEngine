package inf.elte.hu.gameengine_javafx.Maths.Geometry;

import inf.elte.hu.gameengine_javafx.Components.CameraComponent;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

public class Triangle extends Shape {

    public Triangle(Point a, Point b, Point c) {
        super(List.of(a, b, c));
        updateEdges();
    }

    public Triangle(Triangle hitBox) {
        super(hitBox.getPoints());
        updateEdges();
    }

    private void updateEdges() {
        edges = List.of(
                new Edge(points.get(0), points.get(1)), // ab
                new Edge(points.get(1), points.get(2)), // bc
                new Edge(points.get(2), points.get(0))  // ca
        );
    }

    public Point getA() {
        return points.get(0);
    }

    public void setA(Point a) {
        points.set(0, a);
        updateEdges();
    }

    public Point getB() {
        return points.get(1);
    }

    public void setB(Point b) {
        points.set(1, b);
        updateEdges();
    }

    public Point getC() {
        return points.get(2);
    }

    public void setC(Point c) {
        points.set(2, c);
        updateEdges();
    }

    public Edge getAb() {
        return edges.get(0);
    }

    public Edge getBc() {
        return edges.get(1);
    }

    public Edge getCa() {
        return edges.get(2);
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
        return Math.abs((points.get(0).getX() * (points.get(1).getY() - points.get(2).getY()) +
                points.get(1).getX() * (points.get(2).getY() - points.get(0).getY()) +
                points.get(2).getX() * (points.get(0).getY() - points.get(1).getY())) / 2);
    }

    public boolean contains(Point p) {
        double A = getArea();
        double A1 = new Triangle(p, getB(), getC()).getArea();
        double A2 = new Triangle(getA(), p, getC()).getArea();
        double A3 = new Triangle(getA(), getB(), p).getArea();

        return Math.abs(A - (A1 + A2 + A3)) < 1e-9;
    }

    public boolean isInside(Point p) {
        return contains(p) && !getAb().contains(p) && !getBc().contains(p) && !getCa().contains(p);
    }

    public void render(GraphicsContext gc) {
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);

        CameraComponent camera = Globals.playerEntity.getComponent(CameraComponent.class);

        double renderAX = points.get(0).getX() - camera.getX();
        double renderAY = points.get(0).getY() - camera.getY();
        double renderBX = points.get(1).getX() - camera.getX();
        double renderBY = points.get(1).getY() - camera.getY();
        double renderCX = points.get(2).getX() - camera.getX();
        double renderCY = points.get(2).getY() - camera.getY();

        gc.strokeLine(renderAX, renderAY, renderBX, renderBY);
        gc.strokeLine(renderBX, renderBY, renderCX, renderCY);
        gc.strokeLine(renderCX, renderCY, renderAX, renderAY);
    }
}
