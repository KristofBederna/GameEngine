package inf.elte.hu.gameengine_javafx.Systems.ResourceSystems;

import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.HitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.RectangularHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.CentralMassComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.DimensionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.MapMeshComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.WorldDataComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import inf.elte.hu.gameengine_javafx.Entities.TileEntity;
import inf.elte.hu.gameengine_javafx.Entities.WorldEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.Chunk;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.WorldGenerator;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.World;
import inf.elte.hu.gameengine_javafx.Misc.Tuple;

import java.util.*;

public class DynamicWorldLoaderSystem extends GameSystem {
    private static final int MAX_WORLD_WIDTH = 2;
    private static final int MAX_WORLD_HEIGHT = 1;
    private static final int CHUNK_SIZE = 8;
    private static final int LOAD_DISTANCE = 2;

    private final Map<Tuple<Integer, Integer>, Chunk> savedChunks = new HashMap<>();

    @Override
    public void start() {
        this.active = true;
        WorldEntity map = WorldEntity.getInstance();
        if (map == null) return;
        loadFullWorld();
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
        int playerChunkX = Math.floorDiv((int) (camX + camWidth / 2), CHUNK_SIZE * Globals.tileSize);
        int playerChunkY = Math.floorDiv((int) (camY + camHeight / 2), CHUNK_SIZE * Globals.tileSize);
        loadSurroundingChunks(playerChunkX, playerChunkY);
        unloadFarChunks(playerChunkX, playerChunkY);
    }

    private void loadFullWorld() {
        for (int cx = 0; cx < MAX_WORLD_WIDTH; cx++) {
            for (int cy = 0; cy < MAX_WORLD_HEIGHT; cy++) {
                loadOrGenerateChunk(cx, cy);
            }
        }
    }

    private void loadSurroundingChunks(int playerChunkX, int playerChunkY) {
        Set<Tuple<Integer, Integer>> loadedChunks = WorldEntity.getInstance().getComponent(WorldDataComponent.class).getMapData().getWorld().keySet();
        for (int dx = -LOAD_DISTANCE; dx <= LOAD_DISTANCE; dx++) {
            for (int dy = -LOAD_DISTANCE; dy <= LOAD_DISTANCE; dy++) {
                int chunkX = playerChunkX + dx;
                int chunkY = playerChunkY + dy;
                if (chunkX >= 0 && chunkX < MAX_WORLD_WIDTH && chunkY >= 0 && chunkY < MAX_WORLD_HEIGHT) {
                    Tuple<Integer, Integer> chunkKey = new Tuple<>(chunkX, chunkY);
                    if (!loadedChunks.contains(chunkKey)) {
                        loadOrGenerateChunk(chunkX, chunkY);
                    }
                }
            }
        }
    }

    private void unloadFarChunks(int playerChunkX, int playerChunkY) {
        Iterator<Map.Entry<Tuple<Integer, Integer>, Chunk>> iterator = WorldEntity.getInstance().getComponent(WorldDataComponent.class).getMapData().getWorld().entrySet().iterator();
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

    private void loadOrGenerateChunk(int chunkX, int chunkY) {
        Tuple<Integer, Integer> chunkKey = new Tuple<>(chunkX, chunkY);

        if (savedChunks.containsKey(chunkKey)) {
            WorldEntity.getInstance().getComponent(WorldDataComponent.class).getMapData().addChunk(chunkX, chunkY, savedChunks.get(chunkKey));
        } else {
            Chunk newChunk = WorldGenerator.generateChunk(chunkX, chunkY, CHUNK_SIZE);
            savedChunks.put(chunkKey, newChunk);
            WorldEntity.getInstance().getComponent(WorldDataComponent.class).getMapData().addChunk(chunkX, chunkY, newChunk);
        }
        addBoundaryWalls(WorldEntity.getInstance().getComponent(WorldDataComponent.class).getMapData().getWorld().get(chunkKey), chunkX, chunkY);
        addWorldMesh();
    }

    private void addWorldMesh() {
        WorldEntity map = WorldEntity.getInstance();
        if (map == null) return;
        MapMeshComponent mapMesh = map.getComponent(MapMeshComponent.class);
        List<Point> meshRow = new ArrayList<>();
        map.getComponent(WorldDataComponent.class).getMapData().getWorld().values().forEach(chunk -> {
            for (List<TileEntity> tileEntities : chunk.getChunk()) {
                for (TileEntity tileEntity : tileEntities) {
                    if (tileEntity.getComponent(RectangularHitBoxComponent.class) != null) {
                        continue;
                    }
                    meshRow.add(new Point(tileEntity.getComponent(CentralMassComponent.class).getCentralX(), tileEntity.getComponent(CentralMassComponent.class).getCentralY()));
                }
                mapMesh.addRow(meshRow);
            }
        });
    }

    private void addBoundaryWalls(Chunk chunk, int chunkX, int chunkY) {
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                if (x == 0 && y == 0 && chunkX == 0 && chunkY == 0) {
                    chunk.setElement(x, y, 3); // topLeftWall
                } else if (x == CHUNK_SIZE - 1 && y == 0 && chunkX == 0 && chunkY == MAX_WORLD_HEIGHT-1) {
                    chunk.setElement(x, y, 1); // BottomLeftWall
                } else if (x == 0 && y == CHUNK_SIZE - 1 && chunkX == MAX_WORLD_WIDTH-1 && chunkY == 0) {
                    chunk.setElement(x, y, 4); // topRightWall
                } else if (x == CHUNK_SIZE - 1 && y == CHUNK_SIZE - 1 && chunkX == MAX_WORLD_WIDTH-1 && chunkY == MAX_WORLD_HEIGHT-1) {
                    chunk.setElement(x, y, 2); // bottomRightWall
                } else if (x == 0) {
                    chunk.setElement(x, y, 7); // topWall
                } else if (x == CHUNK_SIZE - 1) {
                    chunk.setElement(x, y, 8); // bottomWall
                } else if (y == 0 && chunkX == 0) {
                    chunk.setElement(x, y, 5); // leftWall
                } else if (y == CHUNK_SIZE - 1  && chunkX == MAX_WORLD_WIDTH-1) {
                    chunk.setElement(x, y, 6); // rightWall
                } else {
                    chunk.setElement(x, y, 9); // windowWall
                }
            }
        }
    }
}
