package inf.elte.hu.gameengine_javafx.Components;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;

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

    @Override
    public String getStatus() {
        return (this.getClass().getSimpleName() + ": Dx: " + Dx + ", Dy: " + Dy);
    }
}
