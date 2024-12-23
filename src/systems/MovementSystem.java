package systems;

import components.PositionComponent;
import components.VelocityComponent;
import core.Entity;
import core.System;

import java.util.List;

public class MovementSystem extends System {
    private List<Entity> entities;

    public MovementSystem() {
        ;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    @Override
    public void update(float deltaTime, List<Entity> entities) {
        setEntities(entities);

        for (Entity entity : entities) {
            if (entity.getComponent(PositionComponent.class) != null) {
                var velocity = entity.getComponent(VelocityComponent.class);
                var position = entity.getComponent(PositionComponent.class);
                position.setX(position.getX() + velocity.getDx());
                position.setY(position.getY() + velocity.getDy());
            } else {
                throw new Error("Trying to move entity without a position component.");
            }
        }
    }
}
