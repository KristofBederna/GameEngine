package systems;

import components.MoveableComponent;
import components.PositionComponent;
import components.VelocityComponent;
import core.Entity;
import core.System;
import miscs.Tuple;

import java.util.List;
import java.util.Map;

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
                handleInput(moveable);
            } else {
                throw new Error("Trying to apply keyboard movement to entity without Position, Velocity and Moveable components");
            }
        }
    }


    private void handleInput(MoveableComponent moveable) {
        for (Map.Entry<Integer, Tuple<Runnable, Runnable>> entry : moveable.getInputMapping().entrySet()) {
            int keyCode = entry.getKey();
            Runnable action = entry.getValue().first();
            Runnable counterAction = entry.getValue().second() == null ? null : entry.getValue().second();
            if (inputHandler.isKeyPressed(keyCode)) {
                action.run();
            }
            if (inputHandler.isKeyReleased(keyCode) && counterAction != null) {
                counterAction.run();
            }
        }
    }
}
