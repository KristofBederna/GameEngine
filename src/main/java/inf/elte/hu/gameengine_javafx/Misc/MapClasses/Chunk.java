package inf.elte.hu.gameengine_javafx.Misc.MapClasses;

import inf.elte.hu.gameengine_javafx.Entities.TileEntity;

import java.util.ArrayList;
import java.util.List;

public class Chunk {
    List<List<TileEntity>> chunk;
    final int width = 16, height = 16;

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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
