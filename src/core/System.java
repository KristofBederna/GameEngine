package core;

import java.util.List;

public abstract class System {
    public abstract void update(float deltaTime, List<Entity> entities);
}

