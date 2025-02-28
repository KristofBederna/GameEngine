package inf.elte.hu.gameengine_javafx.Systems.ResourceSystems;

import inf.elte.hu.gameengine_javafx.Components.*;
import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.RectangularHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.CentralMassComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.DimensionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ImageComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.MapMeshComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.TileSetComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.WorldDataComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.EntityManager;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import inf.elte.hu.gameengine_javafx.Entities.TileEntity;
import inf.elte.hu.gameengine_javafx.Entities.WorldEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.MapSaver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class WorldLoaderSystem extends GameSystem {
    @Override
    public void start() {
        this.active = true;
        WorldEntity map = WorldEntity.getInstance();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(MapSaver.class.getResourceAsStream(map.getComponent(FilePathComponent.class).getFilePath()))))) {
            String[] dimensions = reader.readLine().split(" ");
            int width = Integer.parseInt(dimensions[0]);
            int height = Integer.parseInt(dimensions[1]);

            for (int y = 0; y < height; y++) {
                String[] row = reader.readLine().split(" ");
                List<TileEntity> worldRow = new ArrayList<>();
                List<Point> meshRow = new ArrayList<>();
                for (int x = 0; x < width; x++) {
                    int value = Integer.parseInt(row[x]);
                    String name = map.getComponent(TileSetComponent.class).getTileLoader().getTilePath(value);
                    TileEntity tile;
                    if (name == null) {
                        name = String.valueOf(value);
                    }
                    if (value == 9) {
                        tile = new TileEntity(value,x* Globals.tileSize, y*Globals.tileSize, "/assets/tiles/" + name+".png", Globals.tileSize, Globals.tileSize);
                    } else {
                        tile = new TileEntity(value,x*Globals.tileSize, y*Globals.tileSize, "/assets/tiles/" + name+".png", Globals.tileSize, Globals.tileSize, true);
                    }
                    worldRow.add(tile);
                    meshRow.add(new Point(tile.getComponent(CentralMassComponent.class).getCentralX(), tile.getComponent(CentralMassComponent.class).getCentralY()));
                }
                map.getComponent(MapMeshComponent.class).addRow(meshRow);
                map.getComponent(WorldDataComponent.class).addRow(worldRow);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update() {
        CameraEntity camera = CameraEntity.getInstance();
        WorldEntity map = WorldEntity.getInstance();
        if (map == null) return;
        if (camera == null) return;

        double camX = camera.getComponent(PositionComponent.class).getGlobalX();
        double camY = camera.getComponent(PositionComponent.class).getGlobalY();
        double camWidth = camera.getComponent(DimensionComponent.class).getWidth();
        double camHeight = camera.getComponent(DimensionComponent.class).getHeight();

        EntityManager<TileEntity> tileManager = EntityHub.getInstance().getEntityManager(TileEntity.class);
        List<TileEntity> toRemove = new ArrayList<>();

        for (TileEntity tile : tileManager.getEntities().values()) {
            double tileX = tile.getComponent(PositionComponent.class).getGlobalX();
            double tileY = tile.getComponent(PositionComponent.class).getGlobalY();

            if (tileX + Globals.tileSize < camX || tileX > camX + camWidth ||
                    tileY + Globals.tileSize < camY || tileY > camY + camHeight) {
                toRemove.add(tile);
            }
        }

        for (TileEntity tile : toRemove) {
            tileManager.unload(tile.getId());
        }

        List<List<TileEntity>> worldData = map.getComponent(WorldDataComponent.class).getMapData();
        Set<String> existingTiles = tileManager.getEntities().values().stream()
                .map(t -> t.getComponent(PositionComponent.class).getGlobalX() + "," +
                        t.getComponent(PositionComponent.class).getGlobalY())
                .collect(Collectors.toSet());

        for (List<TileEntity> row : worldData) {
            for (TileEntity data : row) {
                double tileX = data.getComponent(PositionComponent.class).getGlobalX();
                double tileY = data.getComponent(PositionComponent.class).getGlobalY();

                if (tileX + Globals.tileSize >= camX && tileX <= camX + camWidth &&
                        tileY + Globals.tileSize >= camY && tileY <= camY + camHeight) {

                    String key = tileX + "," + tileY;
                    if (!existingTiles.contains(key)) {
                        boolean hasHitBox = data.getComponent(RectangularHitBoxComponent.class) != null;
                        TileEntity newTile = new TileEntity(data.getValue(), tileX, tileY,
                                data.getComponent(ImageComponent.class).getImagePath(),
                                Globals.tileSize, Globals.tileSize, hasHitBox);
                        tileManager.register(newTile);
                    }
                }
            }
        }
    }
}
