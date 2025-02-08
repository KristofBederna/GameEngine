package inf.elte.hu.gameengine_javafx.Misc.InputHandlers;

import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.util.HashSet;
import java.util.Set;

public class MouseInputHandler {
    private final Set<MouseButton> pressedButtons = new HashSet<>();
    private final Set<MouseButton> releasedButtons = new HashSet<>();
    private int mouseX, mouseY;
    private double scrollDeltaY;

    public MouseInputHandler(Scene scene) {
        scene.setOnMousePressed(this::mousePressed);
        scene.setOnMouseReleased(this::mouseReleased);
        scene.setOnMouseMoved(this::mouseMoved);
        scene.setOnMouseDragged(this::mouseMoved);
        scene.setOnScroll(this::mouseScrolled);
    }

    private void mousePressed(MouseEvent event) {
        releasedButtons.clear();
        pressedButtons.add(event.getButton());
    }

    private void mouseReleased(MouseEvent event) {
        pressedButtons.remove(event.getButton());
        releasedButtons.add(event.getButton());
    }

    private void mouseMoved(MouseEvent event) {
        mouseX = (int) event.getX();
        mouseY = (int) event.getY();
    }

    private void mouseScrolled(ScrollEvent event) {
        scrollDeltaY = event.getDeltaY();
    }

    public boolean isButtonPressed(MouseButton button) {
        return pressedButtons.contains(button);
    }

    public boolean isButtonReleased(MouseButton button) {
        return releasedButtons.contains(button);
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public double getScrollDeltaY() {
        return scrollDeltaY;
    }
}
