package inf.elte.hu.gameengine_javafx.Components.UIComponents;

import javafx.scene.control.TextField;

public class TextFieldComponent extends UIElementComponent {
    private TextField textField;
    private double x, y, width, height;

    public TextFieldComponent(double x, double y, double width, double height) {
        this.textField = new TextField();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        textField.setLayoutX(x);
        textField.setLayoutY(y);
        textField.setPrefSize(width, height);
    }

    public TextField getTextField() {
        return textField;
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        textField.setText(text);
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        textField.setLayoutX(x);
        textField.setLayoutY(y);
    }
}
