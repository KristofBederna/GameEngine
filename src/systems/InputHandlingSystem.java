package systems;

import components.MoveableComponent;
import components.PositionComponent;
import components.VelocityComponent;
import core.Entity;
import core.System;

import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;
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
                handleInput(moveable, velocity, deltaTime);
            } else {
                throw new Error("Trying to apply keyboard movement to entity without Position, Velocity and Moveable components");
            }
        }
    }


    private void handleInput(MoveableComponent moveable, VelocityComponent velocity, float deltaTime) {
        int speed = 1;

        int dx = 0;
        int dy = 0;

        for (Map.Entry<Integer, String> entry : moveable.getInputMapping().entrySet()) {
            int keyCode = entry.getKey();
            String action = entry.getValue();

            if (inputHandler.isKeyPressed(keyCode)) {
                switch (action) {
                    case "MOVE_UP" -> dy -= speed;
                    case "MOVE_DOWN" -> dy += speed;
                    case "MOVE_LEFT" -> dx -= speed;
                    case "MOVE_RIGHT" -> dx += speed;
                }
            }
        }

        velocity.setDx(dx);
        velocity.setDy(dy);
    }

}
