package inf.elte.hu.gameengine_javafx.Misc.MapClasses;

import inf.elte.hu.gameengine_javafx.Components.FilePathComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.CentralMassComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.MapMeshComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.TileSetComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.WorldDataComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.WorldDimensionComponent;
import inf.elte.hu.gameengine_javafx.Entities.TileEntity;
import inf.elte.hu.gameengine_javafx.Entities.WorldEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Misc.Config;
import inf.elte.hu.gameengine_javafx.Misc.Tuple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MapLoader {
    public static void loadMap() {
        ArrayList<ArrayList<Integer>> data = new ArrayList<>();
        int width, height;

        // Get the instance of the world entity
        WorldEntity map = WorldEntity.getInstance();
        if (map == null) {
            return;
        }

        // Read map data from file
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(MapSaver.class.getResourceAsStream(map.getComponent(FilePathComponent.class).getFilePath()))))) {
            String line;
            String[] dimensions = reader.readLine().split(" ");
            width = Integer.parseInt(dimensions[0]);
            height = Integer.parseInt(dimensions[1]);
            WorldEntity.getInstance().getComponent(WorldDimensionComponent.class).setWorldHeight(height);
            WorldEntity.getInstance().getComponent(WorldDimensionComponent.class).setWorldWidth(width);
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(" ");
                ArrayList<Integer> row = new ArrayList<>();
                for (String value : values) {
                    row.add(Integer.parseInt(value));
                }
                data.add(row);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }

        // Divide map into chunks
        int chunkWidth = Math.floorDiv(width, Config.chunkWidth);
        int chunkHeight = Math.floorDiv(height, Config.chunkHeight);
        for (int i = 0; i < chunkWidth; i++) {
            for (int j = 0; j < chunkHeight; j++) {
                Tuple<Integer, Integer> coordinates = new Tuple(i, j);
                Chunk chunkTiles = new Chunk();
                for (int y = j * Config.chunkHeight; y < height; y++) {
                    List<TileEntity> chunkRow = new ArrayList<>();
                    List<Point> meshRow = new ArrayList<>();
                    for (int x = i * Config.chunkWidth; x < width; x++) {
                        int value = data.get(y).get(x);
                        String name = TileLoader.getTilePath(value);
                        TileEntity tile;
                        if (name == null) {
                            name = String.valueOf(value);
                        }
                        if (!Config.wallTiles.contains(value)) {
                            tile = new TileEntity(value, x * Config.scaledTileSize, y * Config.scaledTileSize, "/assets/tiles/" + name + ".png", Config.scaledTileSize, Config.scaledTileSize);
                            meshRow.add(new Point(tile.getComponent(CentralMassComponent.class).getCentralX(), tile.getComponent(CentralMassComponent.class).getCentralY()));
                        } else {
                            tile = new TileEntity(value, x * Config.scaledTileSize, y * Config.scaledTileSize, "/assets/tiles/" + name + ".png", Config.scaledTileSize, Config.scaledTileSize, true);
                            meshRow.add(null);
                        }
                        chunkRow.add(tile);
                    }
                    chunkTiles.getChunk().add(chunkRow);
                    map.getComponent(MapMeshComponent.class).addRow(meshRow);
                }
                map.getComponent(WorldDataComponent.class).getMapData().getWorld().putIfAbsent(coordinates, chunkTiles);
            }
        }
    }
}
