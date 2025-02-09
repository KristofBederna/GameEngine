package inf.elte.hu.gameengine_javafx.Components;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;

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

    public void setWidth(double width) {
        this.width = width;
    }
    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public String getStatus() {
        return (this.getClass().getSimpleName() + ": x: " + x + ", y: " + y + ", Width: " + width + ", Height: " + height);
    }

    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }

    public boolean isPositionInsideViewport(double entityX, double entityY, double entityWidth, double entityHeight) {
        double renderX = entityX - x;
        double renderY = entityY - y;

        return renderX + entityWidth >= 0 && renderX <= width &&
                renderY + entityHeight >= 0 && renderY <= height;
    }
}
