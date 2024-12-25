package systems;

import components.InteractiveComponent;
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
            InteractiveComponent interactive = entity.getComponent(InteractiveComponent.class);

            if (position != null && velocity != null && interactive != null) {
                handleInput(interactive);
            }
        }
    }


    private void handleInput(InteractiveComponent interactive) {
        for (Map.Entry<Integer, Tuple<Runnable, Runnable>> entry : interactive.getInputMapping().entrySet()) {
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
