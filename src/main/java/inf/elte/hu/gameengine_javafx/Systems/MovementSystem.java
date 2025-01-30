package inf.elte.hu.gameengine_javafx.Systems;


import inf.elte.hu.gameengine_javafx.Components.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Core.Entity;
import inf.elte.hu.gameengine_javafx.Core.GameSystem;

import java.util.List;

public class MovementSystem extends GameSystem {
    @Override
    public void update(float deltaTime, List<Entity> entities) {
        for (Entity entity : entities) {
            if (entity.getComponent(PositionComponent.class) != null && entity.getComponent(VelocityComponent.class) != null) {
                var velocity = entity.getComponent(VelocityComponent.class);
                var position = entity.getComponent(PositionComponent.class);
                position.setX(position.getX() + velocity.getDx());
                position.setY(position.getY() + velocity.getDy());
            }
        }
    }
}
