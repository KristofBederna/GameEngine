package inf.elte.hu.gameengine_javafx.Misc.MapClasses;

import inf.elte.hu.gameengine_javafx.Entities.TileEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Misc.Config;
import inf.elte.hu.gameengine_javafx.Misc.Tuple;

import java.util.HashMap;
import java.util.Map;

public class World {
    Map<Tuple<Integer,Integer>, Chunk> chunks = new HashMap<>();
    Map<Tuple<Integer, Integer>, Chunk> savedChunks = new HashMap<>();


    public World() {
    }

    public Map<Tuple<Integer, Integer>, Chunk> getSavedChunks() {
        return savedChunks;
    }

    public void setSavedChunks(Map<Tuple<Integer, Integer>, Chunk> savedChunks) {
        this.savedChunks = savedChunks;
    }

    public void clear() {
        chunks.clear();
    }

    public Chunk get(int x, int y) {
        return chunks.getOrDefault(new Tuple<>(x, y), null);
    }

    public int size() {
        return chunks.size();
    }

    public Map<Tuple<Integer,Integer>, Chunk> getWorld() {
        return chunks;
    }

    public void addChunk(int x, int y, Chunk chunk) {
        chunks.putIfAbsent(new Tuple<>(x, y), chunk);
    }

    public TileEntity getElementAt(int tileX, int tileY) {
        int chunkX = Math.floorDiv(tileX, Config.chunkHeight);
        int chunkY = Math.floorDiv(tileY, Config.chunkWidth);

        int localX = Math.floorMod(tileX, Config.chunkHeight);
        int localY = Math.floorMod(tileY, Config.chunkWidth);

        Chunk chunk = chunks.get(new Tuple<>(chunkX, chunkY));
        if (chunk != null) {
            return chunk.getElement(localX, localY);
        }
        return null;
    }

    public TileEntity getElementAt(Point point) {
        int tileX = Math.floorDiv((int) point.getX(), Config.tileSize);
        int tileY = Math.floorDiv((int) point.getY(), Config.tileSize);

        int chunkX = Math.floorDiv(tileX, Config.chunkWidth);
        int chunkY = Math.floorDiv(tileY, Config.chunkHeight);


        int localX = Math.floorMod(tileY, Config.chunkWidth);
        int localY = Math.floorMod(tileX, Config.chunkHeight);


        Chunk chunk = chunks.get(new Tuple<>(chunkX, chunkY));
        if (chunk != null) {
            return chunk.getElement(localX, localY);
        }
        return null;
    }

}
