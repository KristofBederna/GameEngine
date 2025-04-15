package TestSuite.Entities;

import TestSuite.Components.TestComponent;
import inf.elte.hu.gameengine_javafx.Components.Default.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.DimensionComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ImageComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.ResourceHub;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import inf.elte.hu.gameengine_javafx.Entities.PlayerEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntityHubTests {
    @Test
    public void testSingletonInstance() {
        EntityHub entityHub = EntityHub.getInstance();
        EntityHub anotherInstance = EntityHub.getInstance();
        assertSame(entityHub, anotherInstance);
    }

    @Test
    void testIsInManager() {
        Entity e = new TestEntity();

        assertEquals(e, EntityHub.getInstance().getEntityManager(TestEntity.class).get(e.getId()));
    }

    @Test
    void testIsInCache() {
        Entity e = new TestEntity();
        e.addComponent(new TestComponent());
        assertTrue(EntityHub.getInstance().getComponentCache().get(TestComponent.class).contains(e.getId()));
    }

    @Test
    void testIsInsideViewport() {
        Entity e = new TestEntity();
        e.getComponent(PositionComponent.class).setLocal(new Point(50, 50), e);
        e.addComponent(new DimensionComponent(10, 10));
        e.addComponent(new ImageComponent(""));

        CameraEntity.getInstance(1920, 1080);

        assertTrue(EntityHub.getInstance().getEntitiesInsideViewport(CameraEntity.getInstance()).contains(e));
    }

    @Test
    void testIsNotInsideViewport1() {
        Entity e = new TestEntity();
        e.getComponent(PositionComponent.class).setLocal(new Point(-11, -11), e);
        e.addComponent(new DimensionComponent(10, 10));
        e.addComponent(new ImageComponent(""));

        CameraEntity.getInstance(1920, 1080);

        assertFalse(EntityHub.getInstance().getEntitiesInsideViewport(CameraEntity.getInstance()).contains(e));
    }

    @Test
    void testIsNotInsideViewport2() {
        Entity e = new TestEntity();
        e.getComponent(PositionComponent.class).setLocal(new Point(-10, -10), e);
        e.addComponent(new DimensionComponent(10, 10));
        e.addComponent(new ImageComponent(""));

        CameraEntity.getInstance(1920, 1080);

        assertTrue(EntityHub.getInstance().getEntitiesInsideViewport(CameraEntity.getInstance()).contains(e));
    }

    @Test
    void testIsNotInsideViewport3() {
        Entity e = new TestEntity();
        e.getComponent(PositionComponent.class).setLocal(new Point(1921, 1081), e);
        e.addComponent(new DimensionComponent(10, 10));
        e.addComponent(new ImageComponent(""));

        CameraEntity.getInstance(1920, 1080);

        assertFalse(EntityHub.getInstance().getEntitiesInsideViewport(CameraEntity.getInstance()).contains(e));
    }

    @Test
    void testEntitiesWithType() {
        Entity e = new TestEntity();

        assertTrue(EntityHub.getInstance().getEntitiesWithType(TestEntity.class).contains(e));
    }

    @Test
    void testEntitiesWithType2() {
        Entity e = new TestEntity();

        assertFalse(EntityHub.getInstance().getEntitiesWithType(PlayerEntity.class).contains(e));
    }

    @Test
    void testRemoveEntity() {
        Entity e = new TestEntity();
        e.addComponent(new TestComponent());

        EntityHub entityHub = EntityHub.getInstance();

        entityHub.removeEntity(e);

        assertTrue(!entityHub.getEntitiesWithType(TestEntity.class).contains(e)
                && !entityHub.getEntitiesWithComponent(TestComponent.class).contains(e)
                && !entityHub.getComponentCache().get(TestComponent.class).contains(e.getId())
                && !entityHub.getEntityManager(TestEntity.class).getEntities().containsValue(e)
        );
    }
}
