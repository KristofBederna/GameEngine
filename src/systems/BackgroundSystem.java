package systems;

import core.System;
import core.Entity;
import components.ColorComponent;

import java.awt.Color;
import java.util.List;

public class BackgroundSystem extends System {

    @Override
    public void update(float deltaTime, List<Entity> entities) {
        // Iterate over the provided list of entities
        for (Entity entity : entities) {
            ColorComponent colorComponent = (ColorComponent) entity.getComponent(ColorComponent.class);
            if (colorComponent != null) {
                toggleBackgroundColor(colorComponent);
            }
        }
    }

    // Toggle the color between white and black
    private void toggleBackgroundColor(ColorComponent colorComponent) {
        if (colorComponent.getColor().equals(Color.WHITE)) {
            colorComponent.setColor(Color.BLACK);
        } else if (colorComponent.getColor().equals(Color.BLACK)) {
            colorComponent.setColor(Color.WHITE);
        }
    }
}
