package TestSuite.Entities;

import inf.elte.hu.gameengine_javafx.Components.HitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ImageComponent;
import inf.elte.hu.gameengine_javafx.Components.TileValueComponent;
import inf.elte.hu.gameengine_javafx.Entities.TileEntity;
import inf.elte.hu.gameengine_javafx.Misc.Configs.MapConfig;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.TileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TileEntityTests {

    private TileEntity tileEntity;

    @BeforeEach
    public void setUp() {
        tileEntity = new TileEntity(9, 100, 100, "/assets/tiles/windowWall.png", 50, 50);
    }

    @Test
    public void testAddHitBox() {
        tileEntity.addHitBox(100, 100, 50, 50);
        assertNotNull(tileEntity.getComponent(HitBoxComponent.class), "HitBoxComponent should be added when addHitBox is called.");
    }

    @Test
    public void testChangeValuesForWallTile() {
        MapConfig.wallTiles.add(1);
        tileEntity.changeValues(1);

        assertEquals(1, tileEntity.getComponent(TileValueComponent.class).getTileValue(), "Tile value should be updated.");

        String expectedImagePath = "/assets/tiles/" + TileLoader.getTilePath(1) + ".png";
        assertEquals(expectedImagePath, tileEntity.getComponent(ImageComponent.class).getImagePath(), "Image path should match the expected path.");

        assertNotNull(tileEntity.getComponent(HitBoxComponent.class), "HitBoxComponent should be added for wall tiles.");
    }

    @Test
    public void testChangeValuesForNonWallTile() {
        MapConfig.wallTiles.add(1);
        tileEntity.changeValues(9);

        assertEquals(9, tileEntity.getComponent(TileValueComponent.class).getTileValue(), "Tile value should be updated.");

        String expectedImagePath = "/assets/tiles/" + TileLoader.getTilePath(9) + ".png";
        assertEquals(expectedImagePath, tileEntity.getComponent(ImageComponent.class).getImagePath(), "Image path should match the expected path.");

        assertNull(tileEntity.getComponent(HitBoxComponent.class), "HitBoxComponent should be removed for non-wall tiles.");
    }

    @Test
    public void testChangeValuesWithoutHitBox() {
        TileEntity tileWithoutHitBox = new TileEntity(9, 100, 100, "/assets/tiles/windowWall.png", 50, 50);

        assertNull(tileWithoutHitBox.getComponent(HitBoxComponent.class), "HitBoxComponent should not be present for this tile.");
    }

    @Test
    public void testHitBoxComponentForWallTile() {
        MapConfig.wallTiles.add(1);

        TileEntity wallTileEntity = new TileEntity(1, 100, 100, "/assets/tiles/default.png", 50, 50);

        assertNotNull(wallTileEntity.getComponent(HitBoxComponent.class), "A wall tile should have a HitBoxComponent.");
    }
}
