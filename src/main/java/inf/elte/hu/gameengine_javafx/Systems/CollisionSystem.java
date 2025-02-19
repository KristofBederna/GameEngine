package inf.elte.hu.gameengine_javafx.Systems;


import inf.elte.hu.gameengine_javafx.Components.*;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.*;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;

import java.util.List;

public class CollisionSystem extends GameSystem {

    @Override
    public void update() {
        List<Entity> filteredEntities = EntityHub.getInstance().getEntitiesInsideViewport(CameraEntity.getInstance());

        for (Entity entity : filteredEntities) {

            RectangularHitBoxComponent hitBox = entity.getComponent(RectangularHitBoxComponent.class);
            TriangularHitBoxComponent triBox = entity.getComponent(TriangularHitBoxComponent.class);
            NSidedHitBoxComponent circBox = entity.getComponent(NSidedHitBoxComponent.class);
            if (entity.getComponent(VelocityComponent.class) == null) continue;
            VelocityComponent velocity = entity.getComponent(VelocityComponent.class);
            PositionComponent position = entity.getComponent(PositionComponent.class);
            DimensionComponent dimension = entity.getComponent(DimensionComponent.class);
            if (dimension == null) continue;
            if (position == null) continue;
            if (hitBox == null && triBox == null && circBox == null) continue;
            Shape futureHitBox = null;
            if (hitBox != null) {
                futureHitBox = new Rectangle(hitBox.getHitBox());
            }
            if (triBox != null) {
                futureHitBox = new Triangle(triBox.getHitBox());
            }
            if (circBox != null) {
                futureHitBox = new NSidedShape(circBox.getHitBox());
            }
            if (futureHitBox.getClass() == Rectangle.class) {
                ((Rectangle) futureHitBox).moveTo(new Point(position.getGlobalX(), position.getGlobalY()));
                horizontalCollisionCheck(EntityHub.getInstance().getAllEntities(), entity, futureHitBox, velocity);
                ((Rectangle) futureHitBox).moveTo(new Point(position.getGlobalX(), position.getGlobalY()));
                verticalCollisionCheck(EntityHub.getInstance().getAllEntities(), entity, futureHitBox, velocity);
                ((Rectangle) futureHitBox).moveTo(new Point(position.getGlobalX(), position.getGlobalY()));
            } else if (futureHitBox.getClass() == Triangle.class) {
                ((Triangle) futureHitBox).moveTo(new Point(position.getGlobalX(), position.getGlobalY()));
                horizontalCollisionCheck(EntityHub.getInstance().getAllEntities(), entity, futureHitBox, velocity);
                ((Triangle) futureHitBox).moveTo(new Point(position.getGlobalX(), position.getGlobalY()));
                verticalCollisionCheck(EntityHub.getInstance().getAllEntities(), entity, futureHitBox, velocity);
                ((Triangle) futureHitBox).moveTo(new Point(position.getGlobalX(), position.getGlobalY()));
            } else {
                ((NSidedShape) futureHitBox).moveTo(new Point(position.getGlobalX()+dimension.getWidth()/2, position.getGlobalY()+dimension.getHeight()/2));
                horizontalCollisionCheck(EntityHub.getInstance().getAllEntities(), entity, futureHitBox, velocity);
                ((NSidedShape) futureHitBox).moveTo(new Point(position.getGlobalX()+dimension.getWidth()/2, position.getGlobalY()+dimension.getHeight()/2));
                verticalCollisionCheck(EntityHub.getInstance().getAllEntities(), entity, futureHitBox, velocity);
                ((NSidedShape) futureHitBox).moveTo(new Point(position.getGlobalX()+dimension.getWidth()/2, position.getGlobalY()+dimension.getHeight()/2));
            }
        }
    }

    private static void verticalCollisionCheck(List<Entity> entities, Entity entity, Shape futureHitBox, VelocityComponent velocity) {
        if (futureHitBox instanceof Rectangle) {
            ((Rectangle) futureHitBox).translate(0, (int) velocity.getDy());
        } else if (futureHitBox instanceof Triangle) {
            ((Triangle) futureHitBox).translate(0, (int) velocity.getDy());
        } else if (futureHitBox instanceof NSidedShape) {
            ((NSidedShape) futureHitBox).translate(0, (int) velocity.getDy());
        }

        // Check against all shape types
        for (Entity otherEntity : entities) {
            if (otherEntity == entity) continue;

            Shape otherHitBox = getHitBoxOfEntity(otherEntity);
            if (otherHitBox == null) continue;

            if (Shape.intersect(futureHitBox, otherHitBox)) {
                velocity.setDy(0); // Stop vertical movement
                break;
            }
        }
    }

    private static void horizontalCollisionCheck(List<Entity> entities, Entity entity, Shape futureHitBox, VelocityComponent velocity) {
        if (futureHitBox instanceof Rectangle) {
            ((Rectangle) futureHitBox).translate((int) velocity.getDx(), 0);
        } else if (futureHitBox instanceof Triangle) {
            ((Triangle) futureHitBox).translate((int) velocity.getDx(), 0);
        } else if (futureHitBox instanceof NSidedShape) {
            ((NSidedShape) futureHitBox).translate((int) velocity.getDx(), 0);
        }

        // Check against all shape types, not just rectangles!
        for (Entity otherEntity : entities) {
            if (otherEntity == entity) continue;

            Shape otherHitBox = getHitBoxOfEntity(otherEntity);
            if (otherHitBox == null) continue;

            if (Shape.intersect(futureHitBox, otherHitBox)) {
                velocity.setDx(0); // Stop horizontal movement
                break;
            }
        }
    }

    private static Shape getHitBoxOfEntity(Entity entity) {
        RectangularHitBoxComponent rectHitBox = entity.getComponent(RectangularHitBoxComponent.class);
        TriangularHitBoxComponent triHitBox = entity.getComponent(TriangularHitBoxComponent.class);
        NSidedHitBoxComponent circHitBox = entity.getComponent(NSidedHitBoxComponent.class);

        if (rectHitBox != null) return rectHitBox.getHitBox();
        if (triHitBox != null) return triHitBox.getHitBox();
        if (circHitBox != null) return circHitBox.getHitBox();

        return null;
    }

}
