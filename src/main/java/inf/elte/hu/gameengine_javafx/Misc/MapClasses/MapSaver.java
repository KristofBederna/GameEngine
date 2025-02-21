package inf.elte.hu.gameengine_javafx.Misc.MapClasses;
import inf.elte.hu.gameengine_javafx.Components.WorldDataComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldDimensionComponent;
import inf.elte.hu.gameengine_javafx.Entities.WorldEntity;
import inf.elte.hu.gameengine_javafx.Misc.Globals;

import java.io.*;

public class MapSaver {
    public static void saveMap(WorldEntity map, String path, TileLoader tileLoader) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(map.getComponent(WorldDimensionComponent.class).getWorldWidth()/ Globals.tileSize + " " + map.getComponent(WorldDimensionComponent.class).getWorldHeight()/ Globals.tileSize);
            writer.newLine();

            for (int y = 0; y < map.getComponent(WorldDimensionComponent.class).getWorldHeight()/Globals.tileSize; y++) {
                for (int x = 0; x < map.getComponent(WorldDimensionComponent.class).getWorldWidth()/Globals.tileSize; x++) {
                    writer.write(map.getComponent(WorldDataComponent.class).getElement(x, y).getValue() + " ");
                }
                writer.newLine();
            }
        }
    }
}
