package inf.elte.hu.gameengine_javafx.Misc.MapClasses;

import inf.elte.hu.gameengine_javafx.Entities.TileEntity;
import inf.elte.hu.gameengine_javafx.Misc.Tuple;

import java.util.HashMap;
import java.util.Map;

public class World {
    Map<Tuple<Integer,Integer>, Chunk> chunks = new HashMap<>();

    public World() {
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
        int chunkX = Math.floorDiv(tileX, 16);
        int chunkY = Math.floorDiv(tileY, 16);

        int localX = Math.floorMod(tileX, 16);
        int localY = Math.floorMod(tileY, 16);

        Chunk chunk = chunks.get(new Tuple<>(chunkX, chunkY));
        if (chunk != null) {
            return chunk.getElement(localX, localY);
        }
        return null;
    }

}
