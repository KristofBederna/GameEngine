package inf.elte.hu.gameengine_javafx.Systems.RenderingSystems;

import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.LightHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.LightComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.DimensionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.RadiusComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ImageComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ZIndexComponent;
import inf.elte.hu.gameengine_javafx.Components.ShapeComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.MapMeshComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.EntityManager;
import inf.elte.hu.gameengine_javafx.Core.ResourceManager;
import inf.elte.hu.gameengine_javafx.Entities.*;
import inf.elte.hu.gameengine_javafx.Core.ResourceHub;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.*;
import inf.elte.hu.gameengine_javafx.Misc.Layers.GameCanvas;
import inf.elte.hu.gameengine_javafx.Misc.Time;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
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
            MapMeshComponent meshComponent = WorldEntity.getInstance().getComponent(MapMeshComponent.class);
            if (meshComponent != null) {
                for (List<Point> row : meshComponent.getMapCoordinates()) {
                    for (Point point : row) {
                        if (point == null) continue;
                        point.renderFill(gc, 5, Color.YELLOW);
                    }
                }
            }
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
                ((ParticleEntity) entity).alignShapeWithEntity(entity);
                ((ParticleEntity)entity).render(gc);
            }
//            handleLighting(gc);

            if (!GameCanvas.getInstance().isFocused()) {
                GameCanvas.getInstance().requestFocus();
            }
        });
    }

    private void handleLighting(GraphicsContext gc) {
        List<Point> points = new ArrayList<>();
        points.add(new Point(0,0));
        points.add(new Point(0, 1500));
        points.add(new Point(3000, 1500));
        points.add(new Point(3000, 0));
        ComplexShape darkness = new ComplexShape(points);
        for (Entity entity : EntityHub.getInstance().getEntitiesWithComponent(LightComponent.class)) {
            if (entity.getId() == EntityHub.getInstance().getEntitiesWithComponent(LightComponent.class).getFirst().getId()) {
                ((LightingEntity)entity).matchPositionToEntity(EntityHub.getInstance().getEntitiesWithType(PlayerEntity.class).getFirst());
            }

            ((LightingEntity)entity).calculateCollisions();
            //((LightingEntity)entity).renderRays(gc);

            ComplexShape complexShape = ((LightingEntity)entity).createShapeFromRays();
            double firstPos = complexShape.getPoints().getFirst().getX();
            darkness.getPoints().add(new Point(firstPos, 0));
            darkness.updateEdges();
            darkness.addShape(complexShape);
            darkness.getPoints().add(new Point(firstPos, complexShape.getPoints().getFirst().getY()));
            darkness.updateEdges();
            darkness.getPoints().add(new Point(firstPos, 0));
            darkness.updateEdges();
            complexShape.renderFill(gc, new Color(1,1,1,0.2));
        }
        for (Edge edge : darkness.getEdges()) {
            new Line(edge.getBeginning(), edge.getEnd()).render(gc, Color.YELLOW, 3);
        }
        for (Entity entity : EntityHub.getInstance().getEntitiesWithType(LightingEntity.class)) {
            new Point(entity.getComponent(PositionComponent.class).getGlobalX(), entity.getComponent(PositionComponent.class).getGlobalY()).render(gc, 3, Color.CYAN);
        }
        List<Point> toRemove = new ArrayList<>();
        for (Point point : darkness.getPoints()) {
            boolean isLit = false;

            for (Entity entity : EntityHub.getInstance().getEntitiesWithType(LightingEntity.class)) {
                double entityX = entity.getComponent(PositionComponent.class).getGlobalX();
                double entityY = entity.getComponent(PositionComponent.class).getGlobalY();
                double radius = entity.getComponent(RadiusComponent.class).getRadius();
                LightHitBoxComponent hitBox = entity.getComponent(LightHitBoxComponent.class);

                if (point.distanceTo(new Point(entityX, entityY)) <= radius-1) {
                    if (!hitBox.getHitBox().getPoints().contains(point)) {
                        isLit = true;
                        toRemove.add(point);
                        break;
                    }
                }
            }

            if (!isLit) {
                point.renderFill(gc, 3, Color.ORANGE);
            }
        }

        for (Point point : toRemove) {
            darkness.getPoints().remove(point);
        }
        darkness.updateEdges();
        darkness.renderFill(gc, new Color(0, 0, 0, 0.7));
    }
}
