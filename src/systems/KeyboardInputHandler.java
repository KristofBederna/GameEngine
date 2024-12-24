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
        System.out.println("Created");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
        System.out.println("Key pressed: " + KeyEvent.getKeyText(e.getKeyCode()));
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        System.out.println("Key released: " + KeyEvent.getKeyText(e.getKeyCode()));
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
