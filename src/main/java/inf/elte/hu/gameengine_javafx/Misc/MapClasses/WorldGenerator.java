package inf.elte.hu.gameengine_javafx.Misc.MapClasses;

import inf.elte.hu.gameengine_javafx.Components.WorldComponents.TileSetComponent;
import inf.elte.hu.gameengine_javafx.Entities.TileEntity;
import inf.elte.hu.gameengine_javafx.Entities.WorldEntity;
import inf.elte.hu.gameengine_javafx.Misc.Globals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGenerator {
    private static final Random RANDOM = new Random();

    public static Chunk generateChunk(int chunkX, int chunkY, int chunkSize) {
        List<List<TileEntity>> tiles = new ArrayList<>();
        TileSetComponent tileSet = WorldEntity.getInstance().getComponent(TileSetComponent.class);

        int[][] tileValues = new int[chunkSize][chunkSize];

        for (int y = 0; y < chunkSize; y++) {
            for (int x = 0; x < chunkSize; x++) {
                tileValues[y][x] = getNextTileValue(x, y, tileValues, chunkSize);
            }
        }

        for (int y = 0; y < chunkSize; y++) {
            List<TileEntity> row = new ArrayList<>();
            for (int x = 0; x < chunkSize; x++) {
                int worldX = chunkX * chunkSize * Globals.tileSize + x * Globals.tileSize;
                int worldY = chunkY * chunkSize * Globals.tileSize + y * Globals.tileSize;

                int tileValue = tileValues[y][x];
                String tilePath = tileSet.getTileLoader().getTilePath(tileValue);
                if (tilePath == null) {
                    tilePath = "default.png";
                }

                TileEntity tile = new TileEntity(tileValue, worldX, worldY, "/assets/tiles/" + tilePath + ".png", Globals.tileSize, Globals.tileSize, tileValue != 9);
                row.add(tile);
            }
            tiles.add(row);
        }

        return new Chunk(tiles);
    }

    private static int getNextTileValue(int x, int y, int[][] tileValues, int chunkSize) {
        return 9;
    }
}
