package inf.elte.hu.gameengine_javafx.Systems;

import inf.elte.hu.gameengine_javafx.Components.InteractiveComponent;
import inf.elte.hu.gameengine_javafx.Components.Default.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Misc.InputHandlers.KeyboardInputHandler;
import inf.elte.hu.gameengine_javafx.Misc.InputHandlers.MouseInputHandler;
import inf.elte.hu.gameengine_javafx.Misc.Tuple;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InputHandlingSystem extends GameSystem {
    @Override
    public void start() {
        this.active = true;
    }

    @Override
    public void update() {
        var entitiesSnapshot = EntityHub.getInstance().getEntitiesWithComponent(InteractiveComponent.class);

        if (entitiesSnapshot.isEmpty()) {
            return;
        }

        for (Entity entity : entitiesSnapshot) {
            processEntity(entity);
        }
    }

    private void processEntity(Entity entity) {
        if (entity == null) return;
        PositionComponent position = entity.getComponent(PositionComponent.class);
        VelocityComponent velocity = entity.getComponent(VelocityComponent.class);
        InteractiveComponent interactive = entity.getComponent(InteractiveComponent.class);

        if (position != null && velocity != null && interactive != null) {
            handleKeyboardInput(interactive);
            handleMouseInput(interactive);
        }
    }

    private void handleKeyboardInput(InteractiveComponent interactive) {
        KeyboardInputHandler keyboardInputHandler = KeyboardInputHandler.getInstance();
        List<Map.Entry<KeyCode, Tuple<Runnable, Runnable>>> snapshot = new ArrayList<>(interactive.getKeyInputMapping().entrySet());
        Map<Tuple<KeyCode, MouseButton>, Tuple<Long, Long>> lastTimeCalled = interactive.getLastTimeCalled();

        for (Map.Entry<KeyCode, Tuple<Runnable, Runnable>> entry : snapshot) {
            KeyCode keyCode = entry.getKey();
            Runnable action = entry.getValue().first();
            Runnable counterAction = entry.getValue().second();

            if (keyboardInputHandler.isKeyPressed(keyCode)) {
                if (System.currentTimeMillis() > lastTimeCalled.get(new Tuple<>(keyCode, null)).first() + lastTimeCalled.get(new Tuple<>(keyCode, null)).second()) {
                    action.run();
                    lastTimeCalled.put(new Tuple<>(keyCode, null), new Tuple<>(System.currentTimeMillis(), lastTimeCalled.get(new Tuple<>(keyCode, null)).second()));
                }
            }
            if (keyboardInputHandler.isKeyReleased(keyCode) && counterAction != null) {
                counterAction.run();
            }
        }

    }

    private void handleMouseInput(InteractiveComponent interactive) {
        MouseInputHandler mouseInputHandler = MouseInputHandler.getInstance();
        Map<Tuple<KeyCode, MouseButton>, Tuple<Long, Long>> lastTimeCalled = interactive.getLastTimeCalled();
        for (Map.Entry<MouseButton, Tuple<Runnable, Runnable>> entry : interactive.getMouseInputMapping().entrySet()) {
            MouseButton mouseButton = entry.getKey();
            Runnable action = entry.getValue().first();
            Runnable counterAction = entry.getValue().second();

            if (mouseInputHandler.isButtonPressed(mouseButton)) {
                if (System.currentTimeMillis() > lastTimeCalled.get(new Tuple<>(null, mouseButton)).first() + lastTimeCalled.get(new Tuple<>(null, mouseButton)).second()) {
                    action.run();
                    lastTimeCalled.put(new Tuple<>(null, mouseButton), new Tuple<>(System.currentTimeMillis(), lastTimeCalled.get(new Tuple<>(null, mouseButton)).second()));
                }
            }
            if (mouseInputHandler.isButtonReleased(mouseButton) && counterAction != null) {
                counterAction.run();
            }
        }
    }
}
