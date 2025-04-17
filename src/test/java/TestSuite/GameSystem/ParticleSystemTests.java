package TestSuite.GameSystem;

import inf.elte.hu.gameengine_javafx.Components.Default.ParentComponent;
import inf.elte.hu.gameengine_javafx.Components.Default.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.MaxDistanceFromOriginComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.DirectionComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Entities.ParticleEmitterEntity;
import inf.elte.hu.gameengine_javafx.Entities.ParticleEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Rectangle;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Shape;
import inf.elte.hu.gameengine_javafx.Maths.Vector;
import inf.elte.hu.gameengine_javafx.Misc.Direction;
import inf.elte.hu.gameengine_javafx.Misc.Time;
import inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems.MovementSystem;
import inf.elte.hu.gameengine_javafx.Systems.RenderingSystems.ParticleSystem;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParticleSystemTests {
    @Test
    public void testInitializeParticleAccelerationForDirections() {
        Time.getInstance().setDeltaTime(1);
        ParticleSystem ps = new ParticleSystem();
        MovementSystem ms = new MovementSystem();
        Direction[] directions = Direction.values();
        ms.start();
        ps.start();

        for (Direction dir : directions) {
            new ParticleEmitterEntity(0, 0, new ParticleEntity(0, 0, 1, 1, new Rectangle(new Point(0, 0), 1, 1), Color.HONEYDEW, Color.BLACK, 100), dir, 5, 1000);
            ps.update();
            ms.update();

            List<Entity> particleEntities = EntityHub.getInstance().getEntitiesWithType(ParticleEntity.class);
            for (Entity particleEntity : particleEntities) {
                double dy = particleEntity.getComponent(PositionComponent.class).getLocalY();
                double dx = particleEntity.getComponent(PositionComponent.class).getLocalX();
                switch (dir) {
                    case UP -> assertTrue(dy < 0);
                    case DOWN -> assertTrue(dy > 0);
                    case LEFT -> assertTrue(dx < 0);
                    case RIGHT -> assertTrue(dx > 0);
                }
            }
            EntityHub.resetInstance();
        }
    }

    @Test
    public void testSpawnRate() {
        Time.getInstance().setDeltaTime(1);
        ParticleSystem ps = new ParticleSystem();
        MovementSystem ms = new MovementSystem();
        ms.start();
        new ParticleEmitterEntity(0, 0, new ParticleEntity(0, 0, 1, 1, new Rectangle(new Point(0, 0), 1, 1), Color.HONEYDEW, Color.BLACK, 100), Direction.ALL, 5, 1000);
        ps.update();
        ms.update();

        List<Entity> particleEntities = EntityHub.getInstance().getEntitiesWithType(ParticleEntity.class);
        assertEquals(5, particleEntities.size());

        ps.update();
        ms.update();
        assertEquals(5, particleEntities.size());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        ps.update();
        ms.update();
        particleEntities = EntityHub.getInstance().getEntitiesWithType(ParticleEntity.class);
        assertEquals(10, particleEntities.size());
    }

    @Test
    public void testParticleRemovedWhenTooFarFromOrigin() {
        Time.getInstance().setDeltaTime(1);
        ParticleSystem ps = new ParticleSystem();
        MovementSystem ms = new MovementSystem();
        ms.start();
        ps.start();

        new ParticleEmitterEntity(0, 0, new ParticleEntity(0, 0, 1, 1, new Rectangle(new Point(0, 0), 1, 1), Color.HONEYDEW, Color.BLACK, 100), Direction.ALL, 5, 1000);
        ps.update();
        ms.update();
        Entity particle = EntityHub.getInstance().getEntitiesWithType(ParticleEntity.class).getFirst();
        particle.getComponent(PositionComponent.class).setLocal(new Point(70.71, 70.71), particle);
        ps.update();
        assertTrue(EntityHub.getInstance().getEntitiesWithType(ParticleEntity.class).contains(particle));
        particle.getComponent(PositionComponent.class).setLocal(new Point(70.72, 70.72), particle);
        ps.update();
        assertFalse(EntityHub.getInstance().getEntitiesWithType(ParticleEntity.class).contains(particle));
    }
}
