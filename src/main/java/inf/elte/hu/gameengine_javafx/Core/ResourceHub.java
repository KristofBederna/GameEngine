package inf.elte.hu.gameengine_javafx.Core;

import java.util.HashMap;
import java.util.Map;

public class ResourceHub {
    private static ResourceHub instance;
    private final Map<Class<?>, ResourceManager<?>> resourceManagers;

    private ResourceHub() {
        resourceManagers = new HashMap<>();
    }

    public static synchronized ResourceHub getInstance() {
        if (instance == null) {
            instance = new ResourceHub();
        }
        return instance;
    }

    public <T> void addResourceManager(Class<T> classType, ResourceManager<T> resourceManager) {
        resourceManagers.put(classType, resourceManager);
    }

    @SuppressWarnings("unchecked")
    public <T> ResourceManager<T> getResourceManager(Class<T> classType) {
        return (ResourceManager<T>) resourceManagers.get(classType);
    }
    public void removeResourceManager(Class<?> type) {
        resourceManagers.remove(type);
    }

    public void unloadAll() {
        resourceManagers.values().forEach(ResourceManager::unloadAll);
    }

    public Map<Class<?>, ResourceManager<?>> getAllResourceManagers() {
        return resourceManagers;
    }
}
