package inf.elte.hu.gameengine_javafx.Misc.Layers;

import javafx.application.Platform;
import javafx.scene.layout.Pane;

public class uiRoot extends Pane {
    private static uiRoot instance;

    private uiRoot() {
        super();
    }

    public static uiRoot getInstance() {
        if (instance == null) {
            instance = new uiRoot();
        }
        return instance;
    }

    public void unloadAll() {
        Platform.runLater(()->this.getChildren().clear());
    }
}