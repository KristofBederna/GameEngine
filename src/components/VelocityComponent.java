package components;

import core.Component;

public class VelocityComponent extends Component {
    private int Dx;
    private int Dy;
    public VelocityComponent(int dx, int dy) {
        Dx = dx;
        Dy = dy;
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
}
