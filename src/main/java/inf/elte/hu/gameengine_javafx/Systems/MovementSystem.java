package inf.elte.hu.gameengine_javafx.Systems;


import inf.elte.hu.gameengine_javafx.Components.*;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;

import java.util.ArrayList;

public class MovementSystem extends GameSystem {
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

                position.setLocalX(position.getLocalX() + velocity.getDx(), entity);
                position.setLocalY(position.getLocalY() + velocity.getDy(), entity);

                position.updateGlobalPosition(entity);
                RectangularHitBoxComponent hitBox = entity.getComponent(RectangularHitBoxComponent.class);
                TriangularHitBoxComponent triBox = entity.getComponent(TriangularHitBoxComponent.class);
                NSidedHitBoxComponent circBox = entity.getComponent(NSidedHitBoxComponent.class);
                if (hitBox != null) {
                    hitBox.getHitBox().moveTo(new Point(position.getGlobalX(), position.getGlobalY()));
                }
                if (triBox != null) {
                    triBox.getHitBox().moveTo(new Point(position.getGlobalX(), position.getGlobalY()));
                }
                if (circBox != null) {
                    circBox.getHitBox().moveTo(new Point(position.getGlobalX()+entity.getComponent(DimensionComponent.class).getWidth()/2, position.getGlobalY()+entity.getComponent(DimensionComponent.class).getHeight()/2));
                }
            }
        }
    }
}
