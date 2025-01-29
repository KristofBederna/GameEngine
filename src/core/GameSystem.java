package core;

import java.util.List;

public abstract class GameSystem {
    public abstract void update(float deltaTime, List<Entity> entities);
}

