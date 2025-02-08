package inf.elte.hu.gameengine_javafx.Components;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;

public class ImageComponent extends Component {
    private String imagePath;
    private int width = -1, height = -1;

    public ImageComponent(String path) {
        this.imagePath = path;
    }

    public ImageComponent(String path, int width, int height) {
        this.imagePath = path;
        this.width = width;
        this.height = height;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setNextFrame(String nextFramePath) {
        this.imagePath = nextFramePath;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public String getStatus() {
        return (this.getClass().getSimpleName() + ": Image Path: " + imagePath + ", Width: " + width + ", Height: " + height);
    }
}
