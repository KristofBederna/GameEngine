package inf.elte.hu.gameengine_javafx.Core;

import java.util.HashMap;
import java.util.Map;

public class SystemHub {
    private static SystemHub instance;
    private final Map<Class<? extends GameSystem>, GameSystem> systems;

    private SystemHub() {
        systems = new HashMap<>();
    }

    public static SystemHub getInstance() {
        if (instance == null) {
            instance = new SystemHub();
        }
        return instance;
    }

    public <T extends GameSystem> void addSystem(Class<T> systemClass, T system) {
        systems.put(systemClass, system);
    }

    public <T extends GameSystem> void removeSystem(Class<T> systemClass) {
        systems.remove(systemClass);
    }

    public <T extends GameSystem> T getSystem(Class<T> systemClass) {
        return systemClass.cast(systems.get(systemClass));
    }

    public GameSystem[] getAllSystems() {
        return systems.values().toArray(new GameSystem[0]);
    }
}