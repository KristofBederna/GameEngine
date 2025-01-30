package inf.elte.hu.gameengine_javafx.Core;

import java.util.List;

public abstract class GameSystem {
    public abstract void update(float deltaTime, List<Entity> entities);
}

