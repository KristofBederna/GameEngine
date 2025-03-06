package inf.elte.hu.gameengine_javafx.Components.PropertyComponents;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;

public class AnchorComponent extends Component {
    private Point anchor;

    public AnchorComponent(double anchorX, double anchorY) {
        anchor = new Point(anchorX, anchorY);
    }
    public double getAnchorX() {
        return anchor.getX();
    }
    public void setAnchorX(double anchorX) {
        this.anchor.setX(anchorX);
    }
    public double getAnchorY() {
        return this.anchor.getY();
    }
    public void setAnchorY(double anchorY) {
        this.anchor.setY(anchorY);
    }

    @Override
    public String getStatus() {
        return "";
    }
}
