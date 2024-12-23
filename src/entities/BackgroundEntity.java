package entities;

import components.ColorComponent;
import core.Entity;

import java.awt.*;

public class BackgroundEntity extends Entity {
    public BackgroundEntity() {
        super(0);
        ColorComponent colorComponent = new ColorComponent(Color.white);
        this.addComponent(colorComponent);
    }
}
