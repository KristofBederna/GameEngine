package inf.elte.hu.gameengine_javafx.Misc;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Entities.DummyEntity;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class Globals {
    public static int tileSize = 100;
    public static Entity playerEntity = new DummyEntity(400, 100, "idle", "/assets/images/PlayerIdle.png", 0.8*tileSize, 0.8*tileSize);
}
