package inf.elte.hu.gameengine_javafx.Misc.StartUpClasses;

import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.SystemHub;
import inf.elte.hu.gameengine_javafx.Misc.GameLoop;
import inf.elte.hu.gameengine_javafx.Systems.LogSystem;

public class GameLoopStartUp {
    public GameLoopStartUp() {
        GameLoop gameloop = new GameLoop(60) {
            @Override
            public void update() {
                var systems = SystemHub.getInstance().getAllSystemsInPriorityOrder();
                for (GameSystem system : systems) {
                    system.update();
                }
            }
        };
        gameloop.startLoop();
    }
}

