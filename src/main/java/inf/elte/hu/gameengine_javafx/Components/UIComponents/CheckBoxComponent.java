package inf.elte.hu.gameengine_javafx.Components.UIComponents;

import javafx.scene.control.CheckBox;

public class CheckBoxComponent extends UIComponent<CheckBox> {

    public CheckBoxComponent(String text, double x, double y) {
        super(x, y, 0, 0);  // No width and height as it depends on the CheckBox size
        this.uiElement = new CheckBox(text);
        uiElement.setLayoutX(x);
        uiElement.setLayoutY(y);
    }

    public boolean isSelected() {
        return uiElement.isSelected();
    }

    public void setSelected(boolean selected) {
        uiElement.setSelected(selected);
    }

    @Override
    public String getStatus() {
        return "";
    }
}
