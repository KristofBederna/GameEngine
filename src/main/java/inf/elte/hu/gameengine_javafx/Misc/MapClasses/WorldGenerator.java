package inf.elte.hu.gameengine_javafx.Misc.MapClasses;

import inf.elte.hu.gameengine_javafx.Components.WorldComponents.TileSetComponent;
import inf.elte.hu.gameengine_javafx.Entities.TileEntity;
import inf.elte.hu.gameengine_javafx.Entities.WorldEntity;
import inf.elte.hu.gameengine_javafx.Misc.Config;

import java.util.ArrayList;
import java.util.List;

public class WorldGenerator {
    public static Chunk generateChunk(int chunkX, int chunkY, int chunkWidth, int chunkHeight) {
        List<List<TileEntity>> tiles = new ArrayList<>();
        TileSetComponent tileSet = WorldEntity.getInstance().getComponent(TileSetComponent.class);

        int[][] tileValues = new int[chunkWidth][chunkHeight];

        for (int y = 0; y < chunkHeight; y++) {
            for (int x = 0; x < chunkWidth; x++) {
                tileValues[y][x] = getNextTileValue(x, y, tileValues, chunkWidth, chunkHeight);
            }
        }

        for (int y = 0; y < chunkHeight; y++) {
            List<TileEntity> row = new ArrayList<>();
            for (int x = 0; x < chunkWidth; x++) {
                int worldX = chunkX * chunkWidth * Config.tileSize + x * Config.tileSize;
                int worldY = chunkY * chunkHeight * Config.tileSize + y * Config.tileSize;

                int tileValue = tileValues[y][x];
                String tilePath = tileSet.getTileLoader().getTilePath(tileValue);
                if (tilePath == null) {
                    tilePath = "default.png";
                }

                TileEntity tile = new TileEntity(tileValue, worldX, worldY, "/assets/tiles/" + tilePath + ".png", Config.tileSize, Config.tileSize, tileValue != 9);
                row.add(tile);
            }
            tiles.add(row);
        }

        return new Chunk(tiles);
    }

    private static int getNextTileValue(int x, int y, int[][] tileValues, int chunkWidth, int chunkHeight) {
        return 9;
    }
}
