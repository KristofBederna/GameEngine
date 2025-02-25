package inf.elte.hu.gameengine_javafx.Components.UIComponents;

import javafx.scene.control.Label;

public class LabelComponent extends UIElementComponent {
    private Label label;
    private double x, y;

    public LabelComponent(String text, double x, double y) {
        this.label = new Label(text);
        this.x = x;
        this.y = y;

        label.setLayoutX(x);
        label.setLayoutY(y);
    }

    public Label getLabel() {
        return label;
    }

    public void setText(String text) {
        label.setText(text);
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        label.setLayoutX(x);
        label.setLayoutY(y);
    }
}
