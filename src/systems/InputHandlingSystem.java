package systems;

import components.MoveableComponent;
import components.PositionComponent;
import components.VelocityComponent;
import core.Entity;
import core.System;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Set;

public class InputHandlingSystem extends System {
    private final KeyboardInputHandler inputHandler;

    public InputHandlingSystem(KeyboardInputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    @Override
    public void update(float deltaTime, List<Entity> entities) {
        for (Entity entity : entities) {
            PositionComponent position = entity.getComponent(PositionComponent.class);
            VelocityComponent velocity = entity.getComponent(VelocityComponent.class);
            MoveableComponent moveable = entity.getComponent(MoveableComponent.class);

            if (position != null && velocity != null && moveable != null) {
                handleInput(velocity, deltaTime);
            } else {
                throw new Error("Trying to apply keyboard movement to entity without Position, Velocity and Moveable components");
            }
        }
    }


    private void handleInput(VelocityComponent velocity, float deltaTime) {
        int speed = 1;

        int dx = 0;
        int dy = 0;

        Set<Integer> inputs = inputHandler.getPressedKeys();

        if (inputs.contains(KeyEvent.VK_W)) {
            dy -= speed; // Move up
        }
        if (inputs.contains(KeyEvent.VK_S)) {
            dy += speed; // Move down
        }
        if (inputs.contains(KeyEvent.VK_A)) {
            dx -= speed; // Move left
        }
        if (inputs.contains(KeyEvent.VK_D)) {
            dx += speed; // Move right
        }

        velocity.setDx(dx);
        velocity.setDy(dy);
    }
}
