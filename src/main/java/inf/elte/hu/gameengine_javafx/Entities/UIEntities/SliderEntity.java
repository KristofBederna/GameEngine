package inf.elte.hu.gameengine_javafx.Entities.UIEntities;

import inf.elte.hu.gameengine_javafx.Components.UIComponents.SliderComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.EntityManager;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import javafx.scene.layout.Pane;

public class SliderEntity extends Entity {
    public SliderEntity() {
        addComponent(new SliderComponent(200, 200, 100, 100, 1, 100, 50));

        addToManager();
        addToUI(Globals.uiRoot);
    }

    @Override
    protected void addToManager() {
        EntityManager<SliderEntity> manager = EntityHub.getInstance().getEntityManager((Class<SliderEntity>)this.getClass());

        if (manager != null) {
            manager.register(this);
        } else {
            manager = new EntityManager<>();
            EntityHub.getInstance().addEntityManager(SliderEntity.class, manager);
            manager.register(this);
        }
    }

    public void addToUI(Pane uiRoot) {
        uiRoot.getChildren().add(getComponent(SliderComponent.class).getSlider());
    }

    public void removeFromUI(Pane uiRoot) {
        uiRoot.getChildren().remove(getComponent(SliderComponent.class).getSlider());
    }
}
