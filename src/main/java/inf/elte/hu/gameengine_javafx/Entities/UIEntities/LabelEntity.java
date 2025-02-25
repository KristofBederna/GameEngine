package inf.elte.hu.gameengine_javafx.Entities.UIEntities;
import inf.elte.hu.gameengine_javafx.Components.UIComponents.LabelComponent;

public class LabelEntity extends UIEntity<LabelComponent> {
    public LabelEntity() {
        this.uiComponent = new LabelComponent("Hi", 400, 100);
        addComponent(uiComponent);

        addToManager();
        this.addToUI();
    }

}
