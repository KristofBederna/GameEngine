package inf.elte.hu.gameengine_javafx.Misc.StartUpClasses;

import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.SystemHub;
import inf.elte.hu.gameengine_javafx.Misc.GameLoop;

public class GameLoopStartUp {
    static private GameLoop gameLoop;
    public GameLoopStartUp() {
        gameLoop = new GameLoop() {
            @Override
            public void update() {
                var systems = SystemHub.getInstance().getAllSystemsInPriorityOrder();
                for (GameSystem system : systems) {
                    if (!system.getIsActive()) {
                        if (system.getIsAborting()) {
                            continue;
                        }
                        system.start();
                    } else {
                        system.run();
                    }
                }
            }
        };
        gameLoop.startLoop();
    }

    public static void stopGameLoop() {
        gameLoop.stopLoop();
    }
}

