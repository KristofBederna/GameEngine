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

    public double getLength() {
        return beginning.distanceTo(end);
    }
}
