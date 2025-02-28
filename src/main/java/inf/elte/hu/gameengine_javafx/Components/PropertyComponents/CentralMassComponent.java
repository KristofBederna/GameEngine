package inf.elte.hu.gameengine_javafx.Components.PropertyComponents;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;

public class CentralMassComponent extends Component {
    private double centralX;
    private double centralY;

    public CentralMassComponent(double centralX, double centralY) {
        this.centralX = centralX;
        this.centralY = centralY;
    }
    public double getCentralX() {
        return centralX;
    }
    public double getCentralY() {
        return centralY;
    }

    @Override
    public String getStatus() {
        return "";
    }

    public void setCentralX(double centralX) {
        this.centralX = centralX;
    }

    public void setCentralY(double centralY) {
        this.centralY = centralY;
    }
}
