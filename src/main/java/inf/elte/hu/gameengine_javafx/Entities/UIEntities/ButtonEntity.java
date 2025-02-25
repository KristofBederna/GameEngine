package inf.elte.hu.gameengine_javafx.Entities.UIEntities;

import inf.elte.hu.gameengine_javafx.Components.UIComponents.ButtonComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.EntityManager;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import javafx.scene.layout.Pane;

public class ButtonEntity extends Entity {
    public ButtonEntity() {
        addComponent(new ButtonComponent("Button", 100, 100, 100, 100, () -> {
            System.out.println("ButtonEntity clicked");
        }));

        addToManager();
        addToUI(Globals.uiRoot);
    }

    @Override
    protected void addToManager() {
        EntityManager<ButtonEntity> manager = EntityHub.getInstance().getEntityManager((Class<ButtonEntity>)this.getClass());

        if (manager != null) {
            manager.register(this);
        } else {
            manager = new EntityManager<>();
            EntityHub.getInstance().addEntityManager(ButtonEntity.class, manager);
            manager.register(this);
        }
    }

    public void addToUI(Pane uiRoot) {
        uiRoot.getChildren().add(getComponent(ButtonComponent.class).getButton());
    }

    public void removeFromUI(Pane uiRoot) {
        uiRoot.getChildren().remove(getComponent(ButtonComponent.class).getButton());
    }
}
