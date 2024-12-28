package components;

import core.Component;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageComponent extends Component {
    private BufferedImage Image;
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

    public BufferedImage getImage() {
        return Image;
    }

    public void setImage(String path) throws IOException {
        var resource = getClass().getResourceAsStream(path);
        if (resource == null) {
            throw new IOException("Image resource not found: " + path);
        }
        this.Image = ImageIO.read(resource);
    }

    public void setImage(BufferedImage nextFrame) {
        this.Image = nextFrame;
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
