package inf.elte.hu.gameengine_javafx.Core;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityManager<T> {
    private final Map<Integer, T> entities;
    private final Map<Integer, Long> lastAccessed;

    public EntityManager() {
        this.entities = new HashMap<>();
        this.lastAccessed = new HashMap<>();
    }

    public T get(Integer id) {
        if (entities.containsKey(id)) {
            lastAccessed.put(id, System.currentTimeMillis());
            return entities.get(id);
        } else {
            System.err.println("Entity not registered: " + id);
            return null;
        }
    }

    public void register(T entity) {
        this.entities.put(((Entity)entity).getId(), entity);
        EntityHub.getInstance().refreshEntitiesList();
    }


    public void unload(Integer id) {
        entities.remove(id);
        lastAccessed.remove(id);
    }

    public void unloadAll() {
        entities.clear();
        lastAccessed.clear();
    }

    public Map<Integer, T> getEntities() {
        return entities;
    }

    public Long getLastAccessed(Integer id) {
        return lastAccessed.getOrDefault(id, null);
    }

    public void registerAll(List<T> list) {
        for (T entity : list) {
            register(entity);
        }
    }

    public void updateLastUsed(int id) {
        lastAccessed.put(id, System.currentTimeMillis());
    }
}
