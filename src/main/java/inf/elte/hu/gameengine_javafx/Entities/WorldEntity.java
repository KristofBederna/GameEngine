package inf.elte.hu.gameengine_javafx.Entities;

import inf.elte.hu.gameengine_javafx.Components.FilePathComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.TileSetComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.WorldDataComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.WorldDimensionComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.EntityManager;

public class WorldEntity extends Entity {
    private static WorldEntity instance;

    private WorldEntity(double width, double height, String filePath, String tileSetPath, String separator) {
        this.addComponent(new WorldDimensionComponent(width, height));
        this.addComponent(new WorldDataComponent());
        this.addComponent(new FilePathComponent(filePath));
        this.addComponent(new TileSetComponent(tileSetPath, separator));
        addToManager();
    }

    private WorldEntity(double width, double height, String filePath, String tileSetPath) {
        this.addComponent(new WorldDimensionComponent(width, height));
        this.addComponent(new WorldDataComponent());
        this.addComponent(new FilePathComponent(filePath));
        this.addComponent(new TileSetComponent(tileSetPath));
        addToManager();
    }

    public static WorldEntity getInstance(double width, double height, String filePath, String tileSetPath, String separator) {
        if (instance == null) {
            instance = new WorldEntity(width, height, filePath, tileSetPath, separator);
        }
        return instance;
    }

    public static WorldEntity getInstance(double width, double height, String filePath, String tileSetPath) {
        if (instance == null) {
            instance = new WorldEntity(width, height, filePath, tileSetPath);
        }
        return instance;
    }

    public static WorldEntity getInstance() {
        return instance;
    }

    public static void resetInstance() {
        instance = null;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addToManager() {
        EntityManager<WorldEntity> manager = EntityHub.getInstance().getEntityManager((Class<WorldEntity>) this.getClass());
        if (manager != null) {
            manager.register(this);
        } else {
            manager = new EntityManager<>();
            EntityHub.getInstance().addEntityManager(WorldEntity.class, manager);
            manager.register(this);
        }
    }
}