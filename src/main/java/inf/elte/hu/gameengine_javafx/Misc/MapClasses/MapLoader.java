package inf.elte.hu.gameengine_javafx.Misc.MapClasses;

import inf.elte.hu.gameengine_javafx.Entities.TileEntity;

import java.io.*;
import java.util.Objects;

public class MapLoader {
    public static GameMap loadMap(String path, int tileSize, TileLoader tileLoader) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(MapLoader.class.getResourceAsStream(path))))) {
            String[] dimensions = reader.readLine().split(" ");
            int width = Integer.parseInt(dimensions[0]);
            int height = Integer.parseInt(dimensions[1]);

            GameMap map = new GameMap(width, height);

            for (int y = 0; y < height; y++) {
                String[] row = reader.readLine().split(" ");
                for (int x = 0; x < width; x++) {
                    int value = Integer.parseInt(row[x]);
                    String name = tileLoader.getTilePath(value);
                    if (name == null) {
                        name = String.valueOf(value);
                    }
                    if (value == 9) {
                        map.setElement(x, y, new TileEntity(value,x*tileSize, y*tileSize, "/assets/tiles/" + name+".png", tileSize, tileSize));
                    } else {
                        map.setElement(x, y, new TileEntity(value,x*tileSize, y*tileSize, "/assets/tiles/" + name+".png", tileSize, tileSize, true));
                    }
                }
            }

            return map;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveMap(GameMap map, String path, TileLoader tileLoader) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(map.getWidth() + " " + map.getHeight());
            writer.newLine();

            for (int y = 0; y < map.getHeight(); y++) {
                for (int x = 0; x < map.getWidth(); x++) {
                    writer.write(map.getElement(x, y).getValue() + " ");
                }
                writer.newLine();
            }
        }
    }
}
