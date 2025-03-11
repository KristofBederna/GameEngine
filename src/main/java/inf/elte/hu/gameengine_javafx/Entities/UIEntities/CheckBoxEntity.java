package inf.elte.hu.gameengine_javafx.Entities.UIEntities;
import inf.elte.hu.gameengine_javafx.Components.UIComponents.CheckBoxComponent;

public class CheckBoxEntity extends UIEntity<CheckBoxComponent> {
    public CheckBoxEntity() {
        this.uiComponent = new CheckBoxComponent("Hi", 300, 100, 0, 0);
        addComponent(uiComponent);

        addToManager();
        this.addToUI();
    }
}
