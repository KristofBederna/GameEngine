package inf.elte.hu.gameengine_javafx.Entities.UIEntities;

import inf.elte.hu.gameengine_javafx.Components.UIComponents.ProgressBarComponent;

public class ProgressBarEntity extends UIEntity<ProgressBarComponent> {
    public ProgressBarEntity() {
        this.uiComponent = new ProgressBarComponent(500, 400, 100, 20, 10);
        addComponent(uiComponent);

        addToManager();
        this.addToUI();
    }
}
