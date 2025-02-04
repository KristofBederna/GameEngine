package inf.elte.hu.gameengine_javafx.Core;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ResourceManager<T> {
    private final Map<String, T> resources;
    private final Function<String, T> loader;

    public ResourceManager(Function<String, T> loader) {
        this.loader = loader;
        this.resources = new HashMap<>();
    }

    public T get(String key) {
        if (resources.containsKey(key)) {
            return resources.get(key);
        }

        T resource = loader.apply(key);
        if (resource == null) {
            System.err.println("Failed to load resource: " + key);
            return null;
        }
        resources.put(key, resource);
        return resource;
    }

    public void unload(String key) {
        resources.remove(key);
    }

    public void unloadAll() {
        resources.clear();
    }

    public Map<String, T> getResources() {
        return resources;
    }
}
