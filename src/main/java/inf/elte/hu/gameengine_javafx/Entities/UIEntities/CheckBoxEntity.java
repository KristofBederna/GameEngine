package inf.elte.hu.gameengine_javafx.Entities.UIEntities;

import inf.elte.hu.gameengine_javafx.Components.UIComponents.ButtonComponent;
import inf.elte.hu.gameengine_javafx.Components.UIComponents.CheckBoxComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.EntityManager;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import javafx.scene.layout.Pane;

public class CheckBoxEntity extends Entity {
    public CheckBoxEntity() {
        addComponent(new CheckBoxComponent("Hi", 300, 100));

        addToManager();
        addToUI(Globals.uiRoot);
    }

    @Override
    protected void addToManager() {
        EntityManager<CheckBoxEntity> manager = EntityHub.getInstance().getEntityManager((Class<CheckBoxEntity>)this.getClass());

        if (manager != null) {
            manager.register(this);
        } else {
            manager = new EntityManager<>();
            EntityHub.getInstance().addEntityManager(CheckBoxEntity.class, manager);
            manager.register(this);
        }
    }

    public void addToUI(Pane uiRoot) {
        uiRoot.getChildren().add(getComponent(CheckBoxComponent.class).getCheckBox());
    }

    public void removeFromUI(Pane uiRoot) {
        uiRoot.getChildren().remove(getComponent(CheckBoxComponent.class).getCheckBox());
    }
}
