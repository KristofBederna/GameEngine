package inf.elte.hu.gameengine_javafx.Systems.RenderingSystems;

import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.DimensionComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ImageComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;

public class CameraSystem extends GameSystem {
    @Override
    public void start() {
        this.active = true;
    }

    @Override
    public void update() {
        if (CameraEntity.getInstance() == null) {
            return;
        }
        if (CameraEntity.getInstance().getOwner() == null) {
            return;
        }
        PositionComponent playerPos = CameraEntity.getInstance().getOwner().getComponent(PositionComponent.class);
        ImageComponent playerImg = CameraEntity.getInstance().getOwner().getComponent(ImageComponent.class);
        CameraEntity cameraEntity = CameraEntity.getInstance();

        if (playerPos == null || playerImg == null) return;

        double playerCenterX = playerPos.getGlobalX() + playerImg.getWidth() / 2;
        double playerCenterY = playerPos.getGlobalY() + playerImg.getHeight() / 2;

        double newX = playerCenterX - cameraEntity.getComponent(DimensionComponent.class).getWidth() / 2;
        double newY = playerCenterY - cameraEntity.getComponent(DimensionComponent.class).getHeight() / 2;

        cameraEntity.setPosition(newX, newY);

        EntityHub.getInstance().getEntityManager(CameraEntity.getInstance().getOwner().getClass()).updateLastUsed(CameraEntity.getInstance().getOwner().getId());
    }
}
