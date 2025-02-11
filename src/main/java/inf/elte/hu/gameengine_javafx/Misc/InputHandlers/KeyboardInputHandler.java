package inf.elte.hu.gameengine_javafx.Misc.InputHandlers;

import inf.elte.hu.gameengine_javafx.Misc.Globals;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class KeyboardInputHandler {
    private static KeyboardInputHandler instance = null;
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private final Set<KeyCode> releasedKeys = new HashSet<>();

    private KeyboardInputHandler() {
        Globals.canvas.getScene().setOnKeyPressed(this::keyPressed);
        Globals.canvas.getScene().setOnKeyReleased(this::keyReleased);
    }

    public static KeyboardInputHandler getInstance() {
        if (instance == null) {
            instance = new KeyboardInputHandler();
        }
        return instance;
    }

    private void keyPressed(KeyEvent event) {
        releasedKeys.clear();
        pressedKeys.add(event.getCode());
    }

    private void keyReleased(KeyEvent event) {
        pressedKeys.remove(event.getCode());
        releasedKeys.add(event.getCode());
    }

    public boolean isKeyPressed(KeyCode keyCode) {
        return pressedKeys.contains(keyCode);
    }

    public boolean isKeyReleased(KeyCode keyCode) {
        return releasedKeys.contains(keyCode);
    }

    public Set<KeyCode> getPressedKeys() {
        return pressedKeys;
    }
}
