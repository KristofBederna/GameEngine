package inf.elte.hu.gameengine_javafx.Misc.Layers;

import javafx.scene.canvas.Canvas;

public class GameCanvas extends Canvas {
    private static GameCanvas instance;

    private GameCanvas(double width, double height) {
        super(width, height);
    }

    public static GameCanvas createInstance(double width, double height) {
        if (instance == null) {
            instance = new GameCanvas(width, height);
        }
        return instance;
    }

    public static GameCanvas getInstance() {
        return instance;
    }
}
