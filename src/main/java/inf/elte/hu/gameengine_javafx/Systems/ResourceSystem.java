package inf.elte.hu.gameengine_javafx.Systems;

import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.ResourceHub;
import inf.elte.hu.gameengine_javafx.Core.ResourceManager;

import java.util.Iterator;
import java.util.Map;

public class ResourceSystem extends GameSystem {
    @Override
    public void update(float deltaTime) {
        Map<Class<?>, ResourceManager<?>> resourceManagers = ResourceHub.getInstance().getAllResourceManagers();

        long threshold = System.currentTimeMillis() - 1000;

        for (ResourceManager<?> resourceManager : resourceManagers.values()) {
            Iterator<String> iterator = resourceManager.getResources().keySet().iterator();

            while (iterator.hasNext()) {
                String resourceKey = iterator.next();
                Long lastAccessed = resourceManager.getLastAccessed(resourceKey);

                if (lastAccessed != null && lastAccessed < threshold) {
                    iterator.remove();
                }
            }
        }
    }
}
