package inf.elte.hu.gameengine_javafx.Components.UIComponents;

import javafx.scene.control.Button;

public class ButtonComponent extends UIElementComponent {
    private Button button;
    private double x, y, width, height;

    public ButtonComponent(String text, double x, double y, double width, double height, Runnable onClick) {
        this.button = new Button(text);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setPrefSize(width, height);

        button.setOnAction(event -> onClick.run());
    }

    public Button getButton() {
        return button;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        button.setLayoutX(x);
        button.setLayoutY(y);
    }
}
