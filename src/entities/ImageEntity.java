package entities;

import components.ImageComponent;
import components.MoveableComponent;
import components.PositionComponent;
import components.VelocityComponent;
import core.Entity;

public class ImageEntity extends Entity {
    public ImageEntity(int x, int y, String path) {
        super(0);
        this.addComponent(new ImageComponent(path, 25, 25));
        this.addComponent(new PositionComponent(x, y));
        this.addComponent(new VelocityComponent(0, 0));
        this.addComponent(new MoveableComponent());
    }
}
