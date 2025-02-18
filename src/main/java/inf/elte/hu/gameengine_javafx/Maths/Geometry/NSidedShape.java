package inf.elte.hu.gameengine_javafx.Maths.Geometry;


import inf.elte.hu.gameengine_javafx.Components.CameraComponent;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class NSidedShape extends Shape {
    private Point center;
    private double radius;
    private int segments;
    private double rotation = 0;

    public NSidedShape(Point center, double radius, int segments) {
        this.center = center;
        this.radius = radius;
        this.segments = segments;
        this.points = new ArrayList<>(List.of(center));
        generateApproximation();
    }

    public NSidedShape(NSidedShape hitBox) {
        this.center = hitBox.center;
        this.radius = hitBox.radius;
        this.segments = hitBox.segments;
        this.rotation = hitBox.rotation;
        this.points = new ArrayList<>(hitBox.points);
    }

    public NSidedShape(Point center, double sideLength, int segments, int rotation) {
        this.center = center;
        this.radius = sideLength / 2;
        this.segments = segments;
        this.points = new ArrayList<>();
        generateApproximation();
        this.rotation = rotation;
        rotate(rotation);
    }



    private void generateApproximation() {
        points.clear();

        for (int i = 0; i < this.segments; i++) {
            double angle = 2 * Math.PI * i / this.segments;
            double x = center.getX() + radius * Math.cos(angle);
            double y = center.getY() + radius * Math.sin(angle);
            points.add(new Point(x, y));
        }
        updateEdges();
        rotate(rotation);
    }

    public void rotate(double degrees) {
        double angle = Math.toRadians(degrees);

        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);

            double x = p.getX() - center.getX();
            double y = p.getY() - center.getY();

            double rotatedX = x * Math.cos(angle) - y * Math.sin(angle);
            double rotatedY = x * Math.sin(angle) + y * Math.cos(angle);

            points.set(i, new Point(rotatedX + center.getX(), rotatedY + center.getY()));
        }
        updateEdges();
    }


    public void updateEdges() {
        this.edges = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            Point p1 = points.get(i);
            Point p2 = points.get((i + 1) % points.size());
            edges.add(new Edge(p1, p2));
        }
    }


    public void render(GraphicsContext gc) {
        CameraComponent camera = Globals.playerEntity.getComponent(CameraComponent.class);

        gc.setStroke(Color.GREEN);
        gc.setLineWidth(2);

        if (points.isEmpty()) {
            generateApproximation();
            updateEdges();
        }

        Point prev = points.getLast();
        for (Point p : points) {
            double x1 = prev.getX() - camera.getX();
            double y1 = prev.getY() - camera.getY();
            double x2 = p.getX() - camera.getX();
            double y2 = p.getY() - camera.getY();

            gc.strokeLine(x1, y1, x2, y2);
            prev = p;
        }
    }

    public void moveTo(Point newPoint) {
        center = newPoint;
        generateApproximation();
        updateEdges();
    }

    public void translate(double x, double y) {
        center.translate(x, y);
        generateApproximation();
        updateEdges();
    }

}

