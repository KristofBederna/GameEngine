package inf.elte.hu.gameengine_javafx.Systems.ResourceSystems;

import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.RectangularHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.CentralMassComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.DimensionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.MapMeshComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.WorldDataComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import inf.elte.hu.gameengine_javafx.Entities.TileEntity;
import inf.elte.hu.gameengine_javafx.Entities.WorldEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Misc.Config;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.Chunk;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.World;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.WorldGenerator;
import inf.elte.hu.gameengine_javafx.Misc.Tuple;

import java.util.*;

public class InfiniteWorldLoaderSystem extends GameSystem {
    private static final int CHUNK_SIZE = 8;
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

        int playerChunkX = Math.floorDiv((int) (camX + camWidth / 2), CHUNK_SIZE * Config.tileSize);
        int playerChunkY = Math.floorDiv((int) (camY + camHeight / 2), CHUNK_SIZE * Config.tileSize);

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

    private Set<Tuple<Double, Double>> addedTileCoordinates = new HashSet<>();

    private void addWorldMesh() {
        WorldEntity map = WorldEntity.getInstance();
        if (map == null) return;

        MapMeshComponent mapMesh = map.getComponent(MapMeshComponent.class);
        List<Point> meshRow = new ArrayList<>();
        Map<Tuple<Integer, Integer>, Chunk> worldChunks = map.getComponent(WorldDataComponent.class).getMapData().getWorld();

        worldChunks.values().forEach(chunk -> {
            for (List<TileEntity> tileEntities : chunk.getChunk()) {
                for (TileEntity tileEntity : tileEntities) {
                    if (tileEntity.getComponent(RectangularHitBoxComponent.class) != null) {
                        continue;
                    }
                    double tileX = tileEntity.getComponent(CentralMassComponent.class).getCentralX();
                    double tileY = tileEntity.getComponent(CentralMassComponent.class).getCentralY();
                    Tuple<Double, Double> tileCoordinates = new Tuple<>(tileX, tileY);
                    if (!addedTileCoordinates.contains(tileCoordinates)) {
                        meshRow.add(new Point(tileX, tileY));
                        addedTileCoordinates.add(tileCoordinates);
                    }
                }

                if (!meshRow.isEmpty()) {
                    mapMesh.addRow(new ArrayList<>(meshRow));
                    meshRow.clear();
                }
            }
        });
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
            Chunk newChunk = WorldGenerator.generateChunk(chunkX, chunkY, CHUNK_SIZE, Config.chunkHeight);
            savedChunks.put(chunkKey, newChunk);
            worldData.addChunk(chunkX, chunkY, newChunk);
        }

        addWorldMesh();
    }
}
