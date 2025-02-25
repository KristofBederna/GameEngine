package inf.elte.hu.gameengine_javafx.Entities.UIEntities;

import inf.elte.hu.gameengine_javafx.Components.UIComponents.SliderComponent;

public class SliderEntity extends UIEntity<SliderComponent> {
    public SliderEntity() {
        this.uiComponent = new SliderComponent(200, 200, 100, 100, 1, 100, 50);
        addComponent(uiComponent);

        addToManager();
        this.addToUI();
    }
}
