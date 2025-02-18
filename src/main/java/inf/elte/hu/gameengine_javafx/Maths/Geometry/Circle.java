package inf.elte.hu.gameengine_javafx.Maths.Geometry;


import inf.elte.hu.gameengine_javafx.Components.CameraComponent;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Circle extends Shape {
    private Point center;
    private double radius;
    private int segments;

    public Circle(Point center, double radius, int segments) {
        this.center = center;
        this.radius = radius;
        this.segments = segments;
        this.points = new ArrayList<>(List.of(center));
        generateApproximation();
    }

    public Circle(Circle hitBox) {
        this.center = hitBox.center;
        this.radius = hitBox.radius;
        this.points = new ArrayList<>(hitBox.points);
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
        }

        Point prev = points.get(points.size() - 1);
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

