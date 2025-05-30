package inf.elte.hu.gameengine_javafx.Components.RenderingComponents;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;
import inf.elte.hu.gameengine_javafx.Misc.Configs.MapConfig;
import inf.elte.hu.gameengine_javafx.Misc.Configs.ResourceConfig;

public class ImageComponent extends Component {
    private String imagePath;
    private double width = -1, height = -1;

    public ImageComponent(String path) {
        this.imagePath = path;
    }

    public ImageComponent(String path, double width, double height) {
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

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
        this.height = MapConfig.scaledTileSize;
        this.width = MapConfig.scaledTileSize;
    }
}
