package inf.elte.hu.gameengine_javafx.Components.PropertyComponents;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Maths.Vector;

public class VelocityComponent extends Component {
    Vector velocity;
    public VelocityComponent() {
        velocity = new Vector(0, 0);
    }

    public Vector getVelocity() {
        return velocity;
    }
    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }
    public void setVelocity(double x, double y) {
        this.velocity = new Vector(x, y);
    }

    public void stopMovement() {
        velocity = new Vector(0, 0);
    }

    @Override
    public String getStatus() {
        return "";
    }

}
