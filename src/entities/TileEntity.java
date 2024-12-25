package entities;

import components.ImageComponent;
import components.PositionComponent;
import core.Entity;

public class TileEntity extends Entity {
    private int value;
    public TileEntity(int id, int value, int x, int y, String path, int width, int height) {
        super(id);
        this.value = value;
        this.addComponent(new ImageComponent(path, width, height));
        this.addComponent(new PositionComponent(x, y));
    }
    public TileEntity(int id, int value, int x, int y, String path) {
        super(id);
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
}
