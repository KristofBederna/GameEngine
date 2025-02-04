package inf.elte.hu.gameengine_javafx.Systems;


import inf.elte.hu.gameengine_javafx.Components.AnimationComponent;
import inf.elte.hu.gameengine_javafx.Components.ImageComponent;
import inf.elte.hu.gameengine_javafx.Components.PositionComponent;
import inf.elte.hu.gameengine_javafx.Core.Entity;
import inf.elte.hu.gameengine_javafx.Core.GameSystem;
import inf.elte.hu.gameengine_javafx.Entities.DummyEntity;

import java.util.List;

public class AnimationSystem extends GameSystem {
    @Override
    public void update(float deltaTime, List<Entity> entities) {
        for (Entity entity : entities) {
            PositionComponent position = entity.getComponent(PositionComponent.class);
            ImageComponent img = entity.getComponent(ImageComponent.class);
            if (entity.getClass() == DummyEntity.class) {
                ((DummyEntity) entity).setAnimationState();
            }
            AnimationComponent animation = entity.getComponent(AnimationComponent.class);

            if (position != null && img != null && animation != null) {
                img.setNextFrame(animation.getNextFrame());
            }
        }
    }

}
