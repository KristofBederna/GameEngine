package inf.elte.hu.gameengine_javafx.Components.UIComponents;

import javafx.scene.control.Slider;

public class SliderComponent extends UIElementComponent {
    private Slider slider;
    private double x, y, width, height;

    public SliderComponent(double x, double y, double width, double height, double min, double max, double initialValue) {
        this.slider = new Slider(min, max, initialValue);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        slider.setLayoutX(x);
        slider.setLayoutY(y);
        slider.setPrefSize(width, height);
    }

    public Slider getSlider() {
        return slider;
    }

    public double getValue() {
        return slider.getValue();
    }

    public void setValue(double value) {
        slider.setValue(value);
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        slider.setLayoutX(x);
        slider.setLayoutY(y);
    }
}
