package inf.elte.hu.gameengine_javafx.Core;

import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Systems.ResourceSystems.SceneManagementSystem;

import java.util.*;

public class SystemHub {
    private static SystemHub instance;
    private final Map<Class<? extends GameSystem>, Integer> systemPriorities;
    private final TreeMap<Integer, GameSystem> systems;
    private boolean isShuttingDown = false;

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

    public void shutDownSystems() {
        if (isShuttingDown) {
            return;
        }

        isShuttingDown = true;
        try {
            SceneManagementSystem sceneManagementSystem = getSystem(SceneManagementSystem.class);

            for (GameSystem system : getAllSystemsInPriorityOrder().reversed()) {
                if (system != sceneManagementSystem) {
                    system.abort();
                }
            }
            systems.clear();
            systems.put(systemPriorities.get(SceneManagementSystem.class), sceneManagementSystem);
        } finally {
            isShuttingDown = false;
        }
    }
}