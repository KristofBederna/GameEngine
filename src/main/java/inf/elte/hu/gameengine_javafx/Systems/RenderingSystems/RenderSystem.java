package inf.elte.hu.gameengine_javafx.Systems.RenderingSystems;

import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.DimensionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ImageComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ZIndexComponent;
import inf.elte.hu.gameengine_javafx.Components.ShapeComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.EntityManager;
import inf.elte.hu.gameengine_javafx.Core.ResourceManager;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import inf.elte.hu.gameengine_javafx.Core.ResourceHub;
import inf.elte.hu.gameengine_javafx.Entities.ParticleEntity;
import inf.elte.hu.gameengine_javafx.Misc.Layers.GameCanvas;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

public class RenderSystem extends GameSystem {
    @Override
    public void start() {
        this.active = true;
    }

    @Override
    public void update() {
        GraphicsContext gc = GameCanvas.getInstance().getGraphicsContext2D();
        CameraEntity cameraEntity = CameraEntity.getInstance();

        if (gc == null || gc.getCanvas() == null) {
            System.err.println("RenderSystem: GraphicsContext or Canvas is null!");
            return;
        }

        Platform.runLater(() -> {
            gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

            List<Entity> visibleEntities = EntityHub.getInstance().getEntitiesInsideViewport(CameraEntity.getInstance());
            if (visibleEntities == null) {
                return;
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

                double renderX = position.getGlobalX() - cameraEntity.getComponent(PositionComponent.class).getGlobalX();
                double renderY = position.getGlobalY() - cameraEntity.getComponent(PositionComponent.class).getGlobalY();

                if (renderX + width >= 0 && renderX <= cameraEntity.getComponent(DimensionComponent.class).getWidth() &&
                        renderY + height >= 0 && renderY <= cameraEntity.getComponent(DimensionComponent.class).getHeight()) {

                    ResourceManager<Image> imageManager = ResourceHub.getInstance().getResourceManager(Image.class);
                    if (imageManager == null) continue;
                    Image img = imageManager.get(imgComponent.getImagePath());

                    EntityManager<Entity> entityManager = (EntityManager<Entity>)EntityHub.getInstance().getEntityManager(entity.getClass());

                    if (entityManager == null) continue;

                    entityManager.updateLastUsed(entity.getId());

                    if (img == null) {
                        System.err.println("RenderSystem: Missing image for " + imgComponent.getImagePath());
                        continue;
                    }

                    gc.drawImage(img, renderX, renderY, width, height);

//                    RectangularHitBoxComponent hitBox = entity.getComponent(RectangularHitBoxComponent.class);
//                    if (hitBox != null) {
//                        hitBox.getHitBox().render(gc, Color.RED);
//                    }
//                    TriangularHitBoxComponent hitBox2 = entity.getComponent(TriangularHitBoxComponent.class);
//                    if (hitBox2 != null) {
//                        hitBox2.getHitBox().render(gc, Color.RED);
//                    }
//                    NSidedHitBoxComponent hitBox3 = entity.getComponent(NSidedHitBoxComponent.class);
//                    if (hitBox3 != null) {
//                        hitBox3.getHitBox().render(gc, Color.RED);
//                    }
//                    ComplexHitBoxComponent hitBox4 = entity.getComponent(ComplexHitBoxComponent.class);
//                    if (hitBox4 != null) {
//                        hitBox4.getHitBox().render(gc, Color.RED);
//                    }
                }
            }
//            MapMeshComponent meshComponent = WorldEntity.getInstance().getComponent(MapMeshComponent.class);
//            if (meshComponent != null) {
//                for (List<Point> row : meshComponent.getMapCoordinates()) {
//                    for (Point point : row) {
//                        if (point == null) continue;
//                        point.render(gc, 5, Color.YELLOW);
//                    }
//                }
//            }
//            for (Entity entity : EntityHub.getInstance().getEntitiesWithComponent(PathfindingComponent.class)) {
//                PathfindingComponent pathfindingComponent = entity.getComponent(PathfindingComponent.class);
//                if (pathfindingComponent.getPath() == null) {
//                    continue;
//                }
//                if (pathfindingComponent.getPath().isEmpty()) {
//                    continue;
//                }
//                for (Point neighbour : pathfindingComponent.getNeighbours(pathfindingComponent.getPath().getFirst())) {
//                    Line line = new Line(pathfindingComponent.getPath().getFirst(), neighbour);
//                    line.render(gc, Color.ORANGE, 5);
//                }
//            }
            for (Entity entity : EntityHub.getInstance().getEntitiesWithType(ParticleEntity.class)) {
                ((ParticleEntity)entity).render(gc);
            }
            if (!GameCanvas.getInstance().isFocused()) {
                GameCanvas.getInstance().requestFocus();
            }
        });
    }
}
