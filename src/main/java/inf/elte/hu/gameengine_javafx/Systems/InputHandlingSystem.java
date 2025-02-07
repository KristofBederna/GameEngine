package inf.elte.hu.gameengine_javafx.Systems;

import inf.elte.hu.gameengine_javafx.Components.InteractiveComponent;
import inf.elte.hu.gameengine_javafx.Components.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Core.Entity;
import inf.elte.hu.gameengine_javafx.Core.GameSystem;
import inf.elte.hu.gameengine_javafx.Misc.KeyboardInputHandler;
import inf.elte.hu.gameengine_javafx.Misc.MouseInputHandler;
import inf.elte.hu.gameengine_javafx.Misc.Tuple;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import java.util.List;
import java.util.Map;

public class InputHandlingSystem extends GameSystem {
    private final KeyboardInputHandler keyboardInputHandler;
    private final MouseInputHandler mouseInputHandler;

    public InputHandlingSystem(KeyboardInputHandler keyboardInputHandler, MouseInputHandler mouseInputHandler) {
        this.keyboardInputHandler = keyboardInputHandler;
        this.mouseInputHandler = mouseInputHandler;
    }

    @Override
    public void update(float deltaTime, List<Entity> entities) {
        for (Entity entity : entities) {
            PositionComponent position = entity.getComponent(PositionComponent.class);
            VelocityComponent velocity = entity.getComponent(VelocityComponent.class);
            InteractiveComponent interactive = entity.getComponent(InteractiveComponent.class);

            if (position != null && velocity != null && interactive != null) {
                handleKeyboardInput(interactive);
                handleMouseInput(interactive, position);
            }
        }
    }

    private void handleKeyboardInput(InteractiveComponent interactive) {
        for (Map.Entry<KeyCode, Tuple<Runnable, Runnable>> entry : interactive.getKeyInputMapping().entrySet()) {
            KeyCode keyCode = entry.getKey();
            Runnable action = entry.getValue().first();
            Runnable counterAction = entry.getValue().second();

            if (keyboardInputHandler.isKeyPressed(keyCode)) {
                action.run();
            }
            if (keyboardInputHandler.isKeyReleased(keyCode) && counterAction != null) {
                counterAction.run();
            }
        }
    }

    private void handleMouseInput(InteractiveComponent interactive, PositionComponent position) {
        for (Map.Entry<MouseButton, Tuple<Runnable, Runnable>> entry : interactive.getMouseInputMapping().entrySet()) {
            MouseButton mouseButton = entry.getKey();
            Runnable action = entry.getValue().first();
            Runnable counterAction = entry.getValue().second();

            if (mouseInputHandler.isButtonPressed(mouseButton)) {
                action.run();
            }
            if (mouseInputHandler.isButtonReleased(mouseButton) && counterAction != null) {
                counterAction.run();
            }
        }
    }
}
