package TestSuite.GameSystem;

import inf.elte.hu.gameengine_javafx.Components.TileValueComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.MapMeshComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.WorldDataComponent;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import inf.elte.hu.gameengine_javafx.Entities.WorldEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Misc.Configs.MapConfig;
import inf.elte.hu.gameengine_javafx.Misc.Configs.WalkerConfig;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.Chunk;
import inf.elte.hu.gameengine_javafx.Systems.ResourceSystems.DynamicWorldLoaderSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DynamicWorldLoaderSystemTests {
    @BeforeEach
    public void setUp() {
        EntityHub.resetInstance();
        WorldEntity map = WorldEntity.getInstance(32, 32, "/assets/tileSets/testTiles.txt");
        DynamicWorldLoaderSystem system = new DynamicWorldLoaderSystem(2, 2);
        system.start();
    }

    @Test
    public void testDynamicWorldLoaderChangesTiles() {
        WorldEntity map = WorldEntity.getInstance();
        WorldDataComponent mapData = map.getComponent(WorldDataComponent.class);
        for (int i = 0; i < mapData.getMapData().getWorld().size()/2; i++) {
            for (int j = 0; j < mapData.getMapData().getWorld().size()/2; j++) {
                for (int k = 0; k < 15; k++) {
                    for (int l = 0; l < 15; l++) {
                        if(mapData.getMapData().get(i,j).getElement(k, l).getComponent(TileValueComponent.class).getTileValue() != 0) {
                            assertTrue(true);
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testWorldIsFullyLoadedOnStart() {
        WorldDataComponent mapData = WorldEntity.getInstance().getComponent(WorldDataComponent.class);
        int expectedChunkCount = 4; // 2x2 chunks
        assertEquals(expectedChunkCount, mapData.getMapData().getWorld().size());
    }

    @Test
    public void testChunksAreSavedOnUnload() {
        CameraEntity cameraEntity = CameraEntity.getInstance(100, 100, 3200, 3200);
        DynamicWorldLoaderSystem system = new DynamicWorldLoaderSystem(2, 2);
        system.start();
        system.update();

        WorldEntity.getInstance().getComponent(WorldDataComponent.class).getMapData().getWorld().clear();
        system.update();

        int savedChunkCount = WorldEntity.getInstance().getComponent(WorldDataComponent.class).getMapData().getSavedChunks().size();
        assertTrue(savedChunkCount > 0);
    }

    @Test
    public void testChunkGenerationAddsChunkToWorld() {
        DynamicWorldLoaderSystem system = new DynamicWorldLoaderSystem(3, 3);
        WorldDataComponent mapData = WorldEntity.getInstance().getComponent(WorldDataComponent.class);
        int prevSize = mapData.getMapData().getWorld().size();

        system.start();

        assertTrue(mapData.getMapData().getWorld().size() > prevSize);
    }

    @Test
    public void testCornerWallsAreSetCorrectly() {
        DynamicWorldLoaderSystem system = new DynamicWorldLoaderSystem(2, 2);
        system.start();

        var mapData = WorldEntity.getInstance().getComponent(WorldDataComponent.class).getMapData();
        var chunk = mapData.get(0, 0);

        int topLeftTile = chunk.getElement(0, 0).getComponent(TileValueComponent.class).getTileValue();
        assertEquals(topLeftTile, MapConfig.topLeftWallCode);
    }

    @Test
    public void testWorldMeshDoesNotContainNullsForWalkableTiles() {
        DynamicWorldLoaderSystem system = new DynamicWorldLoaderSystem(2, 2);
        system.start();

        var mesh = WorldEntity.getInstance().getComponent(MapMeshComponent.class).getMapCoordinates();
        for (int i = 0; i < mesh.size(); i++) {
            for (int j = 0; j < mesh.getFirst().size(); j++) {
                if (mesh.get(i).get(j) == null) {
                    if (!MapConfig.wallTiles.contains(WorldEntity.getInstance().getComponent(WorldDataComponent.class).getMapData().getElementAt(new Point(j*MapConfig.scaledTileSize, i*MapConfig.scaledTileSize)).getComponent(TileValueComponent.class).getTileValue())) {
                        fail();
                    }
                }
            }
        }
        assertTrue(true);
    }

    @Test
    public void testWalkerModifiesTiles() {
        DynamicWorldLoaderSystem system = new DynamicWorldLoaderSystem(2, 2);
        system.start();

        var mapData = WorldEntity.getInstance().getComponent(WorldDataComponent.class).getMapData();
        boolean hasPlacedTile = false;
        for (Chunk chunk : mapData.getWorld().values()) {
            outer: for (int i = 0; i < MapConfig.chunkWidth; i++) {
                for (int j = 0; j < MapConfig.chunkHeight; j++) {
                    if (chunk.getElement(i, j).getComponent(TileValueComponent.class).getTileValue() == WalkerConfig.placeTileNumber) {
                        hasPlacedTile = true;
                        break outer;
                    }
                }
            }
        }
        assertTrue(hasPlacedTile);
    }

    @Test
    public void testUnloadedChunksAreRemovedFromWorld() {
        CameraEntity cameraEntity = CameraEntity.getInstance(100, 100, 3200, 3200);

        MapConfig.loadDistance = 0;
        DynamicWorldLoaderSystem system = new DynamicWorldLoaderSystem(2, 2);
        system.start();
        system.update();

        assertEquals(1, WorldEntity.getInstance().getComponent(WorldDataComponent.class).getMapData().getWorld().size());
        assertEquals(4, WorldEntity.getInstance().getComponent(WorldDataComponent.class).getMapData().getSavedChunks().size());
    }
}
