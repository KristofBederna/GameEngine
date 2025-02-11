package inf.elte.hu.gameengine_javafx.Systems;

import inf.elte.hu.gameengine_javafx.Components.InteractiveComponent;
import inf.elte.hu.gameengine_javafx.Components.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Misc.InputHandlers.KeyboardInputHandler;
import inf.elte.hu.gameengine_javafx.Misc.InputHandlers.MouseInputHandler;
import inf.elte.hu.gameengine_javafx.Misc.Tuple;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import java.util.Map;

public class InputHandlingSystem extends GameSystem {
    @Override
    public void update(float deltaTime) {
        for (Entity entity : EntityHub.getInstance().getAllEntities()) {
            PositionComponent position = entity.getComponent(PositionComponent.class);
            VelocityComponent velocity = entity.getComponent(VelocityComponent.class);
            InteractiveComponent interactive = entity.getComponent(InteractiveComponent.class);

            if (position != null && velocity != null && interactive != null) {
                handleKeyboardInput(interactive);
                handleMouseInput(interactive);
            }
        }
    }

    private void handleKeyboardInput(InteractiveComponent interactive) {
        KeyboardInputHandler keyboardInputHandler = KeyboardInputHandler.getInstance();
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

    private void handleMouseInput(InteractiveComponent interactive) {
        MouseInputHandler mouseInputHandler = MouseInputHandler.getInstance();
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
