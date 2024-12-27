package systems;

import components.AnimationComponent;
import components.ImageComponent;
import components.PositionComponent;
import core.Entity;
import core.System;
import entities.ImageEntity;

import java.util.List;

public class AnimationSystem extends System {
    @Override
    public void update(float deltaTime, List<Entity> entities) {
        for (Entity entity : entities) {
            PositionComponent position = entity.getComponent(PositionComponent.class);
            ImageComponent img = entity.getComponent(ImageComponent.class);
            if (entity.getClass() == ImageEntity.class) {
                ((ImageEntity) entity).setAnimationState();
            }
            AnimationComponent animation = entity.getComponent(AnimationComponent.class);

            if (position != null && img != null && animation != null) {
                img.setImage(animation.getNextFrame());
            }
        }
    }
}
