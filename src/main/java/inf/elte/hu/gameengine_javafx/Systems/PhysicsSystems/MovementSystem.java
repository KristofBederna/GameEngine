package inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems;

import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.*;
import inf.elte.hu.gameengine_javafx.Components.PhysicsComponents.AccelerationComponent;
import inf.elte.hu.gameengine_javafx.Components.PhysicsComponents.DragComponent;
import inf.elte.hu.gameengine_javafx.Components.PhysicsComponents.FrictionComponent;
import inf.elte.hu.gameengine_javafx.Components.PhysicsComponents.MassComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.*;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.WorldDataComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import inf.elte.hu.gameengine_javafx.Entities.TileEntity;
import inf.elte.hu.gameengine_javafx.Entities.WorldEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Rectangle;
import inf.elte.hu.gameengine_javafx.Misc.Config;
import inf.elte.hu.gameengine_javafx.Misc.Layers.GameCanvas;
import inf.elte.hu.gameengine_javafx.Misc.Time;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class MovementSystem extends GameSystem {
    @Override
    public void start() {
        this.active = true;
    }

    @Override
    public void update() {
        var entitiesSnapshot = new ArrayList<>(EntityHub.getInstance().getEntitiesWithComponent(VelocityComponent.class));
        entitiesSnapshot.retainAll(EntityHub.getInstance().getEntitiesWithComponent(PositionComponent.class));
        entitiesSnapshot.retainAll(EntityHub.getInstance().getEntitiesInsideViewport(CameraEntity.getInstance()));
        if (entitiesSnapshot.isEmpty()) {
            return;
        }

        for (Entity entity : entitiesSnapshot) {
            if (entity == null) continue;

            var velocity = entity.getComponent(VelocityComponent.class);
            var position = entity.getComponent(PositionComponent.class);
            var acceleration = entity.getComponent(AccelerationComponent.class);
            var massComponent = entity.getComponent(MassComponent.class);
            var dragComponent = entity.getComponent(DragComponent.class);

            double mass = (massComponent != null) ? massComponent.getMass() : 1.0;
            double drag = (dragComponent != null) ? dragComponent.getDrag() : Config.drag;
            double dragFactor = Math.pow(1 - drag, Time.getInstance().getDeltaTime());
            double maxSpeed = velocity.getMaxVelocity();

            double newDx = velocity.getVelocity().getDx();
            double newDy = velocity.getVelocity().getDy();

            if (acceleration != null) {
                newDx += acceleration.getAcceleration().getDx() / mass;
                newDy += acceleration.getAcceleration().getDy() / mass;
            }
            TileEntity tile = WorldEntity.getInstance().getComponent(WorldDataComponent.class).getElement(entity.getComponent(PositionComponent.class).getGlobal());

            if (entity.getComponent(CentralMassComponent.class) != null) {
                tile = WorldEntity.getInstance().getComponent(WorldDataComponent.class).getElement(entity.getComponent(CentralMassComponent.class).getCentral());
            }

            if (tile.getComponent(FrictionComponent.class) != null) {
                FrictionComponent frictionComponent = tile.getComponent(FrictionComponent.class);
                double friction = (frictionComponent != null) ? frictionComponent.getFriction() : Config.friction;
                double frictionForce = friction * Time.getInstance().getDeltaTime();

                if (Math.abs(newDx) > frictionForce) {
                    newDx -= Math.signum(newDx) * frictionForce;
                } else {
                    newDx = 0;
                }

                if (Math.abs(newDy) > frictionForce) {
                    newDy -= Math.signum(newDy) * frictionForce;
                } else {
                    newDy = 0;
                }
            }

            newDx = Math.max(-maxSpeed, Math.min(maxSpeed, newDx));
            newDy = Math.max(-maxSpeed, Math.min(maxSpeed, newDy));

            if (acceleration == null || (acceleration.getAcceleration().getDx() == 0)) {
                newDx *= dragFactor;
            }
            if (acceleration == null || (acceleration.getAcceleration().getDy() == 0)) {
                newDy *= dragFactor;
            }

            velocity.setVelocity(newDx, newDy);

            position.setLocalPosition(
                    position.getLocalX() + velocity.getVelocity().getDx(),
                    position.getLocalY() + velocity.getVelocity().getDy(),
                    entity
            );

            var dimension = entity.getComponent(DimensionComponent.class);
            var centralMass = entity.getComponent(CentralMassComponent.class);
            if (dimension != null && centralMass != null) {
                centralMass.setCentralX(position.getGlobalX() + dimension.getWidth() / 2);
                centralMass.setCentralY(position.getGlobalY() + dimension.getHeight() / 2);
            }

            position.updateGlobalPosition(entity);

            updateHitboxes(entity, velocity);
        }
    }

    private void updateHitboxes(Entity entity, VelocityComponent velocity) {
        double dx = velocity.getVelocity().getDx();
        double dy = velocity.getVelocity().getDy();

        if (entity.getComponent(RectangularHitBoxComponent.class) != null) {
            entity.getComponent(RectangularHitBoxComponent.class).getHitBox().translate(dx, dy);
        }
        if (entity.getComponent(TriangularHitBoxComponent.class) != null) {
            entity.getComponent(TriangularHitBoxComponent.class).getHitBox().translate(dx, dy);
        }
        if (entity.getComponent(NSidedHitBoxComponent.class) != null) {
            entity.getComponent(NSidedHitBoxComponent.class).getHitBox().translate(dx, dy);
        }
        if (entity.getComponent(ComplexHitBoxComponent.class) != null) {
            entity.getComponent(ComplexHitBoxComponent.class).getHitBox().translate(dx, dy);
        }
    }
}
