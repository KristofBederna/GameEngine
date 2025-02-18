package inf.elte.hu.gameengine_javafx.Misc;

import inf.elte.hu.gameengine_javafx.Components.PositionComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;

public class Camera {
    private static Camera instance;
    private double x, y;
    private double width, height;
    private double worldWidth, worldHeight;
    private Entity owner;

    private Camera(double width, double height, double worldWidth, double worldHeight) {
        this.width = width;
        this.height = height;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.x = 0;
        this.y = 0;
    }

    public static Camera getInstance(double width, double height, double worldWidth, double worldHeight) {
        if (instance == null) {
            instance = new Camera(width, height, worldWidth, worldHeight);
        }
        return instance;
    }

    public static Camera getInstance() {
        return instance;
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

    public void attachTo(Entity entity) {
        this.owner = entity;
    }

    public Entity getOwner() {
        return owner;
    }
}