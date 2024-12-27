package systems;

import views.GamePanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;

public class KeyboardInputHandler implements KeyListener {
    private final Set<Integer> pressedKeys = new HashSet<>();
    private final Set<Integer> releasedKeys = new HashSet<>();
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
        releasedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not implemented
    }

    public boolean isKeyPressed(int keyCode) {
        return pressedKeys.contains(keyCode);
    }

    public boolean isKeyReleased(int keyCode) {
        if (releasedKeys.contains(keyCode)) {
            releasedKeys.remove(keyCode);
            return true;
        }
        return false;
    }

    public Set<Integer> getPressedKeys() {
        return pressedKeys;
    }
}
