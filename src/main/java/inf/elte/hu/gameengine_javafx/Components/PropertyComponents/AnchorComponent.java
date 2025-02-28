package inf.elte.hu.gameengine_javafx.Components.PropertyComponents;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;

public class AnchorComponent extends Component {
    private double anchorX;
    private double anchorY;

    public AnchorComponent(double anchorX, double anchorY) {
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }
    public double getAnchorX() {
        return anchorX;
    }
    public void setAnchorX(double anchorX) {
        this.anchorX = anchorX;
    }
    public double getAnchorY() {
        return anchorY;
    }
    public void setAnchorY(double anchorY) {
        this.anchorY = anchorY;
    }

    @Override
    public String getStatus() {
        return "";
    }
}
