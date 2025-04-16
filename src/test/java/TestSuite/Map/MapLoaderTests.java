package TestSuite.Map;

import inf.elte.hu.gameengine_javafx.Components.TileValueComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.WorldDataComponent;
import inf.elte.hu.gameengine_javafx.Entities.WorldEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Misc.Configs.MapConfig;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.World;
import inf.elte.hu.gameengine_javafx.Systems.ResourceSystems.WorldLoaderSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MapLoaderTests {
    World world;

    @BeforeEach
    public void setUp() {
        WorldEntity.getInstance(32, 16, "/assets/maps/hardForAIMap.txt", "/assets/tileSets/testTiles.txt");
        WorldLoaderSystem system = new WorldLoaderSystem();
        system.start();
        world = WorldEntity.getInstance().getComponent(WorldDataComponent.class).getMapData();
    }

    @Test
    public void testLoadMap() {
        assertNotNull(world);
    }

    @Test
    public void testChunkSize() {
        assertEquals(2, world.getWorld().size()); //Since chunks are 16x16
    }

    @Test
    public void testLoadCorrectTiles() {
        assertEquals(9, world.getElementAt(new Point(MapConfig.scaledTileSize, MapConfig.scaledTileSize)).getComponent(TileValueComponent.class).getTileValue());
        assertEquals(3, world.getElementAt(new Point(0, 0)).getComponent(TileValueComponent.class).getTileValue());
        assertEquals(2, world.getElementAt(new Point(MapConfig.scaledTileSize*31, MapConfig.scaledTileSize*15)).getComponent(TileValueComponent.class).getTileValue());
    }

    @Test
    public void testSetTileAlignment() {
        world.setElementAt(new Point(0, 0), 9);
        assertEquals(9, world.getElementAt(new Point(0, 0)).getComponent(TileValueComponent.class).getTileValue());
    }
}
