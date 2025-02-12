package inf.elte.hu.gameengine_javafx.Systems;

import inf.elte.hu.gameengine_javafx.Components.*;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import inf.elte.hu.gameengine_javafx.Core.ResourceHub;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.List;

public class RenderSystem extends GameSystem {
    @Override
    public void update() {
        GraphicsContext gc = Globals.canvas.getGraphicsContext2D();
        CameraComponent camera = Globals.playerEntity.getComponent(CameraComponent.class);

        if (gc == null || gc.getCanvas() == null) {
            System.err.println("RenderSystem: GraphicsContext or Canvas is null!");
            return;
        }

        Platform.runLater(() -> {
            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

            List<Entity> visibleEntities = EntityHub.getInstance().getAllEntities();
            for (Entity entity: visibleEntities) {
                if (entity.getAllComponents().containsKey(CameraComponent.class)) {
                    visibleEntities = EntityHub.getInstance().getEntitiesInsideViewport(entity.getComponent(CameraComponent.class));
                    break;
                }
            }

            List<Entity> sortedEntities = visibleEntities.stream()
                    .filter(entity -> entity.getComponent(ZIndexComponent.class) != null)
                    .sorted((e1, e2) -> {
                        ZIndexComponent zIndex1 = e1.getComponent(ZIndexComponent.class);
                        ZIndexComponent zIndex2 = e2.getComponent(ZIndexComponent.class);
                        return Integer.compare(zIndex1.getZ_index(), zIndex2.getZ_index());
                    })
                    .toList();

            for (Entity entity : sortedEntities) {
                PositionComponent position = entity.getComponent(PositionComponent.class);
                ImageComponent imgComponent = entity.getComponent(ImageComponent.class);

                if (position == null || imgComponent == null) continue;

                double width = imgComponent.getWidth();
                double height = imgComponent.getHeight();

                double renderX = position.getX() - camera.getX();
                double renderY = position.getY() - camera.getY();

                if (renderX + width >= 0 && renderX <= camera.getWidth() &&
                        renderY + height >= 0 && renderY <= camera.getHeight()) {

                    Image img = ResourceHub.getInstance().getResourceManager(Image.class)
                            .get(imgComponent.getImagePath());

                    EntityHub.getInstance().getEntityManager(entity.getClass()).updateLastUsed(entity.getId());

                    if (img == null) {
                        System.err.println("RenderSystem: Missing image for " + imgComponent.getImagePath());
                        continue;
                    }

                    gc.drawImage(img, renderX, renderY, width, height);

                    RectangularHitBoxComponent hitBox = entity.getComponent(RectangularHitBoxComponent.class);
                    if (hitBox != null) {
                        gc.setStroke(Color.RED);
                        gc.strokeRect(renderX, renderY, hitBox.getHitBox().getWidth(), hitBox.getHitBox().getHeight());
                    }
                }
            }
        });
    }
}
