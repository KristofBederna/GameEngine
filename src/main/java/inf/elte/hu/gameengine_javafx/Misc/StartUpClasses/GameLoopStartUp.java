package inf.elte.hu.gameengine_javafx.Misc.StartUpClasses;

import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.SystemHub;
import inf.elte.hu.gameengine_javafx.Misc.GameLoop;

public class GameLoopStartUp {
    public GameLoopStartUp() {
        GameLoop gameloop = new GameLoop(60) {
            @Override
            public void update() {
                for (GameSystem system : SystemHub.getInstance().getAllSystemsInPriorityOrder()) {
                    system.update(1.0f/60.0f);
                }
            }
        };
        gameloop.startLoop();
    }
}
