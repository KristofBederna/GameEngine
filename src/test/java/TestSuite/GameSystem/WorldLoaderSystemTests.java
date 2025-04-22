package TestSuite.GameSystem;

import inf.elte.hu.gameengine_javafx.Components.TileValueComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.MapMeshComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.WorldDataComponent;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import inf.elte.hu.gameengine_javafx.Entities.WorldEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Misc.Configs.MapConfig;
import inf.elte.hu.gameengine_javafx.Systems.ResourceSystems.MapLoaderSystems.WorldLoaderSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WorldLoaderSystemTests {
    @BeforeEach
    public void setUp() {
        EntityHub.resetInstance();
        WorldEntity.resetInstance();
        WorldEntity.getInstance(32, 16, "/assets/maps/hardForAIMap.txt", "/assets/tileSets/testTiles.txt");
    }

    @Test
    public void testWorldIsFullyLoadedOnStart() {
        WorldLoaderSystem system = new WorldLoaderSystem();
        system.start();
        WorldDataComponent mapData = WorldEntity.getInstance().getComponent(WorldDataComponent.class);
        int expectedChunkCount = 2; // 2x1 chunks
        assertEquals(expectedChunkCount, mapData.getMapData().getWorld().size());
    }

    @Test
    public void testChunksAreSavedOnUnload() {
        CameraEntity.getInstance(100, 100, 3200, 3200);
        WorldLoaderSystem system = new WorldLoaderSystem();
        system.start();
        system.update();

        WorldEntity.getInstance().getComponent(WorldDataComponent.class).getMapData().getWorld().clear();
        system.update();

        int savedChunkCount = WorldEntity.getInstance().getComponent(WorldDataComponent.class).getMapData().getSavedChunks().size();
        assertEquals(2, savedChunkCount);
    }

    @Test
    public void testWorldMeshDoesNotContainNullsForWalkableTiles() {
        WorldLoaderSystem system = new WorldLoaderSystem();
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
    public void testUnloadedChunksAreRemovedFromWorld() {
        CameraEntity.getInstance(100, 100, 3200, 1600);

        MapConfig.loadDistance = 0;
        WorldLoaderSystem system = new WorldLoaderSystem();
        system.start();
        system.update();

        assertEquals(1, WorldEntity.getInstance().getComponent(WorldDataComponent.class).getMapData().getWorld().size());
        assertEquals(2, WorldEntity.getInstance().getComponent(WorldDataComponent.class).getMapData().getSavedChunks().size());
    }
}
