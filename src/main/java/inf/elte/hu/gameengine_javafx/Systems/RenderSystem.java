package inf.elte.hu.gameengine_javafx.Systems;

import inf.elte.hu.gameengine_javafx.Components.ImageComponent;
import inf.elte.hu.gameengine_javafx.Components.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.RectangularHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Core.Entity;
import inf.elte.hu.gameengine_javafx.Core.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.ResourceHub;
import inf.elte.hu.gameengine_javafx.Entities.DebugInfoEntity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.List;

public class RenderSystem extends GameSystem {
    GraphicsContext gc;

    public RenderSystem(GraphicsContext gc) {
        this.gc = gc;
    }

    @Override
    public void update(float deltaTime, List<Entity> entities) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        for (Entity entity : entities) {
            PositionComponent position = entity.getComponent(PositionComponent.class);
            ImageComponent imgComponent = entity.getComponent(ImageComponent.class);

            if (position != null && imgComponent != null) {
                Image img = ResourceHub.getInstance().getResourceManager(Image.class).get(imgComponent.getImagePath());
                double width = (imgComponent.getWidth() >= 0) ? imgComponent.getWidth() : img.getWidth();
                double height = (imgComponent.getHeight() >= 0) ? imgComponent.getHeight() : img.getHeight();

                gc.drawImage(img, position.getX(), position.getY(), width, height);
            }

            // DEBUG mode: Draw hitbox
            RectangularHitBoxComponent hitbox = entity.getComponent(RectangularHitBoxComponent.class);
            if (hitbox != null) {
                gc.setStroke(Color.RED);
                gc.strokeRect(position.getX(), position.getY(), hitbox.getHitBox().getWidth(), hitbox.getHitBox().getHeight());
            }

            if (entity.getClass() == DebugInfoEntity.class) {

            }
        }
    }
}
