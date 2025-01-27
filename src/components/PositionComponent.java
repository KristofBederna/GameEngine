package components;

import core.Component;

import java.awt.*;

public class PositionComponent extends Component {
    private int X;
    private int Y;
    public PositionComponent(int x, int y) {
        this.X = x;
        this.Y = y;
    }

    public int getX() {
        return X;
    }
    public int getY() {
        return Y;
    }
    public void setX(int x) {
        X = x;
    }
    public void setY(int y) {
        Y = y;
    }
    public void alignHitBox(Rectangle hitBox) {
        hitBox.x = this.X;
        hitBox.y = this.Y;
    }
}
