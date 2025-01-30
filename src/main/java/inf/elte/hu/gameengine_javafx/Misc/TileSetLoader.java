package inf.elte.hu.gameengine_javafx.Misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class TileSetLoader {
    public static void loadSet(String path, TileLoader tileLoader) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(MapLoader.class.getResourceAsStream(path))))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(" ");
                int value = Integer.parseInt(row[0]);
                String tilePath = row[1];

                tileLoader.addTilePath(value, tilePath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void loadSet(String path, TileLoader tileLoader, String separator) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(MapLoader.class.getResourceAsStream(path))))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(separator);
                int value = Integer.parseInt(row[0]);
                String tilePath = row[1];

                tileLoader.addTilePath(value, tilePath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
