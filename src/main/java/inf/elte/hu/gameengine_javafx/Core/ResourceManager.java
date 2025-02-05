package inf.elte.hu.gameengine_javafx.Core;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ResourceManager<T> {
    private final Map<String, T> resources;
    private final Map<String, Long> lastAccessed;
    private final Function<String, T> loader;

    public ResourceManager(Function<String, T> loader) {
        this.loader = loader;
        this.resources = new HashMap<>();
        this.lastAccessed = new HashMap<>();
    }

    public T get(String key) {
        if (resources.containsKey(key)) {
            lastAccessed.put(key, System.currentTimeMillis());
            return resources.get(key);
        }

        T resource = loader.apply(key);
        if (resource == null) {
            System.err.println("Failed to load resource: " + key);
            return null;
        }

        resources.put(key, resource);
        lastAccessed.put(key, System.currentTimeMillis());
        return resource;
    }

    public void unload(String key) {
        resources.remove(key);
        lastAccessed.remove(key);
    }

    public void unloadAll() {
        resources.clear();
        lastAccessed.clear();
    }

    public Map<String, T> getResources() {
        return resources;
    }

    public Long getLastAccessed(String key) {
        return lastAccessed.getOrDefault(key, null);
    }
}
