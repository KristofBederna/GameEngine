package inf.elte.hu.gameengine_javafx.Misc;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Entities.DummyEntity;
import javafx.scene.canvas.Canvas;

public class Globals {
    public static Entity playerEntity = new DummyEntity(100, 100, "idle", "/assets/images/PlayerIdle.png", 80, 80, 500, 500, 30*100, 15*100);
    public static final Canvas canvas = new Canvas(500, 500);
    public static int tileSize = 100;
}
