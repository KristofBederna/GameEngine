package systems;

import views.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class KeyboardInputHandler implements KeyListener {
    private final Set<Integer> pressedKeys = new HashSet<>();
    private final GamePanel GamePanel;

    public KeyboardInputHandler(GamePanel gamePanel) {
        this.GamePanel = gamePanel;
        GamePanel.addKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No-op
    }

    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    public Set<Integer> getPressedKeys() {
        return pressedKeys;
    }
}
