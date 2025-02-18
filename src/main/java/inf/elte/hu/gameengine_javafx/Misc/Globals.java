package inf.elte.hu.gameengine_javafx.Misc;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Entities.DummyEntity;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;

public class Globals {
    public static Entity playerEntity = new DummyEntity(400, 100, "idle", "/assets/images/PlayerIdle.png", 80, 80);
    public static Canvas canvas = new Canvas(1920, 1080);
    public static int tileSize = 100;
}
