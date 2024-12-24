package views;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class GamePanel extends JPanel {
    public GamePanel() {
        setFocusable(true);
    }


    private Consumer<Graphics2D> renderCallback;

    public void setRenderCallback(Consumer<Graphics2D> renderCallback) {
        this.renderCallback = renderCallback;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (renderCallback != null) {
            renderCallback.accept((Graphics2D) g);
        }
    }
}
