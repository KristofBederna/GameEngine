package inf.elte.hu.gameengine_javafx.Systems.ResourceSystems;

import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.DimensionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.TileSetComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.WorldDataComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import inf.elte.hu.gameengine_javafx.Entities.TileEntity;
import inf.elte.hu.gameengine_javafx.Entities.WorldEntity;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.Chunk;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.World;
import inf.elte.hu.gameengine_javafx.Misc.Tuple;

import java.util.*;

public class DynamicWorldLoaderSystem extends GameSystem {
    private static final int CHUNK_SIZE = 16;
    private static final int LOAD_DISTANCE = 2;

    private final Map<Tuple<Integer, Integer>, Chunk> savedChunks = new HashMap<>();

    @Override
    public void start() {
        this.active = true;
        WorldEntity map = WorldEntity.getInstance();
        if (map == null) return;

        World worldData = map.getComponent(WorldDataComponent.class).getMapData();

        for (int cx = -1; cx <= 1; cx++) {
            for (int cy = -1; cy <= 1; cy++) {
                loadOrGenerateChunk(worldData, cx, cy);
            }
        }
    }

    @Override
    public void update() {
        CameraEntity camera = CameraEntity.getInstance();
        WorldEntity map = WorldEntity.getInstance();
        if (map == null || camera == null) return;

        double camX = camera.getComponent(PositionComponent.class).getGlobalX();
        double camY = camera.getComponent(PositionComponent.class).getGlobalY();
        double camWidth = camera.getComponent(DimensionComponent.class).getWidth();
        double camHeight = camera.getComponent(DimensionComponent.class).getHeight();

        World worldData = map.getComponent(WorldDataComponent.class).getMapData();
        Set<Tuple<Integer, Integer>> loadedChunks = worldData.getWorld().keySet();

        int playerChunkX = Math.floorDiv((int) (camX + camWidth / 2), CHUNK_SIZE * Globals.tileSize);
        int playerChunkY = Math.floorDiv((int) (camY + camHeight / 2), CHUNK_SIZE * Globals.tileSize);

        for (int dx = -LOAD_DISTANCE; dx <= LOAD_DISTANCE; dx++) {
            for (int dy = -LOAD_DISTANCE; dy <= LOAD_DISTANCE; dy++) {
                int chunkX = playerChunkX + dx;
                int chunkY = playerChunkY + dy;
                Tuple<Integer, Integer> chunkKey = new Tuple<>(chunkX, chunkY);

                if (!loadedChunks.contains(chunkKey)) {
                    loadOrGenerateChunk(worldData, chunkX, chunkY);
                }
            }
        }

        unloadFarChunks(worldData, playerChunkX, playerChunkY);
    }

    private void unloadFarChunks(World worldData, int playerChunkX, int playerChunkY) {
        Iterator<Map.Entry<Tuple<Integer, Integer>, Chunk>> iterator = worldData.getWorld().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Tuple<Integer, Integer>, Chunk> entry = iterator.next();
            int chunkX = entry.getKey().first();
            int chunkY = entry.getKey().second();

            if (Math.abs(chunkX - playerChunkX) > LOAD_DISTANCE || Math.abs(chunkY - playerChunkY) > LOAD_DISTANCE) {
                savedChunks.put(entry.getKey(), entry.getValue());
                iterator.remove();
            }
        }
    }


    private void loadOrGenerateChunk(World worldData, int chunkX, int chunkY) {
        Tuple<Integer, Integer> chunkKey = new Tuple<>(chunkX, chunkY);

        if (savedChunks.containsKey(chunkKey)) {
            worldData.addChunk(chunkX, chunkY, savedChunks.get(chunkKey));
        } else {
            Chunk newChunk = generateChunk(chunkX, chunkY);
            savedChunks.put(chunkKey, newChunk);
            worldData.addChunk(chunkX, chunkY, newChunk);
        }
    }

    private Chunk generateChunk(int chunkX, int chunkY) {
        List<List<TileEntity>> tiles = new ArrayList<>();
        TileSetComponent tileSet = WorldEntity.getInstance().getComponent(TileSetComponent.class);

        for (int y = 0; y < CHUNK_SIZE; y++) {
            List<TileEntity> row = new ArrayList<>();
            for (int x = 0; x < CHUNK_SIZE; x++) {
                int worldX = chunkX * CHUNK_SIZE * Globals.tileSize + x * Globals.tileSize;
                int worldY = chunkY * CHUNK_SIZE * Globals.tileSize + y * Globals.tileSize;

                int tileValue = 9;

                String tilePath = tileSet.getTileLoader().getTilePath(tileValue);
                if (tilePath == null) {
                    tilePath = "default.png";
                }

                TileEntity tile = new TileEntity(tileValue, worldX, worldY, "/assets/tiles/" + tilePath + ".png", Globals.tileSize, Globals.tileSize);
                row.add(tile);
            }
            tiles.add(row);
        }
        return new Chunk(tiles);
    }
}
