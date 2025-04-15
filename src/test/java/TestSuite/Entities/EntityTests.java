package TestSuite.Entities;

import TestSuite.Components.TestComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTests {
    @Test
    void testAddAndGetComponent() {
        Entity entity = new TestEntity();
        TestComponent testComponent = new TestComponent();
        entity.addComponent(testComponent);

        TestComponent result = entity.getComponent(TestComponent.class);
        assertSame(testComponent, result);
    }

    @Test
    void testRemoveComponent() {
        Entity entity = new TestEntity();
        entity.addComponent(new TestComponent());
        entity.removeComponentsByType(TestComponent.class);

        assertNull(entity.getComponent(TestComponent.class));
    }

    @Test
    void testEntityIdIsUnique() {
        Entity e1 = new TestEntity();
        Entity e2 = new TestEntity();

        assertNotEquals(e1.getId(), e2.getId());
    }
}
