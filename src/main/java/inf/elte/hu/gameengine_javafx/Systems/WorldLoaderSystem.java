package inf.elte.hu.gameengine_javafx.Systems;

import inf.elte.hu.gameengine_javafx.Components.FilePathComponent;
import inf.elte.hu.gameengine_javafx.Components.TileSetComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldDataComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Entities.TileEntity;
import inf.elte.hu.gameengine_javafx.Entities.WorldEntity;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.MapSaver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WorldLoaderSystem extends GameSystem {
    @Override
    public void start() {
        this.active = true;
        WorldEntity map = EntityHub.getInstance().getEntityManager(WorldEntity.class).getEntities().get(1);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(MapSaver.class.getResourceAsStream(map.getComponent(FilePathComponent.class).getFilePath()))))) {
            String[] dimensions = reader.readLine().split(" ");
            int width = Integer.parseInt(dimensions[0]);
            int height = Integer.parseInt(dimensions[1]);

            for (int y = 0; y < height; y++) {
                String[] row = reader.readLine().split(" ");
                List<TileEntity> worldRow = new ArrayList<>();
                for (int x = 0; x < width; x++) {
                    int value = Integer.parseInt(row[x]);
                    String name = map.getComponent(TileSetComponent.class).getTileLoader().getTilePath(value);
                    if (name == null) {
                        name = String.valueOf(value);
                    }
                    if (value == 9) {
                        worldRow.add(new TileEntity(value,x* Globals.tileSize, y*Globals.tileSize, "/assets/tiles/" + name+".png", Globals.tileSize, Globals.tileSize));
                    } else {
                        worldRow.add(new TileEntity(value,x*Globals.tileSize, y*Globals.tileSize, "/assets/tiles/" + name+".png", Globals.tileSize, Globals.tileSize, true));
                    }
                }
                map.getComponent(WorldDataComponent.class).addRow(worldRow);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update() {
        //Update the entities which are inside the viewport
    }
}
