package TestSuite.InputHandling;

import TestSuite.scenes.SandboxScene;
import inf.elte.hu.gameengine_javafx.Misc.InputHandlers.KeyboardInputHandler;
import inf.elte.hu.gameengine_javafx.Misc.Layers.GameCanvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class KeyboardInputTests {

    private KeyboardInputHandler handler;
    private Method keyPressedMethod;
    private Method keyReleasedMethod;

    @BeforeEach
    void setup() throws Exception {
        handler = KeyboardInputHandler.getInstance(true);

        keyPressedMethod = handler.getClass().getDeclaredMethod("keyPressed", KeyEvent.class);
        keyPressedMethod.setAccessible(true);

        keyReleasedMethod = handler.getClass().getDeclaredMethod("keyReleased", KeyEvent.class);
        keyReleasedMethod.setAccessible(true);

        Set<KeyCode> pressed = handler.getPressedKeys();
        for (KeyCode code : pressed) {
            invokeReleased(code);
        }
    }

    private void invokePressed(KeyCode keyCode) {
        KeyEvent event = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", keyCode, false, false, false, false);
        try {
            keyPressedMethod.invoke(handler, event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void invokeReleased(KeyCode keyCode) {
        KeyEvent event = new KeyEvent(KeyEvent.KEY_RELEASED, "", "", keyCode, false, false, false, false);
        try {
            keyReleasedMethod.invoke(handler, event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testPressAndRelease() {
        assertFalse(handler.isKeyPressed(KeyCode.A));
        assertFalse(handler.isKeyReleased(KeyCode.A));

        invokePressed(KeyCode.A);
        assertTrue(handler.isKeyPressed(KeyCode.A));
        assertFalse(handler.isKeyReleased(KeyCode.A));

        invokeReleased(KeyCode.A);
        assertFalse(handler.isKeyPressed(KeyCode.A));
        assertTrue(handler.isKeyReleased(KeyCode.A));

        assertFalse(handler.isKeyReleased(KeyCode.A));
    }

    @Test
    void testPressedKeysReturnsCopy() {
        invokePressed(KeyCode.SPACE);
        Set<KeyCode> keys = handler.getPressedKeys();
        assertTrue(keys.contains(KeyCode.SPACE));

        keys.remove(KeyCode.SPACE);
        assertTrue(handler.isKeyPressed(KeyCode.SPACE));
    }

    @Test
    void testMultipleKeys() {
        invokePressed(KeyCode.LEFT);
        invokePressed(KeyCode.RIGHT);

        assertTrue(handler.isKeyPressed(KeyCode.LEFT));
        assertTrue(handler.isKeyPressed(KeyCode.RIGHT));

        invokeReleased(KeyCode.LEFT);

        assertFalse(handler.isKeyPressed(KeyCode.LEFT));
        assertTrue(handler.isKeyReleased(KeyCode.LEFT));
        assertTrue(handler.isKeyPressed(KeyCode.RIGHT));
    }
}
