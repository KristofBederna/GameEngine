package inf.elte.hu.gameengine_javafx.Core;

import inf.elte.hu.gameengine_javafx.Components.CameraComponent;
import inf.elte.hu.gameengine_javafx.Components.ImageComponent;
import inf.elte.hu.gameengine_javafx.Components.PositionComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class EntityHub {
    private static EntityHub instance;
    private final Map<Class<?>, EntityManager<?>> entityManagers;
    List<Entity> entities = new ArrayList<>();

    private EntityHub() {
        entityManagers = new HashMap<>();
    }

    public static synchronized EntityHub getInstance() {
        if (instance == null) {
            instance = new EntityHub();
        }
        return instance;
    }

    public <T extends Entity> void addEntityManager(Class<T> type, EntityManager<T> entityManager) {
        entityManagers.put(type, entityManager);
        refreshEntitiesList();
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> EntityManager<T> getEntityManager(Class<T> type) {
        return (EntityManager<T>) entityManagers.get(type);
    }

    public void removeEntityManager(Class<?> type) {
        entityManagers.remove(type);
    }

    public void removeAllEntityManagers() {
        entityManagers.clear();
    }

    public void unloadAll() {
        entityManagers.values().forEach(EntityManager::unloadAll);
    }

    public Map<Class<?>, EntityManager<?>> getAllEntityManagers() {
        return entityManagers;
    }

    public List<Entity> getAllEntities() {
        return entities;
    }

    @SuppressWarnings("unchecked")
    public void refreshEntitiesList() {
        entities.clear();
        for (EntityManager<?> entityManager : entityManagers.values()) {
            entities.addAll((Collection<? extends Entity>) entityManager.getEntities().values());
        }
    }

    public List<Entity> getEntitiesInsideViewport(CameraComponent camera) {
        List<Entity> visibleEntities = new ArrayList<>();
        for (Entity entity : getAllEntities()) {
            PositionComponent position = entity.getComponent(PositionComponent.class);
            if (position == null) continue;

            if (entity.getComponent(ImageComponent.class) == null) {
                continue;
            }
            if (camera.isPositionInsideViewport(position.getGlobalX(), position.getGlobalY(),
                    entity.getComponent(ImageComponent.class).getWidth(), entity.getComponent(ImageComponent.class).getHeight())) {
                visibleEntities.add(entity);
            }
        }

        return visibleEntities;
    }
}
