package inf.elte.hu.gameengine_javafx.Systems;


import inf.elte.hu.gameengine_javafx.Components.AnimationComponent;
import inf.elte.hu.gameengine_javafx.Components.ImageComponent;
import inf.elte.hu.gameengine_javafx.Components.PositionComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Entities.DummyEntity;

import java.util.ArrayList;

public class AnimationSystem extends GameSystem {
    @Override
    public void update() {
        var entitiesSnapshot = new ArrayList<>(EntityHub.getInstance().getAllEntities());
        if (entitiesSnapshot.isEmpty()) {
            return;
        }
        for (Entity entity : entitiesSnapshot) {
            if (entity == null) continue;
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
