package inf.elte.hu.gameengine_javafx.Core;

import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;

import java.util.*;

public class SystemHub {
    private static SystemHub instance;
    private final Map<Class<? extends GameSystem>, Integer> systemPriorities;
    private final TreeMap<Integer, GameSystem> systems;

    private SystemHub() {
        systemPriorities = new HashMap<>();
        systems = new TreeMap<>();
    }

    public static SystemHub getInstance() {
        if (instance == null) {
            instance = new SystemHub();
        }
        return instance;
    }

    public <T extends GameSystem> void addSystem(Class<T> systemClass, T system, int priority) {
        systemPriorities.put(systemClass, priority);
        systems.put(priority, system);
    }

    public <T extends GameSystem> void removeSystem(Class<T> systemClass) {
        Integer priority = systemPriorities.remove(systemClass);
        if (priority != null) {
            systems.remove(priority);
        }
    }

    public <T extends GameSystem> T getSystem(Class<T> systemClass) {
        Integer priority = systemPriorities.get(systemClass);
        return (priority != null) ? systemClass.cast(systems.get(priority)) : null;
    }

    public List<GameSystem> getAllSystemsInPriorityOrder() {
        return new ArrayList<>(systems.values());
    }
}
