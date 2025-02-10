package inf.elte.hu.gameengine_javafx.Misc.InputHandlers;

import inf.elte.hu.gameengine_javafx.Components.CameraComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.SystemHub;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MouseInputHandler {
    private static MouseInputHandler instance;
    private final Set<MouseButton> pressedButtons = new HashSet<>();
    private final Set<MouseButton> releasedButtons = new HashSet<>();
    private int mouseX, mouseY;
    private double scrollDeltaY;

    private MouseInputHandler(Scene scene) {
        scene.setOnMousePressed(this::mousePressed);
        scene.setOnMouseReleased(this::mouseReleased);
        scene.setOnMouseMoved(this::mouseMoved);
        scene.setOnMouseDragged(this::mouseMoved);
        scene.setOnScroll(this::mouseScrolled);
    }

    public static MouseInputHandler getInstance(Scene scene) {
        if (instance == null) {
            instance = new MouseInputHandler(scene);
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
        Entity cameraEntity = null;
        for (Entity entity: EntityHub.getInstance().getAllEntities()) {
            if (entity.getAllComponents().containsKey(CameraComponent.class)) {
                cameraEntity = entity;
                break;
            }
        }
        assert cameraEntity != null;
        mouseX = (int) (event.getX() + cameraEntity.getComponent(CameraComponent.class).getX());
        mouseY = (int) (event.getY() + cameraEntity.getComponent(CameraComponent.class).getY());
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
