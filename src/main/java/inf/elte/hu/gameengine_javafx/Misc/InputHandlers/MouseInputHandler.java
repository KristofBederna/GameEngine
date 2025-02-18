package inf.elte.hu.gameengine_javafx.Misc.InputHandlers;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Misc.Camera;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.util.HashSet;
import java.util.Set;

public class MouseInputHandler {
    private static MouseInputHandler instance;
    private final Set<MouseButton> pressedButtons = new HashSet<>();
    private final Set<MouseButton> releasedButtons = new HashSet<>();
    private int mouseX, mouseY;
    private double scrollDeltaY;

    private MouseInputHandler() {
        Scene scene = Globals.canvas.getScene();
        scene.setOnMousePressed(this::mousePressed);
        scene.setOnMouseReleased(this::mouseReleased);
        scene.setOnMouseMoved(this::mouseMoved);
        scene.setOnMouseDragged(this::mouseMoved);
        scene.setOnScroll(this::mouseScrolled);
    }

    public static MouseInputHandler getInstance() {
        if (instance == null) {
            instance = new MouseInputHandler();
        }
        return instance;
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
        Camera camera = Camera.getInstance();

        if (camera == null) return;
        mouseX = (int) (event.getX() + camera.getX());
        mouseY = (int) (event.getY() + camera.getY());
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
