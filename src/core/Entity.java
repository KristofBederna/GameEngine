package core;

import java.util.HashMap;
import java.util.Map;

public abstract class Entity {
    private final int id;
    private final Map<Class<? extends Component>, Component> components = new HashMap<>();

    public Entity(int id) {
        this.id = id;
    }

    public <T extends Component> void addComponent(T component) {
        components.put(component.getClass(), component);
    }

    public <T extends Component> T getComponent(Class<T> componentType) {
        return componentType.cast(components.get(componentType));
    }
}

