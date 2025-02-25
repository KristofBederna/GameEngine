package inf.elte.hu.gameengine_javafx.Core.Architecture;

import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.EntityManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.Map;

public abstract class Entity {
    private static int nextId = 0;
    private final int id;
    private final Map<Class<? extends Component>, Component> components = new HashMap<>();

    public Entity() {
        this.id = ++nextId;
    }

    public <T extends Component> void addComponent(T component) {
        components.put(component.getClass(), component);
    }

    public <T extends Component> T getComponent(Class<T> componentType) {
        return componentType.cast(components.get(componentType));
    }

    public <T extends Component> void removeComponentsByType(Class<T> componentType) {
        components.entrySet().removeIf(entry -> componentType.isAssignableFrom(entry.getKey()));
    }

    public int getId() {
        return id;
    }

    public void showComponents() {
        for (Component component : components.values()) {
            java.lang.System.out.println(component.getClass().getSimpleName());
        }
    }

    public Map<Class<? extends Component>, Component> getAllComponents() {
        return components;
    }

    @SuppressWarnings("unchecked")
    protected <T extends Entity> void addToManager() {
        Class<T> entityClass = (Class<T>) this.getClass();
        EntityManager<T> manager = EntityHub.getInstance().getEntityManager(entityClass);

        if (manager == null) {
            manager = new EntityManager<>();
            EntityHub.getInstance().addEntityManager(entityClass, manager);
        }

        manager.register((T) this);
    }
}
