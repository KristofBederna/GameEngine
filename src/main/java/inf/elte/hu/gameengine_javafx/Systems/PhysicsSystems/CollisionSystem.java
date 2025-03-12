package inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems;

import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.ComplexHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.NSidedHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.RectangularHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.TriangularHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.PhysicsComponents.AccelerationComponent;
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

        if (filteredEntities == null || filteredEntities.isEmpty()) {
            return;
        }

        synchronized (filteredEntities) {
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

                Shape otherHitBox = getHitBoxOfEntity(otherEntity);
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

                Shape otherHitBox = getHitBoxOfEntity(otherEntity);
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

        if (futureHitBox instanceof Rectangle) {
            ((Rectangle) futureHitBox).translate(dx, 0);
        } else if (futureHitBox instanceof Triangle) {
            ((Triangle) futureHitBox).translate(dx, 0);
        } else if (futureHitBox instanceof NSidedShape) {
            ((NSidedShape) futureHitBox).translate(dx, 0);
        } else if (futureHitBox instanceof ComplexShape) {
            ((ComplexShape) futureHitBox).translate(dx, 0);
        }
    }

    private static void translateHitBoxVertically(Entity entity, Shape futureHitBox, VelocityComponent velocity) {
        double dy = velocity.getVelocity().getDy();

        AccelerationComponent accelerationComponent = entity.getComponent(AccelerationComponent.class);
        if (accelerationComponent != null) {
            dy += accelerationComponent.getAcceleration().getDy();
        }

        if (futureHitBox instanceof Rectangle) {
            ((Rectangle) futureHitBox).translate(0, dy);
        } else if (futureHitBox instanceof Triangle) {
            ((Triangle) futureHitBox).translate(0, dy);
        } else if (futureHitBox instanceof NSidedShape) {
            ((NSidedShape) futureHitBox).translate(0, dy);
        } else if (futureHitBox instanceof ComplexShape) {
            ((ComplexShape) futureHitBox).translate(0, dy);
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
