package inf.elte.hu.gameengine_javafx.Entities.UIEntities;

import inf.elte.hu.gameengine_javafx.Components.UIComponents.TextFieldComponent;

public class TextFieldEntity extends UIEntity<TextFieldComponent> {
    public TextFieldEntity() {
        this.uiComponent = new TextFieldComponent("Hi", 100, 400, 100, 100);
        addComponent(uiComponent);

        addToManager();
        this.addToUI();
    }
}
