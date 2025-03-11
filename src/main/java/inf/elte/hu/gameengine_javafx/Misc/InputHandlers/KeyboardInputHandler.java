package inf.elte.hu.gameengine_javafx.Misc.InputHandlers;

import inf.elte.hu.gameengine_javafx.Misc.Layers.GameCanvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

public class KeyboardInputHandler {
    private static KeyboardInputHandler instance = null;
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private final Set<KeyCode> releasedKeys = new HashSet<>();

    private KeyboardInputHandler() {
        GameCanvas.getInstance().getScene().setOnKeyPressed(this::keyPressed);
        GameCanvas.getInstance().getScene().setOnKeyReleased(this::keyReleased);
    }

    public static KeyboardInputHandler getInstance() {
        if (instance == null) {
            instance = new KeyboardInputHandler();
        }
        return instance;
    }

    private void keyPressed(KeyEvent event) {
        KeyCode key = event.getCode();

        if (!pressedKeys.contains(key)) {
            pressedKeys.add(key);
            releasedKeys.remove(key);
        }
    }

    private void keyReleased(KeyEvent event) {
        KeyCode key = event.getCode();
        pressedKeys.remove(key);
        releasedKeys.add(key);
    }

    public boolean isKeyPressed(KeyCode keyCode) {
        return pressedKeys.contains(keyCode);
    }

    public boolean isKeyReleased(KeyCode keyCode) {
        boolean wasReleased = releasedKeys.contains(keyCode);
        if (wasReleased) {
            releasedKeys.remove(keyCode);
        }
        return wasReleased;
    }

    public Set<KeyCode> getPressedKeys() {
        return new HashSet<>(pressedKeys);
    }
}
