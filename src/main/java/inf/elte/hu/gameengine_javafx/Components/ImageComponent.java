package inf.elte.hu.gameengine_javafx.Components;

import inf.elte.hu.gameengine_javafx.Core.Component;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;

public class ImageComponent extends Component {
    private Image image;
    private int Width, Height = -1;
    public ImageComponent(String path) {
        try {
            setImage(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public ImageComponent(String path, int width, int height) {
        try {
            setImage(path);
            this.Width = width;
            this.Height = height;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Image getImage() {
        return image;
    }

    public void setImage(String path) throws IOException {
        InputStream resource = getClass().getResourceAsStream(path);
        if (resource == null) {
            throw new IOException("Image resource not found: " + path);
        }
        this.image = new Image(resource);
    }

    public void setImage(Image nextFrame) {
        this.image = nextFrame;
    }

    public int getHeight() {
        return Height;
    }
    public int getWidth() {
        return Width;
    }

    public void setHeight(int height) {
        Height = height;
    }
    public void setWidth(int width) {
        Width = width;
    }
}
