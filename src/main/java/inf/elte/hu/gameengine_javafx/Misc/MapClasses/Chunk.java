package inf.elte.hu.gameengine_javafx.Misc.MapClasses;

import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.DimensionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Entities.TileEntity;

import java.util.ArrayList;
import java.util.List;

public class Chunk {
    List<List<TileEntity>> chunk;

    public Chunk(List<List<TileEntity>> chunk) {
        this.chunk = chunk;
    }

    public Chunk() {
        this.chunk = new ArrayList<>();
    }

    public List<List<TileEntity>> getChunk() {
        return chunk;
    }

    public void setChunk(List<List<TileEntity>> chunk) {
        this.chunk = chunk;
    }

    public void addChunk(List<List<TileEntity>> chunk) {
        this.chunk.addAll(chunk);
    }

    public void addChunk(Chunk chunk) {
        this.chunk.addAll(chunk.getChunk());
    }

    public void clear() {
        this.chunk.clear();
    }

    public List<TileEntity> get(int number) {
        return chunk.get(number);
    }

    public int size() {
        return chunk.size();
    }

    public TileEntity getElement(int x, int y) {
        return chunk.get(x).get(y);
    }

    public void setElement(int x, int y, int value) {
        TileEntity tileEntity = chunk.get(x).get(y);
        TileEntity newTileEntity = new TileEntity(value, tileEntity.getComponent(PositionComponent.class).getGlobalX(), tileEntity.getComponent(PositionComponent.class).getGlobalY(), "/assets/tiles/" + TileLoader.getTilePath(value) + ".png", tileEntity.getComponent(DimensionComponent.class).getWidth(), tileEntity.getComponent(DimensionComponent.class).getHeight(), value != 9);
        chunk.get(x).set(y, newTileEntity);
    }
}
