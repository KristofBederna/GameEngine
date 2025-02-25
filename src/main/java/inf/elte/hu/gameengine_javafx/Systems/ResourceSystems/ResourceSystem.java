package inf.elte.hu.gameengine_javafx.Systems.ResourceSystems;

import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.ResourceHub;
import inf.elte.hu.gameengine_javafx.Core.ResourceManager;

import java.util.*;

public class ResourceSystem extends GameSystem {
    @Override
    public void start() {
        this.active = true;
    }

    @Override
    public void update() {
        Map<Class<?>, ResourceManager<?>> resourceManagers = ResourceHub.getInstance().getAllResourceManagers();

        long threshold = System.currentTimeMillis() - 1000;

        for (ResourceManager<?> resourceManager : resourceManagers.values()) {
            synchronized (resourceManager) {
                Map<String, ?> resourcesSnapshot = new HashMap<>(resourceManager.getResources());
                Iterator<? extends Map.Entry<String, ?>> iterator = resourcesSnapshot.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, ?> resourceEntry = iterator.next();
                    String resourceKey = resourceEntry.getKey();
                    Long lastAccessed = resourceManager.getLastAccessed(resourceKey);

                    if (lastAccessed != null && lastAccessed < threshold) {
                        iterator.remove();
                    }
                }
            }
        }
    }
}

