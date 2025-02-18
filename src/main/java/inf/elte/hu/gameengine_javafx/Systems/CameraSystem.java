package inf.elte.hu.gameengine_javafx.Systems;

import inf.elte.hu.gameengine_javafx.Components.ImageComponent;
import inf.elte.hu.gameengine_javafx.Components.PositionComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Misc.Camera;
import inf.elte.hu.gameengine_javafx.Misc.Globals;

public class CameraSystem extends GameSystem {
    @Override
    public void update() {
        PositionComponent playerPos = Camera.getInstance().getOwner().getComponent(PositionComponent.class);
        ImageComponent playerImg = Camera.getInstance().getOwner().getComponent(ImageComponent.class);
        Camera camera = Camera.getInstance();

        if (playerPos == null || playerImg == null) return;

        double playerCenterX = playerPos.getGlobalX() + playerImg.getWidth() / 2;
        double playerCenterY = playerPos.getGlobalY() + playerImg.getHeight() / 2;

        double newX = playerCenterX - camera.getWidth() / 2;
        double newY = playerCenterY - camera.getHeight() / 2;

        camera.setPosition(newX, newY);

        EntityHub.getInstance().getEntityManager(Camera.getInstance().getOwner().getClass()).updateLastUsed(Camera.getInstance().getOwner().getId());
    }
}
