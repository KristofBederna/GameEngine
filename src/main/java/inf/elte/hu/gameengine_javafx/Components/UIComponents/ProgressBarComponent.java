package inf.elte.hu.gameengine_javafx.Components.UIComponents;

import javafx.scene.control.ProgressBar;

public class ProgressBarComponent extends UIElementComponent {
    private ProgressBar progressBar;
    private double x, y, width, height;

    public ProgressBarComponent(double x, double y, double width, double height, double initialValue) {
        this.progressBar = new ProgressBar(initialValue);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        progressBar.setLayoutX(x);
        progressBar.setLayoutY(y);
        progressBar.setPrefSize(width, height);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgress(double progress) {
        progressBar.setProgress(progress);
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
        progressBar.setLayoutX(x);
        progressBar.setLayoutY(y);
    }
}
