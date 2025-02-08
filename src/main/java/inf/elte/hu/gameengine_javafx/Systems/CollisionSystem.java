package inf.elte.hu.gameengine_javafx.Systems;


import inf.elte.hu.gameengine_javafx.Components.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.RectangularHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;

import java.awt.*;
import java.util.List;

public class CollisionSystem extends GameSystem {

    @Override
    public void update(float deltaTime, List<Entity> entities) {
        for (Entity entity : entities) {
            RectangularHitBoxComponent hitBox = entity.getComponent(RectangularHitBoxComponent.class);
            if (entity.getComponent(VelocityComponent.class) == null) continue;
            VelocityComponent velocity = entity.getComponent(VelocityComponent.class);
            PositionComponent position = entity.getComponent(PositionComponent.class);
            if (position == null || hitBox == null) continue;
            Rectangle futureHitBox = new Rectangle(hitBox.getHitBox());
            position.alignHitBox(futureHitBox);
            horizontalCollisionCheck(entities, entity, futureHitBox, velocity);
            position.alignHitBox(futureHitBox);
            verticalCollisionCheck(entities, entity, futureHitBox, velocity);
            position.alignHitBox(hitBox.getHitBox());
        }
    }

    private static void verticalCollisionCheck(List<Entity> entities, Entity entity, Rectangle futureHitBox, VelocityComponent velocity) {
        futureHitBox.translate(0, velocity.getDy()); // Check vertical movement

        // Check vertical collisions
        for (Entity otherEntity : entities) {
            if (otherEntity == entity) continue;
            RectangularHitBoxComponent otherHitBox = otherEntity.getComponent(RectangularHitBoxComponent.class);
            if (otherHitBox == null) continue;

            if (futureHitBox.intersects(otherHitBox.getHitBox())) {
                velocity.setDy(0); // Stop vertical movement
                break;
            }
        }
    }

    private static void horizontalCollisionCheck(List<Entity> entities, Entity entity, Rectangle futureHitBox, VelocityComponent velocity) {
        futureHitBox.translate(velocity.getDx(), 0);
        // Check horizontal collisions
        for (Entity otherEntity : entities) {
            if (otherEntity == entity) continue;
            RectangularHitBoxComponent otherHitBox = otherEntity.getComponent(RectangularHitBoxComponent.class);
            if (otherHitBox == null) continue;

            if (futureHitBox.intersects(otherHitBox.getHitBox())) {
                velocity.setDx(0); // Stop horizontal movement
                break;
            }
        }
    }
}
