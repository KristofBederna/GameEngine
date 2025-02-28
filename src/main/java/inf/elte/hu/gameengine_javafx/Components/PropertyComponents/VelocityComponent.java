package inf.elte.hu.gameengine_javafx.Components.PropertyComponents;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;

public class VelocityComponent extends Component {
    private double Dx;
    private double Dy;
    public VelocityComponent() {
        Dx = 0;
        Dy = 0;
    }

    public double getDx() {
        return Dx;
    }
    public double getDy() {
        return Dy;
    }

    public void setDx(double dx) {
        Dx = dx;
    }
    public void setDy(double dy) {
        Dy = dy;
    }

    public void moveTowards(Point node, Entity entity) {
        double currentX = entity.getComponent(CentralMassComponent.class).getCentralX();
        double currentY = entity.getComponent(CentralMassComponent.class).getCentralY();

        double deltaX = node.getX() - currentX;
        double deltaY = node.getY() - currentY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        if (distance > 0) {
            Dx = deltaX / distance;
            Dy = deltaY / distance;
        }
    }

    public void stopMovement() {
        Dx = 0;
        Dy = 0;
    }

    @Override
    public String getStatus() {
        return (this.getClass().getSimpleName() + ": Dx: " + Dx + ", Dy: " + Dy);
    }

}
