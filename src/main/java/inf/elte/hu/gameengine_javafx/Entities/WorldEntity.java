package inf.elte.hu.gameengine_javafx.Entities;

import inf.elte.hu.gameengine_javafx.Components.FilePathComponent;
import inf.elte.hu.gameengine_javafx.Components.TileSetComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldDataComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldDimensionComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.EntityManager;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.TileLoader;

public class WorldEntity extends Entity {
    public WorldEntity(double width, double height, String filePath, String tileSetPath, String separator) {
        this.addComponent(new WorldDimensionComponent(width, height));
        this.addComponent(new WorldDataComponent());
        this.addComponent(new FilePathComponent(filePath));
        this.addComponent(new TileSetComponent(tileSetPath, new TileLoader(), separator));

        addToManager();
    }
    public WorldEntity(double width, double height, String filePath, String tileSetPath) {
        this.addComponent(new WorldDimensionComponent(width, height));
        this.addComponent(new WorldDataComponent());
        this.addComponent(new FilePathComponent(filePath));
        this.addComponent(new TileSetComponent(tileSetPath, new TileLoader()));

        addToManager();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addToManager() {
        EntityManager<WorldEntity> manager = EntityHub.getInstance().getEntityManager((Class<WorldEntity>)this.getClass());

        if (manager != null) {
            manager.register(this);
        } else {
            manager = new EntityManager<>();
            EntityHub.getInstance().addEntityManager(WorldEntity.class, manager);
            manager.register(this);
        }
    }
}
