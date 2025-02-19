package inf.elte.hu.gameengine_javafx.Misc.Scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;

public abstract class GameScene extends Scene {
    public GameScene(Parent parent, double width, double height) {
        super(parent, width, height);
    }

    public abstract void setup();
    public abstract void breakdown();
}
