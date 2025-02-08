package inf.elte.hu.gameengine_javafx.Misc.InputHandlers;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class KeyboardInputHandler {
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private final Set<KeyCode> releasedKeys = new HashSet<>();

    public KeyboardInputHandler(Scene scene) {
        scene.setOnKeyPressed(this::keyPressed);
        scene.setOnKeyReleased(this::keyReleased);
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
