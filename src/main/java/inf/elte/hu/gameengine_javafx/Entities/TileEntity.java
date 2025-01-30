package inf.elte.hu.gameengine_javafx.Entities;

import inf.elte.hu.gameengine_javafx.Components.ImageComponent;
import inf.elte.hu.gameengine_javafx.Components.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.RectangularHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Core.Entity;

public class TileEntity extends Entity {
    private int value;
    public TileEntity(int value, int x, int y, String path, int width, int height) {
        this.value = value;
        this.addComponent(new ImageComponent(path, width, height));
        this.addComponent(new PositionComponent(x, y));
    }
    public TileEntity(int value, int x, int y, String path, int width, int height, boolean hasHitBox) {
        this.value = value;
        this.addComponent(new ImageComponent(path, width, height));
        this.addComponent(new PositionComponent(x, y));
        if (hasHitBox) {
            this.addComponent(new RectangularHitBoxComponent(x, y, width, height));
        }
    }
    public TileEntity(int value, int x, int y, String path) {
        this.value = value;
        this.addComponent(new ImageComponent(path));
        this.addComponent(new PositionComponent(x, y));
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public void addHitBox(int x, int y, int width, int height) {
        this.addComponent(new RectangularHitBoxComponent(x, y, width, height));
    }
}
