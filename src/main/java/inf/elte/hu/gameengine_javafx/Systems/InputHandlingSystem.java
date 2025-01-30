package inf.elte.hu.gameengine_javafx.Systems;

import inf.elte.hu.gameengine_javafx.Components.InteractiveComponent;
import inf.elte.hu.gameengine_javafx.Components.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Core.Entity;
import inf.elte.hu.gameengine_javafx.Core.GameSystem;
import inf.elte.hu.gameengine_javafx.Misc.KeyboardInputHandler;
import inf.elte.hu.gameengine_javafx.Misc.Tuple;
import javafx.scene.input.KeyCode;

import java.util.List;
import java.util.Map;

public class InputHandlingSystem extends GameSystem {
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
        for (Map.Entry<KeyCode, Tuple<Runnable, Runnable>> entry : interactive.getInputMapping().entrySet()) {
            KeyCode keyCode = entry.getKey();
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
