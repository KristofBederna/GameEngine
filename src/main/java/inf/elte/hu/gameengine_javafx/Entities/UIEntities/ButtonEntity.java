package inf.elte.hu.gameengine_javafx.Entities.UIEntities;
import inf.elte.hu.gameengine_javafx.Components.UIComponents.ButtonComponent;

public class ButtonEntity extends UIEntity<ButtonComponent> {
    public ButtonEntity() {
        this.uiComponent = new ButtonComponent("Button", 100, 100, 100, 100, () -> {
            System.out.println("ButtonEntity clicked");
        });
        addComponent(uiComponent);

        addToManager();
        this.addToUI();
    }
}
