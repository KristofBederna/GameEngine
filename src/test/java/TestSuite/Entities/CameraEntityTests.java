package TestSuite.Entities;

import inf.elte.hu.gameengine_javafx.Components.Default.PositionComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CameraEntityTests {
    private CameraEntity cameraEntity;

    @BeforeEach
    public void setUp() {
        cameraEntity = CameraEntity.getInstance(800, 600, 1600, 1200);
    }

    @Test
    public void testSingletonInstance() {
        CameraEntity camera1 = CameraEntity.getInstance(800, 600, 1600, 1200);
        CameraEntity camera2 = CameraEntity.getInstance();

        assertSame(camera1, camera2, "Camera should be a singleton instance.");
    }

    @Test
    public void testSetPosition() {
        cameraEntity.setPosition(100, 150);
        double x = cameraEntity.getComponent(PositionComponent.class).getGlobalX();
        double y = cameraEntity.getComponent(PositionComponent.class).getGlobalY();

        assertEquals(100, x, "X position should be set to 100");
        assertEquals(150, y, "Y position should be set to 150");
    }

    @Test
    public void testSetClampedPosition() {
        cameraEntity.setClampedPosition(1700, 1300);

        double x = cameraEntity.getComponent(PositionComponent.class).getGlobalX();
        double y = cameraEntity.getComponent(PositionComponent.class).getGlobalY();

        assertEquals(800, x, "X position should be clamped to the world width (1600)");
        assertEquals(600, y, "Y position should be clamped to the world height (1200)");
    }

    @Test
    public void testIsPositionInsideViewport() {
        double entityX = 100, entityY = 100, entityWidth = 50, entityHeight = 50;

        boolean insideViewport = cameraEntity.isPositionInsideViewport(entityX, entityY, entityWidth, entityHeight);
        assertTrue(insideViewport, "The entity should be inside the viewport.");

        double outOfBoundsX = 2000, outOfBoundsY = 100;
        boolean outsideViewport = cameraEntity.isPositionInsideViewport(outOfBoundsX, outOfBoundsY, entityWidth, entityHeight);
        assertFalse(outsideViewport, "The entity should be outside the viewport.");
    }

    @Test
    public void testGetRenderX() {
        cameraEntity.setPosition(100, 100);

        double entityX = 250;
        double renderX = CameraEntity.getRenderX(entityX);
        assertEquals(150, renderX, "Render X should account for the camera's global X position.");
    }

    @Test
    public void testGetRenderY() {
        cameraEntity.setPosition(100, 100);

        double entityY = 250;
        double renderY = CameraEntity.getRenderY(entityY);
        assertEquals(150, renderY, "Render Y should account for the camera's global Y position.");
    }

    @Test
    public void testAttachTo() {
        Entity childEntity = new TestEntity();
        cameraEntity.attachTo(childEntity);

        Entity attachedEntity = cameraEntity.getOwner();

        assertNotNull(attachedEntity, "Camera should have a child entity attached.");
        assertEquals(childEntity, attachedEntity, "Attached entity should match the child.");
    }

    @Test
    public void testResetInstance() {
        CameraEntity camera1 = CameraEntity.getInstance(800, 600, 1600, 1200);
        CameraEntity.resetInstance();
        CameraEntity camera2 = CameraEntity.getInstance(800, 600, 1600, 1200);

        assertNotSame(camera1, camera2, "Resetting the instance should create a new camera entity.");
    }
}
