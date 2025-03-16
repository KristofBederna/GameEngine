package inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems;

import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.HitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.PhysicsComponents.AccelerationComponent;
import inf.elte.hu.gameengine_javafx.Components.Default.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.PhysicsComponents.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.ComplexShape;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Shape;

import java.util.List;

public class CollisionSystem extends GameSystem {

    @Override
    public void start() {
        this.active = true;
    }

    @Override
    public void update() {
        List<Entity> filteredEntities = getEntities();
        List<Entity> hitBoxes = EntityHub.getInstance().getEntitiesWithComponent(HitBoxComponent.class);

        if (filteredEntities == null || filteredEntities.isEmpty()) {
            return;
        }

        processEntities(filteredEntities, hitBoxes);
    }

    private static void processEntities(List<Entity> filteredEntities, List<Entity> hitBoxes) {
        synchronized (filteredEntities) {
            for (Entity entity : filteredEntities) {
                processEntity(hitBoxes, entity);
            }
        }
    }

    private static void processEntity(List<Entity> hitBoxes, Entity entity) {
        HitBoxComponent hitBox = entity.getComponent(HitBoxComponent.class);
        VelocityComponent velocity = entity.getComponent(VelocityComponent.class);
        PositionComponent position = entity.getComponent(PositionComponent.class);

        ComplexShape futureHitBox = null;
        if (hitBox != null) {
            futureHitBox = new ComplexShape(hitBox.getHitBox());
            futureHitBox.moveTo(new Point(position.getGlobalX(), position.getGlobalY()));
        }

        moveDiagonally(hitBoxes, entity, futureHitBox, velocity);
    }

    private static List<Entity> getEntities() {
        List<Entity> filteredEntities = EntityHub.getInstance().getEntitiesInsideViewport(CameraEntity.getInstance());
        filteredEntities.retainAll(EntityHub.getInstance().getEntitiesWithComponent(HitBoxComponent.class));
        filteredEntities.retainAll(EntityHub.getInstance().getEntitiesWithComponent(VelocityComponent.class));
        filteredEntities.retainAll(EntityHub.getInstance().getEntitiesWithComponent(PositionComponent.class));
        return filteredEntities;
    }

    private static void moveDiagonally(List<Entity> entities, Entity entity, Shape futureHitBox, VelocityComponent velocity) {
        checkCollisionAndMove(entities, entity, futureHitBox, velocity);
        if (velocity.getVelocity().getDy() != 0) {
            verticalCollisionCheck(entities, entity, futureHitBox, velocity);
        }
        if (velocity.getVelocity().getDx() != 0) {
            horizontalCollisionCheck(entities, entity, futureHitBox, velocity);
        }
    }

    private static void checkCollisionAndMove(List<Entity> entities, Entity entity, Shape futureHitBox, VelocityComponent velocity) {
        horizontalCollisionCheck(entities, entity, futureHitBox, velocity);
        verticalCollisionCheck(entities, entity, futureHitBox, velocity);
    }

    private static void horizontalCollisionCheck(List<Entity> entities, Entity entity, Shape futureHitBox, VelocityComponent velocity) {
        translateHitBoxHorizontally(entity, futureHitBox, velocity);

        synchronized (entities) {
            for (Entity otherEntity : entities) {
                if (otherEntity == entity) continue;

                Shape otherHitBox = otherEntity.getComponent(HitBoxComponent.class).getHitBox();
                if (otherHitBox != null && Shape.intersect(futureHitBox, otherHitBox)) {
                    velocity.setVelocity(0, velocity.getVelocity().getDy());
                    if (entity.getComponent(AccelerationComponent.class) != null) {
                        entity.getComponent(AccelerationComponent.class).getAcceleration().setDx(0);
                    }
                    break;
                }
            }
        }
    }

    private static void verticalCollisionCheck(List<Entity> entities, Entity entity, Shape futureHitBox, VelocityComponent velocity) {
        translateHitBoxVertically(entity, futureHitBox, velocity);

        synchronized (entities) {
            for (Entity otherEntity : entities) {
                if (otherEntity == entity) continue;

                Shape otherHitBox = otherEntity.getComponent(HitBoxComponent.class).getHitBox();
                if (otherHitBox != null && Shape.intersect(futureHitBox, otherHitBox)) {
                    velocity.setVelocity(velocity.getVelocity().getDx(), 0);
                    if (entity.getComponent(AccelerationComponent.class) != null) {
                        entity.getComponent(AccelerationComponent.class).getAcceleration().setDy(0);
                    }
                    break;
                }
            }
        }
    }

    private static void translateHitBoxHorizontally(Entity entity, Shape futureHitBox, VelocityComponent velocity) {
        double dx = velocity.getVelocity().getDx();

        AccelerationComponent accelerationComponent = entity.getComponent(AccelerationComponent.class);
        if (accelerationComponent != null) {
            dx += accelerationComponent.getAcceleration().getDx();
        }
        futureHitBox.translate(dx, 0);
    }

    private static void translateHitBoxVertically(Entity entity, Shape futureHitBox, VelocityComponent velocity) {
        double dy = velocity.getVelocity().getDy();

        AccelerationComponent accelerationComponent = entity.getComponent(AccelerationComponent.class);
        if (accelerationComponent != null) {
            dy += accelerationComponent.getAcceleration().getDy();
        }
        futureHitBox.translate(0, dy);
    }
}
