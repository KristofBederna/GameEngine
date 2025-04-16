package TestSuite.Map;

import inf.elte.hu.gameengine_javafx.Components.Default.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ImageComponent;
import inf.elte.hu.gameengine_javafx.Components.TileValueComponent;
import inf.elte.hu.gameengine_javafx.Entities.TileEntity;
import inf.elte.hu.gameengine_javafx.Misc.Configs.MapConfig;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.Chunk;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.WorldGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapGeneratorTests {
        @BeforeEach
        public void setup() {
            MapConfig.scaledTileSize = 32;
            MapConfig.defaultTileCode = 0;
        }

        @Test
        public void testGeneratedChunkSize() {
            int chunkWidth = 4;
            int chunkHeight = 3;
            Chunk chunk = WorldGenerator.generateChunk(0, 0, chunkWidth, chunkHeight);

            List<List<TileEntity>> tiles = chunk.getChunk();
            assertEquals(chunkHeight, tiles.size(), "Chunk row count should match chunkHeight");

            for (List<TileEntity> row : tiles) {
                assertEquals(chunkWidth, row.size(), "Each row in chunk should match chunkWidth");
            }
        }

        @Test
        public void testDefaultTileValues() {
            MapConfig.defaultTileCode = 7;

            Chunk chunk = WorldGenerator.generateChunk(0, 0, 2, 2);
            List<List<TileEntity>> tiles = chunk.getChunk();

            for (List<TileEntity> row : tiles) {
                for (TileEntity tile : row) {
                    assertEquals(7, tile.getComponent(TileValueComponent.class).getTileValue(), "Tile value should match MapConfig.defaultTileCode");
                }
            }
        }

        @Test
        public void testTileWorldCoordinates() {
            int chunkX = 0;
            int chunkY = 0;
            int chunkWidth = 2;
            int chunkHeight = 2;
            MapConfig.scaledTileSize = 16;

            Chunk chunk = WorldGenerator.generateChunk(chunkX, chunkY, chunkWidth, chunkHeight);
            List<List<TileEntity>> tiles = chunk.getChunk();

            assertEquals(0, tiles.get(0).getFirst().getComponent(PositionComponent.class).getGlobalX(), "First tile X should be correct based on chunk position");
            assertEquals(0, tiles.get(0).getFirst().getComponent(PositionComponent.class).getGlobalY(), "First tile Y should be correct based on chunk position");
            assertEquals(16, tiles.get(1).get(1).getComponent(PositionComponent.class).getGlobalX(), "Last tile X should be correct");
            assertEquals(16, tiles.get(1).get(1).getComponent(PositionComponent.class).getGlobalY(), "Last tile Y should be correct");
        }

    @Test
    public void testTileWorldCoordinates2() {
        int chunkX = 100;
        int chunkY = 5000;
        int chunkWidth = 160;
        int chunkHeight = 16;
        MapConfig.scaledTileSize = 137;

        Chunk chunk = WorldGenerator.generateChunk(chunkX, chunkY, chunkWidth, chunkHeight);
        List<List<TileEntity>> tiles = chunk.getChunk();

        assertEquals(0+chunkX*chunkWidth*MapConfig.scaledTileSize, tiles.get(0).getFirst().getComponent(PositionComponent.class).getGlobalX(), "First tile X should be correct based on chunk position");
        assertEquals(0+chunkY*chunkHeight*MapConfig.scaledTileSize, tiles.get(0).getFirst().getComponent(PositionComponent.class).getGlobalY(), "First tile Y should be correct based on chunk position");
        assertEquals(MapConfig.scaledTileSize+chunkX*chunkWidth*MapConfig.scaledTileSize, tiles.get(1).get(1).getComponent(PositionComponent.class).getGlobalX(), "Last tile X should be correct");
        assertEquals(MapConfig.scaledTileSize+chunkY*chunkHeight*MapConfig.scaledTileSize, tiles.get(1).get(1).getComponent(PositionComponent.class).getGlobalY(), "Last tile Y should be correct");
    }

        @Test
        public void testFallbackTilePath() {
            // MapConfig.defaultTileCode is 0, assume it doesn't map to a valid path
            MapConfig.defaultTileCode = 123456;

            Chunk chunk = WorldGenerator.generateChunk(0, 0, 1, 1);
            TileEntity tile = chunk.getChunk().get(0).get(0);

            assertTrue(tile.getComponent(ImageComponent.class).getImagePath().contains("default.png"), "Image path should fall back to default.png");
        }
}
