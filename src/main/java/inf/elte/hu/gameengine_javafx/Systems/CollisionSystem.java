package inf.elte.hu.gameengine_javafx.Systems;


import inf.elte.hu.gameengine_javafx.Components.*;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Rectangle;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Shape;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Triangle;

import java.util.List;

public class CollisionSystem extends GameSystem {

    @Override
    public void update() {
        List<Entity> visibleEntities = EntityHub.getInstance().getAllEntities();
        for (Entity entity: visibleEntities) {
            if (entity.getAllComponents().containsKey(CameraComponent.class)) {
                visibleEntities = EntityHub.getInstance().getEntitiesInsideViewport(entity.getComponent(CameraComponent.class));
                break;
            }
        }
        for (Entity entity : visibleEntities) {
            RectangularHitBoxComponent hitBox = entity.getComponent(RectangularHitBoxComponent.class);
            TriangularHitBoxComponent triBox = entity.getComponent(TriangularHitBoxComponent.class);
            if (entity.getComponent(VelocityComponent.class) == null) continue;
            VelocityComponent velocity = entity.getComponent(VelocityComponent.class);
            PositionComponent position = entity.getComponent(PositionComponent.class);
            if (position == null) continue;
            if (hitBox == null && triBox == null) continue;
            Shape futureHitBox = null;
            if (hitBox != null) {
                futureHitBox = new Rectangle(hitBox.getHitBox());
            }
            if (triBox != null) {
                futureHitBox = new Triangle(triBox.getHitBox());
            }
            if (futureHitBox.getClass() == Rectangle.class) {
                ((Rectangle) futureHitBox).moveTo(new Point(position.getGlobalX(), position.getGlobalY()));
                horizontalCollisionCheck(EntityHub.getInstance().getAllEntities(), entity, futureHitBox, velocity);
                ((Rectangle) futureHitBox).moveTo(new Point(position.getGlobalX(), position.getGlobalY()));
                verticalCollisionCheck(EntityHub.getInstance().getAllEntities(), entity, futureHitBox, velocity);
                ((Rectangle) futureHitBox).moveTo(new Point(position.getGlobalX(), position.getGlobalY()));
            }
            else if (futureHitBox.getClass() == Triangle.class) {
                ((Triangle) futureHitBox).moveTo(new Point(position.getGlobalX(), position.getGlobalY()));
                horizontalCollisionCheck(EntityHub.getInstance().getAllEntities(), entity, futureHitBox, velocity);
                ((Triangle) futureHitBox).moveTo(new Point(position.getGlobalX(), position.getGlobalY()));
                verticalCollisionCheck(EntityHub.getInstance().getAllEntities(), entity, futureHitBox, velocity);
                ((Triangle) futureHitBox).moveTo(new Point(position.getGlobalX(), position.getGlobalY()));
            }
        }
    }

    private static void verticalCollisionCheck(List<Entity> entities, Entity entity, Shape futureHitBox, VelocityComponent velocity) {
        if (futureHitBox.getClass() == Rectangle.class) {
            ((Rectangle)futureHitBox).translate(0, (int)velocity.getDy()); // Check vertical movement
        } else {
            ((Triangle)futureHitBox).translate(0, (int)velocity.getDy());
        }

        // Check vertical collisions
        for (Entity otherEntity : entities) {
            if (otherEntity == entity) continue;
            RectangularHitBoxComponent otherHitBox = otherEntity.getComponent(RectangularHitBoxComponent.class);
            if (otherHitBox == null) continue;

            if (Shape.intersect(futureHitBox, otherHitBox.getHitBox())) {
                velocity.setDy(0); // Stop vertical movement
                break;
            }
        }
    }

    private static void horizontalCollisionCheck(List<Entity> entities, Entity entity, Shape futureHitBox, VelocityComponent velocity) {
        if (futureHitBox.getClass() == Rectangle.class) {
            ((Rectangle)futureHitBox).translate((int)velocity.getDx(), 0); // Check vertical movement
        } else {
            ((Triangle)futureHitBox).translate((int)velocity.getDx(), 0);
        }
        // Check horizontal collisions
        for (Entity otherEntity : entities) {
            if (otherEntity == entity) continue;
            RectangularHitBoxComponent otherHitBox = otherEntity.getComponent(RectangularHitBoxComponent.class);
            if (otherHitBox == null) continue;

            if (Shape.intersect(futureHitBox, otherHitBox.getHitBox())) {
                velocity.setDx(0); // Stop horizontal movement
                break;
            }
        }
    }
}
