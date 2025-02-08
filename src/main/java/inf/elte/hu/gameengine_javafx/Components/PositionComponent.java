package inf.elte.hu.gameengine_javafx.Components;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;

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

    @Override
    public String getStatus() {
        return (this.getClass().getSimpleName() + ": X: " + X + ", Y: " + Y);
    }
}
