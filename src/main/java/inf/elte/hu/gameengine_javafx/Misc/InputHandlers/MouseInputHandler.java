package inf.elte.hu.gameengine_javafx.Misc.InputHandlers;

import inf.elte.hu.gameengine_javafx.Components.PositionComponent;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MouseInputHandler {
    private static MouseInputHandler instance;
    private final Set<MouseButton> pressedButtons = new HashSet<>();
    private final Set<MouseButton> releasedButtons = new HashSet<>();
    private final Map<MouseButton, Long> lastPressedTime = new HashMap<>();

    private int mouseX, mouseY;
    private double scrollDeltaY;
    private static final long PRESS_COOLDOWN = 100;

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
        long currentTime = System.currentTimeMillis();
        MouseButton button = event.getButton();

        if (!pressedButtons.contains(button) ||
                (lastPressedTime.getOrDefault(button, 0L) + PRESS_COOLDOWN < currentTime)) {

            lastPressedTime.put(button, currentTime);
            pressedButtons.add(button);
            releasedButtons.clear();
        }
    }

    private void mouseReleased(MouseEvent event) {
        MouseButton button = event.getButton();
        pressedButtons.remove(button);
        releasedButtons.add(button);
    }

    private void mouseMoved(MouseEvent event) {
        CameraEntity cameraEntity = CameraEntity.getInstance();
        if (cameraEntity == null) return;

        mouseX = (int) (event.getX() + cameraEntity.getComponent(PositionComponent.class).getGlobalX());
        mouseY = (int) (event.getY() + cameraEntity.getComponent(PositionComponent.class).getGlobalY());
    }

    private void mouseScrolled(ScrollEvent event) {
        scrollDeltaY = event.getDeltaY();
    }

    public boolean isButtonPressed(MouseButton button) {
        return pressedButtons.contains(button);
    }

    public boolean isButtonReleased(MouseButton button) {
        boolean wasReleased = releasedButtons.contains(button);
        if (wasReleased) {
            releasedButtons.remove(button);
        }
        return wasReleased;
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
