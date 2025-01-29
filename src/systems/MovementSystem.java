package systems;

import components.PositionComponent;
import components.VelocityComponent;
import core.Entity;
import core.GameSystem;
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
