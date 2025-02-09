package inf.elte.hu.gameengine_javafx.Systems;


import inf.elte.hu.gameengine_javafx.Components.AnimationComponent;
import inf.elte.hu.gameengine_javafx.Components.ImageComponent;
import inf.elte.hu.gameengine_javafx.Components.PositionComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Entities.DummyEntity;

public class AnimationSystem extends GameSystem {
    @Override
    public void update(float deltaTime) {
        for (Entity entity : EntityHub.getInstance().getAllEntities()) {
            PositionComponent position = entity.getComponent(PositionComponent.class);
            ImageComponent img = entity.getComponent(ImageComponent.class);
            if (entity.getClass() == DummyEntity.class) {
                ((DummyEntity) entity).setAnimationState();
            }
            AnimationComponent animation = entity.getComponent(AnimationComponent.class);

            if (position != null && img != null && animation != null) {
                img.setNextFrame(animation.getNextFrame());
                EntityHub.getInstance().getEntityManager(entity.getClass()).updateLastUsed(entity.getId());
            }
        }
    }

}
