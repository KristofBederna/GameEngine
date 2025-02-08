package inf.elte.hu.gameengine_javafx.Components;

import inf.elte.hu.gameengine_javafx.Core.Component;

public class CameraComponent extends Component {
    private double x, y;
    private double width, height;
    private double worldWidth, worldHeight;

    public CameraComponent(double width, double height, double worldWidth, double worldHeight) {
        this.width = width;
        this.height = height;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.x = 0;
        this.y = 0;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public double getWorldWidth() { return worldWidth; }
    public double getWorldHeight() { return worldHeight; }

    public void setPosition(double x, double y) {
        this.x = Math.max(0, Math.min(x, worldWidth - width));
        this.y = Math.max(0, Math.min(y, worldHeight - height));
    }

    @Override
    public String getStatus() {
        return (this.getClass().getSimpleName() + ": x: " + x + ", y: " + y + ", Width: " + width + ", Height: " + height);
    }
}
