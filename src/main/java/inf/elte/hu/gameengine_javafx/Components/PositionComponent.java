package inf.elte.hu.gameengine_javafx.Components;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;

import java.awt.*;

public class PositionComponent extends Component {
    private double X;
    private double Y;
    public PositionComponent(double x, double y) {
        this.X = x;
        this.Y = y;
    }

    public double getX() {
        return X;
    }
    public double getY() {
        return Y;
    }
    public void setX(double x) {
        X = x;
    }
    public void setY(double y) {
        Y = y;
    }
    public void alignHitBox(Rectangle hitBox) {
        hitBox.x = (int) this.X;
        hitBox.y = (int) this.Y;
    }

    @Override
    public String getStatus() {
        return (this.getClass().getSimpleName() + ": X: " + X + ", Y: " + Y);
    }
}
