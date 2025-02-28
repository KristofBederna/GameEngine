package inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems;

import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.ComplexHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.NSidedHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.RectangularHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.TriangularHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.DimensionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.*;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;

import java.util.List;

public class CollisionSystem extends GameSystem {

    @Override
    public void start() {
        this.active = true;
    }

    @Override
    public void update() {
        List<Entity> filteredEntities = EntityHub.getInstance().getEntitiesInsideViewport(CameraEntity.getInstance());

        for (Entity entity : filteredEntities) {

            RectangularHitBoxComponent hitBox = entity.getComponent(RectangularHitBoxComponent.class);
            TriangularHitBoxComponent triBox = entity.getComponent(TriangularHitBoxComponent.class);
            NSidedHitBoxComponent circBox = entity.getComponent(NSidedHitBoxComponent.class);
            ComplexHitBoxComponent complexBox = entity.getComponent(ComplexHitBoxComponent.class);
            if (entity.getComponent(VelocityComponent.class) == null) continue;
            VelocityComponent velocity = entity.getComponent(VelocityComponent.class);
            PositionComponent position = entity.getComponent(PositionComponent.class);
            DimensionComponent dimension = entity.getComponent(DimensionComponent.class);
            if (dimension == null) continue;
            if (position == null) continue;
            if (hitBox == null && triBox == null && circBox == null && complexBox == null) continue;

            Shape futureHitBox = null;
            if (hitBox != null) {
                futureHitBox = new Rectangle(hitBox.getHitBox());
                ((Rectangle)futureHitBox).moveTo(new Point(position.getGlobalX(), position.getGlobalY()));
            }
            if (triBox != null) {
                futureHitBox = new Triangle(triBox.getHitBox());
                ((Triangle)futureHitBox).moveTo(new Point(position.getGlobalX(), position.getGlobalY()));
            }
            if (circBox != null) {
                futureHitBox = new NSidedShape(circBox.getHitBox());
                ((NSidedShape)futureHitBox).moveTo(new Point(position.getGlobalX(), position.getGlobalY()));
            }
            if (complexBox != null) {
                futureHitBox = new ComplexShape(complexBox.getHitBox());
                ((ComplexShape)futureHitBox).moveTo(new Point(position.getGlobalX(), position.getGlobalY()));
            }

            moveDiagonally(EntityHub.getInstance().getAllEntities(), entity, futureHitBox, velocity);
        }
    }

    private static void moveDiagonally(List<Entity> entities, Entity entity, Shape futureHitBox, VelocityComponent velocity) {
        checkCollisionAndMove(entities, entity, futureHitBox, velocity);
        if (velocity.getDy() != 0) {
            verticalCollisionCheck(entities, entity, futureHitBox, velocity);
        }
        if (velocity.getDx() != 0) {
            horizontalCollisionCheck(entities, entity, futureHitBox, velocity);
        }
    }

    private static void checkCollisionAndMove(List<Entity> entities, Entity entity, Shape futureHitBox, VelocityComponent velocity) {
        horizontalCollisionCheck(entities, entity, futureHitBox, velocity);
        verticalCollisionCheck(entities, entity, futureHitBox, velocity);
    }

    private static void horizontalCollisionCheck(List<Entity> entities, Entity entity, Shape futureHitBox, VelocityComponent velocity) {
        translateHitBoxHorizontally(futureHitBox, velocity);

        for (Entity otherEntity : entities) {
            if (otherEntity == entity) continue;

            Shape otherHitBox = getHitBoxOfEntity(otherEntity);
            if (otherHitBox != null && Shape.intersect(futureHitBox, otherHitBox)) {
                velocity.setDx(0);
                break;
            }
        }
    }

    private static void verticalCollisionCheck(List<Entity> entities, Entity entity, Shape futureHitBox, VelocityComponent velocity) {
        translateHitBoxVertically(futureHitBox, velocity);

        for (Entity otherEntity : entities) {
            if (otherEntity == entity) continue;

            Shape otherHitBox = getHitBoxOfEntity(otherEntity);
            if (otherHitBox != null && Shape.intersect(futureHitBox, otherHitBox)) {
                velocity.setDy(0);
                break;
            }
        }
    }

    private static void translateHitBoxHorizontally(Shape futureHitBox, VelocityComponent velocity) {
        if (futureHitBox instanceof Rectangle) {
            ((Rectangle) futureHitBox).translate((int) velocity.getDx(), 0);
        } else if (futureHitBox instanceof Triangle) {
            ((Triangle) futureHitBox).translate((int) velocity.getDx(), 0);
        } else if (futureHitBox instanceof NSidedShape) {
            ((NSidedShape) futureHitBox).translate((int) velocity.getDx(), 0);
        } else if (futureHitBox instanceof ComplexShape) {
            ((ComplexShape) futureHitBox).translate((int) velocity.getDx(), 0);
        }
    }

    private static void translateHitBoxVertically(Shape futureHitBox, VelocityComponent velocity) {
        if (futureHitBox instanceof Rectangle) {
            ((Rectangle) futureHitBox).translate(0, (int) velocity.getDy());
        } else if (futureHitBox instanceof Triangle) {
            ((Triangle) futureHitBox).translate(0, (int) velocity.getDy());
        } else if (futureHitBox instanceof NSidedShape) {
            ((NSidedShape) futureHitBox).translate(0, (int) velocity.getDy());
        } else if (futureHitBox instanceof ComplexShape) {
            ((ComplexShape) futureHitBox).translate(0, (int) velocity.getDy());
        }
    }

    private static Shape getHitBoxOfEntity(Entity entity) {
        RectangularHitBoxComponent rectHitBox = entity.getComponent(RectangularHitBoxComponent.class);
        TriangularHitBoxComponent triHitBox = entity.getComponent(TriangularHitBoxComponent.class);
        NSidedHitBoxComponent circHitBox = entity.getComponent(NSidedHitBoxComponent.class);
        ComplexHitBoxComponent complexBox = entity.getComponent(ComplexHitBoxComponent.class);

        if (rectHitBox != null) return rectHitBox.getHitBox();
        if (triHitBox != null) return triHitBox.getHitBox();
        if (circHitBox != null) return circHitBox.getHitBox();
        if (complexBox != null) return complexBox.getHitBox();

        return null;
    }
}
