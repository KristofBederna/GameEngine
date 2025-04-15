package TestSuite.Entities;

import TestSuite.Components.TestComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.EntityManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntityManagerTests {
    @Test
    public void testGet() {
        Entity e = new TestEntity();

        EntityManager<TestEntity> em = EntityHub.getInstance().getEntityManager(TestEntity.class);

        assertEquals(em.get(e.getId()), e);
    }

    @Test
    public void testRegister() {
        Entity e = new TestEntity(); //This automatically calls entity manager's register function via the addToManager function
        e.addComponent(new TestComponent());

        EntityHub eh = EntityHub.getInstance();
        EntityManager<TestEntity> em = EntityHub.getInstance().getEntityManager(TestEntity.class);
        assertTrue(em.getEntities().containsValue(e)
        && eh.getComponentCache().get(TestComponent.class).contains(e.getId())
        && eh.getAllEntities().contains(e)
        );
    }

    @Test
    public void testUnload() {
        Entity e = new TestEntity();
        EntityManager<TestEntity> em = EntityHub.getInstance().getEntityManager(TestEntity.class);

        em.unload(e.getId());

        assertFalse(em.getEntities().containsValue(e));
    }
}
