package inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems;


import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.ComplexHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.NSidedHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.RectangularHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.TriangularHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.LightComponent;
import inf.elte.hu.gameengine_javafx.Components.ParentComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.*;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Entities.LightingEntity;
import inf.elte.hu.gameengine_javafx.Entities.ParticleEntity;
import inf.elte.hu.gameengine_javafx.Misc.Time;

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
        if (entitiesSnapshot.isEmpty()) {
            return;
        }
        for (Entity entity : entitiesSnapshot) {
            if (entity == null) continue;
            if (entity.getComponent(PositionComponent.class) != null && entity.getComponent(VelocityComponent.class) != null) {
                var velocity = entity.getComponent(VelocityComponent.class);
                var position = entity.getComponent(PositionComponent.class);

                if (entity.getComponent(AccelerationComponent.class) != null) {
                    var acceleration = entity.getComponent(AccelerationComponent.class);
                    double newDx = velocity.getVelocity().getDx() + acceleration.getAcceleration().getDx();
                    double newDy = velocity.getVelocity().getDy() + acceleration.getAcceleration().getDy();

                    if (Math.signum(newDx) != Math.signum(velocity.getVelocity().getDx()) && Math.abs(newDx) < Math.abs(acceleration.getAcceleration().getDx())) {
                        newDx = 0;
                        acceleration.getAcceleration().setDx(newDx);
                    }
                    if (Math.signum(newDy) != Math.signum(velocity.getVelocity().getDy()) && Math.abs(newDy) < Math.abs(acceleration.getAcceleration().getDy())) {
                        newDy = 0;
                        acceleration.getAcceleration().setDy(newDy);
                    }

                    velocity.setVelocity(newDx, newDy);
                }

                position.setLocalPosition(position.getLocalX() + velocity.getVelocity().getDx(), position.getLocalY() + velocity.getVelocity().getDy(), entity);

                if (entity.getComponent(DimensionComponent.class) != null) {
                    var dimension = entity.getComponent(DimensionComponent.class);
                    if (entity.getComponent(CentralMassComponent.class) != null) {
                        entity.getComponent(CentralMassComponent.class).setCentralX(position.getGlobalX() + dimension.getWidth()/2);
                        entity.getComponent(CentralMassComponent.class).setCentralY(position.getGlobalY() + dimension.getHeight()/2);
                    }
                }
                position.updateGlobalPosition(entity);
                if (entity instanceof ParticleEntity) {
                    System.out.println(position.getGlobalX() + " " + position.getGlobalY());
                }
                RectangularHitBoxComponent hitBox = entity.getComponent(RectangularHitBoxComponent.class);
                TriangularHitBoxComponent triBox = entity.getComponent(TriangularHitBoxComponent.class);
                NSidedHitBoxComponent circBox = entity.getComponent(NSidedHitBoxComponent.class);
                ComplexHitBoxComponent complexBox = entity.getComponent(ComplexHitBoxComponent.class);
                if (hitBox != null) {
                    hitBox.getHitBox().translate(velocity.getVelocity().getDx(), velocity.getVelocity().getDy());
                }
                if (triBox != null) {
                    triBox.getHitBox().translate(velocity.getVelocity().getDx(), velocity.getVelocity().getDy());
                }
                if (circBox != null) {
                    circBox.getHitBox().translate(velocity.getVelocity().getDx(), velocity.getVelocity().getDy());
                }
                if (complexBox != null)
                {
                    complexBox.getHitBox().translate(velocity.getVelocity().getDx(), velocity.getVelocity().getDy());
                }
            }
        }
    }
}
