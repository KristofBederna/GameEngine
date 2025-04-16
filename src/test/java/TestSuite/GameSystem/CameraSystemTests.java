package TestSuite.GameSystem;

import TestSuite.Entities.TestEntity;
import inf.elte.hu.gameengine_javafx.Components.Default.ParentComponent;
import inf.elte.hu.gameengine_javafx.Components.Default.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.DimensionComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ImageComponent;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Systems.RenderingSystems.CameraSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CameraSystemTests {
    @Test
    public void testCameraSnapsToOwner() {
        TestEntity testEntity = new TestEntity();
        TestEntity testEntity2 = new TestEntity();

        CameraSystem cameraSystem = new CameraSystem();

        testEntity.getComponent(PositionComponent.class).setLocal(new Point(900, 900), testEntity);
        testEntity2.getComponent(PositionComponent.class).setLocal(new Point(500, 500), testEntity2);

        testEntity.addComponent(new ImageComponent("", 10, 10));
        testEntity2.addComponent(new ImageComponent("", 10, 10));

        CameraEntity cameraEntity = CameraEntity.getInstance(100, 100, 1000000, 1000000);
        cameraEntity.attachTo(testEntity);

        cameraSystem.start();
        cameraSystem.update();

        assertEquals(855, cameraEntity.getComponent(PositionComponent.class).getLocal().getX());

        cameraSystem.update();
        cameraEntity.attachTo(testEntity2);
        cameraSystem.update();

        assertEquals(455, cameraEntity.getComponent(PositionComponent.class).getLocal().getX());
    }
}
