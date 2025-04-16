package TestSuite.GameSystem;

import TestSuite.Entities.TestEntity;
import inf.elte.hu.gameengine_javafx.Components.Default.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.HitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.PhysicsComponents.AccelerationComponent;
import inf.elte.hu.gameengine_javafx.Components.PhysicsComponents.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.CentralMassComponent;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Rectangle;
import inf.elte.hu.gameengine_javafx.Maths.Vector;
import inf.elte.hu.gameengine_javafx.Misc.IgnoreCollisions;
import inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems.CollisionSystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CollisionSystemTests {

    TestEntity testEntity;
    TestEntity testEntity2;
    CollisionSystem collisionSystem;

    @BeforeEach
    public void setUp() {
        EntityHub.resetInstance();
        testEntity = new TestEntity();
        testEntity2 = new TestEntity();

        testEntity.getComponent(PositionComponent.class).setLocal(new Point(0, 0), testEntity);
        testEntity2.getComponent(PositionComponent.class).setLocal(new Point(0, 0), testEntity2);

        testEntity.addComponent(new CentralMassComponent(50, 50));
        testEntity2.addComponent(new CentralMassComponent(50, 50));

        testEntity.addComponent(new HitBoxComponent(new Rectangle(new Point(0, 0), 100, 100).getPoints()));
        testEntity2.addComponent(new HitBoxComponent(new Rectangle(new Point(0, 0), 100, 100).getPoints()));

        testEntity.addComponent(new VelocityComponent());
        testEntity2.addComponent(new VelocityComponent());

        testEntity.addComponent(new AccelerationComponent(new Vector(5, 0)));
        testEntity2.addComponent(new AccelerationComponent());

        collisionSystem = new CollisionSystem();
        collisionSystem.start();
    }

    @Test
    public void testHorizontalCollisionStopsVelocity() {
        collisionSystem.update();

        AccelerationComponent acceleration = testEntity.getComponent(AccelerationComponent.class);

        assertEquals(0, acceleration.getAcceleration().getDx()); //Loses acceleration, collision
    }

    @Test
    public void testNoCollisionContinuesVelocity() {
        testEntity2.getComponent(PositionComponent.class).setLocal(new Point(500, 0), testEntity2);
        testEntity2.getComponent(CentralMassComponent.class).setCentral(new Point(550, 50));

        collisionSystem.update();

        AccelerationComponent acceleration = testEntity.getComponent(AccelerationComponent.class);

        assertEquals(5, acceleration.getAcceleration().getDx()); //Keeps acceleration, no collision
    }

    @Test
    public void testVerticalCollisionLosesVelocity() {
        testEntity.getComponent(AccelerationComponent.class).setAcceleration(new Vector(0, 5));

        collisionSystem.update();

        AccelerationComponent acceleration = testEntity.getComponent(AccelerationComponent.class);
        assertEquals(0, acceleration.getAcceleration().getDy());
    }

    @Test
    public void testVerticalCollisionContinuesVelocity() {
        testEntity.getComponent(AccelerationComponent.class).setAcceleration(new Vector(0, 5));
        testEntity2.getComponent(PositionComponent.class).setLocal(new Point(500, 0), testEntity2);
        testEntity2.getComponent(CentralMassComponent.class).setCentral(new Point(550, 50));

        collisionSystem.update();

        AccelerationComponent acceleration = testEntity.getComponent(AccelerationComponent.class);
        assertEquals(5, acceleration.getAcceleration().getDy());
    }

    @Test
    public void testCollisionDiagonalLosesVelocity() {
        testEntity.getComponent(AccelerationComponent.class).setAcceleration(new Vector(5, 5));
        testEntity2.getComponent(PositionComponent.class).setLocal(new Point(100, 100), testEntity2);

        collisionSystem.update();

        AccelerationComponent acceleration = testEntity.getComponent(AccelerationComponent.class);
        assertEquals(0, acceleration.getAcceleration().getDx());
        assertEquals(0, acceleration.getAcceleration().getDy());
    }

    @Test
    public void testCollisionDiagonalContinuesVelocity() {
        testEntity.getComponent(AccelerationComponent.class).setAcceleration(new Vector(5, 5));
        testEntity2.getComponent(PositionComponent.class).setLocal(new Point(500, 0), testEntity2);
        testEntity2.getComponent(CentralMassComponent.class).setCentral(new Point(550, 50));

        collisionSystem.update();

        AccelerationComponent acceleration = testEntity.getComponent(AccelerationComponent.class);
        assertEquals(5, acceleration.getAcceleration().getDx());
        assertEquals(5, acceleration.getAcceleration().getDy());
    }

    @Test
    public void testCollisionIgnore() {
        IgnoreCollisions.getInstance().getCollisionRules().computeIfAbsent(TestEntity.class, k -> new ArrayList<>()).add(TestEntity.class);

        collisionSystem.update();

        AccelerationComponent acceleration = testEntity.getComponent(AccelerationComponent.class);

        assertEquals(5, acceleration.getAcceleration().getDx()); //Keeps acceleration, no collision, since they ignore eachother
    }
}
