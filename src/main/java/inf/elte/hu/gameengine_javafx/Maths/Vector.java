package inf.elte.hu.gameengine_javafx.Maths;

import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.CentralMassComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;

public class Vector {
    private double dx;
    private double dy;

    public Vector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
    public double getDx() {
        return dx;
    }
    public double getDy() {
        return dy;
    }
    public Vector getVector() {
        return new Vector(dx, dy);
    }
    public void setDx(double dx) {
        this.dx = dx;
    }
    public void setDy(double dy) {
        this.dy = dy;
    }
    public void setVector(Vector vector) {
        this.dx = vector.getDx();
        this.dy = vector.getDy();
    }

    public void moveTowards(Point node, Entity entity) {
        double currentX = entity.getComponent(CentralMassComponent.class).getCentralX();
        double currentY = entity.getComponent(CentralMassComponent.class).getCentralY();

        double deltaX = node.getX() - currentX;
        double deltaY = node.getY() - currentY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance > 0) {
            dx = deltaX / distance;
            dy = deltaY / distance;
        }
    }
}
