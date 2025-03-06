package inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems;


import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.ComplexHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.NSidedHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.RectangularHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.TriangularHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.LightComponent;
import inf.elte.hu.gameengine_javafx.Components.ParentComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.CentralMassComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.DimensionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.VelocityComponent;
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
        var entitiesSnapshot = new ArrayList<>(EntityHub.getInstance().getAllEntities());
        if (entitiesSnapshot.isEmpty()) {
            return;
        }
        for (Entity entity : entitiesSnapshot) {
            if (entity == null) continue;
            if (entity.getComponent(PositionComponent.class) != null && entity.getComponent(VelocityComponent.class) != null) {
                var velocity = entity.getComponent(VelocityComponent.class);
                var position = entity.getComponent(PositionComponent.class);

                position.setLocalX(position.getLocalX() + velocity.getVelocity().getDx(), entity);
                position.setLocalY(position.getLocalY() + velocity.getVelocity().getDy(), entity);

                if (entity.getComponent(DimensionComponent.class) != null) {
                    var dimension = entity.getComponent(DimensionComponent.class);
                    if (entity.getComponent(CentralMassComponent.class) != null) {
                        entity.getComponent(CentralMassComponent.class).setCentralX(position.getGlobalX() + dimension.getWidth()/2);
                        entity.getComponent(CentralMassComponent.class).setCentralY(position.getGlobalY() + dimension.getHeight()/2);
                    }
                }

                position.updateGlobalPosition(entity);
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
