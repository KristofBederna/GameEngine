package inf.elte.hu.gameengine_javafx.Systems;


import inf.elte.hu.gameengine_javafx.Components.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;

public class MovementSystem extends GameSystem {
    @Override
    public void update() {
        for (Entity entity : EntityHub.getInstance().getAllEntities()) {
            if (entity.getComponent(PositionComponent.class) != null && entity.getComponent(VelocityComponent.class) != null) {
                var velocity = entity.getComponent(VelocityComponent.class);
                var position = entity.getComponent(PositionComponent.class);

                position.setLocalX(position.getLocalX() + velocity.getDx(), entity);
                position.setLocalY(position.getLocalY() + velocity.getDy(), entity);

                position.updateGlobalPosition(entity);
            }
        }
    }
}
