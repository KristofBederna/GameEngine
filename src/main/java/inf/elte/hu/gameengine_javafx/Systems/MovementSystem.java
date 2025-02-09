package inf.elte.hu.gameengine_javafx.Systems;


import inf.elte.hu.gameengine_javafx.Components.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;

public class MovementSystem extends GameSystem {
    @Override
    public void update(float deltaTime) {
        for (Entity entity : EntityHub.getInstance().getAllEntities()) {
            if (entity.getComponent(PositionComponent.class) != null && entity.getComponent(VelocityComponent.class) != null) {
                var velocity = entity.getComponent(VelocityComponent.class);
                var position = entity.getComponent(PositionComponent.class);
                position.setX(position.getX() + velocity.getDx());
                position.setY(position.getY() + velocity.getDy());
            }
        }
    }
}
