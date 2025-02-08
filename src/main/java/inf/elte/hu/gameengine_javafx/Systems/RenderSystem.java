package inf.elte.hu.gameengine_javafx.Systems;

import inf.elte.hu.gameengine_javafx.Components.CameraComponent;
import inf.elte.hu.gameengine_javafx.Components.ImageComponent;
import inf.elte.hu.gameengine_javafx.Components.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.RectangularHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.ResourceHub;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.List;

public class RenderSystem extends GameSystem {
    private GraphicsContext gc;
    private CameraComponent camera;

    public RenderSystem(GraphicsContext gc, CameraComponent camera) {
        this.gc = gc;
        this.camera = camera;
    }

    @Override
    public void update(float deltaTime, List<Entity> entities) {
        if (gc == null || gc.getCanvas() == null) {
            System.err.println("RenderSystem: GraphicsContext or Canvas is null!");
            return;
        }

        Platform.runLater(() -> {
            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

            for (Entity entity : entities) {
                PositionComponent position = entity.getComponent(PositionComponent.class);
                ImageComponent imgComponent = entity.getComponent(ImageComponent.class);

                if (position == null || imgComponent == null) continue;

                Image img = ResourceHub.getInstance().getResourceManager(Image.class).get(imgComponent.getImagePath());
                if (img == null) {
                    System.err.println("RenderSystem: Missing image for " + imgComponent.getImagePath());
                    continue;
                }

                double width = (imgComponent.getWidth() >= 0) ? imgComponent.getWidth() : img.getWidth();
                double height = (imgComponent.getHeight() >= 0) ? imgComponent.getHeight() : img.getHeight();

                double renderX = position.getX() - camera.getX();
                double renderY = position.getY() - camera.getY();

                if (renderX + width >= 0 && renderX <= camera.getWidth() &&
                        renderY + height >= 0 && renderY <= camera.getHeight()) {
                    gc.drawImage(img, renderX, renderY, width, height);
                }

                RectangularHitBoxComponent hitbox = entity.getComponent(RectangularHitBoxComponent.class);
                if (hitbox != null) {
                    gc.setStroke(Color.RED);
                    gc.strokeRect(renderX, renderY, hitbox.getHitBox().getWidth(), hitbox.getHitBox().getHeight());
                }
            }
        });
    }
}
