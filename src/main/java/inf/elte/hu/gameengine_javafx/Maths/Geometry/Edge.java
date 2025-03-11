package inf.elte.hu.gameengine_javafx.Maths.Geometry;

import inf.elte.hu.gameengine_javafx.Misc.Config;

public class Edge {
    private Point beginning, end;

    public Edge(Point beginning, Point end) {
        this.beginning = beginning;
        this.end = end;
    }

    public Point getBeginning() {
        return beginning;
    }

    public void setBeginning(Point beginning) {
        this.beginning = beginning;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public boolean contains(Point p) {
        double crossProduct = (p.getY() - beginning.getY()) * (end.getX() - beginning.getX())
                - (p.getX() - beginning.getX()) * (end.getY() - beginning.getY());

        if (Math.abs(crossProduct) > Config.EPSILON) {
            return false;
        }

        double minX = Math.min(beginning.getX(), end.getX());
        double maxX = Math.max(beginning.getX(), end.getX());
        double minY = Math.min(beginning.getY(), end.getY());
        double maxY = Math.max(beginning.getY(), end.getY());

        return p.getX() >= minX && p.getX() <= maxX && p.getY() >= minY && p.getY() <= maxY;
    }

    public double getLength() {
        return beginning.distanceTo(end);
    }
}
