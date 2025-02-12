package inf.elte.hu.gameengine_javafx.Systems;

import inf.elte.hu.gameengine_javafx.Components.CameraComponent;
import inf.elte.hu.gameengine_javafx.Components.ImageComponent;
import inf.elte.hu.gameengine_javafx.Components.PositionComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Misc.Globals;

public class CameraSystem extends GameSystem {
    @Override
    public void update() {
        if (Globals.playerEntity == null || Globals.playerEntity.getComponent(CameraComponent.class) == null) return;

        PositionComponent playerPos = Globals.playerEntity.getComponent(PositionComponent.class);
        ImageComponent playerImg = Globals.playerEntity.getComponent(ImageComponent.class);

        if (playerPos == null || playerImg == null) return;

        double playerCenterX = playerPos.getX() + (double) playerImg.getWidth() / 2;
        double playerCenterY = playerPos.getY() + (double) playerImg.getHeight() / 2;

        double newX = playerCenterX - Globals.playerEntity.getComponent(CameraComponent.class).getWidth() / 2;
        double newY = playerCenterY - Globals.playerEntity.getComponent(CameraComponent.class).getHeight() / 2;

        Globals.playerEntity.getComponent(CameraComponent.class).setPosition(newX, newY);

        EntityHub.getInstance().getEntityManager(Globals.playerEntity.getClass()).updateLastUsed(Globals.playerEntity.getId());
    }
}
