package inf.elte.hu.gameengine_javafx.Components.UIComponents;

import javafx.scene.control.CheckBox;

public class CheckBoxComponent extends UIElementComponent {
    private CheckBox checkBox;
    private double x, y;

    public CheckBoxComponent(String text, double x, double y) {
        this.checkBox = new CheckBox(text);
        this.x = x;
        this.y = y;

        checkBox.setLayoutX(x);
        checkBox.setLayoutY(y);
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public boolean isSelected() {
        return checkBox.isSelected();
    }

    public void setSelected(boolean selected) {
        checkBox.setSelected(selected);
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        checkBox.setLayoutX(x);
        checkBox.setLayoutY(y);
    }
}
