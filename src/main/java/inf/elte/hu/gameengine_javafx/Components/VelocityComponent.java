package inf.elte.hu.gameengine_javafx.Components;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;

public class VelocityComponent extends Component {
    private int Dx;
    private int Dy;
    public VelocityComponent() {
        Dx = 0;
        Dy = 0;
    }

    public int getDx() {
        return Dx;
    }
    public int getDy() {
        return Dy;
    }

    public void setDx(int dx) {
        Dx = dx;
    }
    public void setDy(int dy) {
        Dy = dy;
    }

    @Override
    public String getStatus() {
        return (this.getClass().getSimpleName() + ": Dx: " + Dx + ", Dy: " + Dy);
    }
}
