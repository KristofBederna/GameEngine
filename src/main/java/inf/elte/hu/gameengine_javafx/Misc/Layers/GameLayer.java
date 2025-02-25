package inf.elte.hu.gameengine_javafx.Misc.Layers;

import javafx.scene.layout.StackPane;

public class GameLayer extends StackPane {
    private static GameLayer instance;
    private GameCanvas canvas;
    private uiRoot UI;

    private GameLayer() {
        super();
        canvas = GameCanvas.getInstance();
        UI = uiRoot.getInstance();
    }

    public static GameLayer getInstance() {
        if (instance == null) {
            instance = new GameLayer();
        }
        return instance;
    }

    public GameCanvas getCanvas() {
        return canvas;
    }
    public uiRoot getUIRoot() {
        return UI;
    }
}
