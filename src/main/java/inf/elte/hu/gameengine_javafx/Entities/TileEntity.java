package inf.elte.hu.gameengine_javafx.Entities;

import inf.elte.hu.gameengine_javafx.Components.*;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.EntityManager;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;

public class TileEntity extends Entity {
    private int value;
    public TileEntity(int value, int x, int y, String path, double width, double height) {
        this.value = value;
        this.addComponent(new ImageComponent(path, width, height));
        this.addComponent(new PositionComponent(x, y, this));
        this.addComponent(new ZIndexComponent(0));
        this.addComponent(new DimensionComponent(width, height));
    }
    public TileEntity(int value, int x, int y, String path, double width, double height, boolean hasHitBox) {
        this.value = value;
        this.addComponent(new ImageComponent(path, width, height));
        this.addComponent(new PositionComponent(x, y, this));
        this.addComponent(new ZIndexComponent(0));
        this.addComponent(new DimensionComponent(width, height));
        if (hasHitBox) {
            this.addComponent(new RectangularHitBoxComponent(new Point(x, y), new Point(x+width, y), new Point(x, y + height), new Point(x + width, y + height)));
        }
    }
    public TileEntity(int value, int x, int y, String path) {
        this.value = value;
        this.addComponent(new ImageComponent(path));
        this.addComponent(new PositionComponent(x, y, this));
        this.addComponent(new ZIndexComponent(0));
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public void addHitBox(int x, int y, double width, double height) {
        this.addComponent(new RectangularHitBoxComponent(new Point(x, y), new Point(x+width, y), new Point(x, y + height), new Point(x + width, y + height)));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addToManager() {
        EntityManager<TileEntity> manager = EntityHub.getInstance().getEntityManager((Class<TileEntity>)this.getClass());

        if (manager != null) {
            manager.register(this);
        } else {
            manager = new EntityManager<>();
            EntityHub.getInstance().addEntityManager(TileEntity.class, manager);
        }
    }
}
