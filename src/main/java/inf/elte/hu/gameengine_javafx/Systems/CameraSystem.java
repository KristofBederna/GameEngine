package inf.elte.hu.gameengine_javafx.Systems;

import inf.elte.hu.gameengine_javafx.Components.CameraComponent;
import inf.elte.hu.gameengine_javafx.Components.ImageComponent;
import inf.elte.hu.gameengine_javafx.Components.PositionComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;

import java.util.List;

public class CameraSystem extends GameSystem {
    private Entity player;
    private CameraComponent camera;

    public CameraSystem(Entity player, CameraComponent camera) {
        this.player = player;
        this.camera = camera;
    }

    @Override
    public void update(float deltaTime, List<Entity> entities) {
        if (player == null || camera == null) return;

        PositionComponent playerPos = player.getComponent(PositionComponent.class);
        ImageComponent playerImg = player.getComponent(ImageComponent.class);

        if (playerPos == null || playerImg == null) return;

        double playerCenterX = playerPos.getX() + (double) playerImg.getWidth() / 2;
        double playerCenterY = playerPos.getY() + (double) playerImg.getHeight() / 2;

        double newX = playerCenterX - camera.getWidth() / 2;
        double newY = playerCenterY - camera.getHeight() / 2;

        camera.setPosition(newX, newY);
    }
}
