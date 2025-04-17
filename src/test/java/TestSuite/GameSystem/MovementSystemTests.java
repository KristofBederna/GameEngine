package TestSuite.GameSystem;

import TestSuite.Entities.DummyEntity;
import inf.elte.hu.gameengine_javafx.Components.Default.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.PhysicsComponents.AccelerationComponent;
import inf.elte.hu.gameengine_javafx.Components.PhysicsComponents.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Entities.WorldEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Maths.Vector;
import inf.elte.hu.gameengine_javafx.Misc.Time;
import inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems.MovementSystem;
import inf.elte.hu.gameengine_javafx.Systems.ResourceSystems.WorldLoaderSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MovementSystemTests {

    private MovementSystem movementSystem;
    private DummyEntity entity;

    @BeforeEach
    public void setUp() {
        EntityHub.resetInstance();
        entity = new DummyEntity(500, 500, "left", "/assets/images/PlayerIdle.png", 32, 32);
        WorldEntity.getInstance(32, 16, "/assets/maps/easyMap.txt", "/assets/tileSets/testTiles.txt");
        new WorldLoaderSystem().start();

        movementSystem = new MovementSystem();
        movementSystem.start();

        Time.getInstance().setDeltaTime(1);
    }

    @Test
    void testDummyEntityStartsMoving() {
        entity.getComponent(AccelerationComponent.class).setAcceleration(new Vector(5, 5));

        Point oldGlobal = new Point(entity.getComponent(PositionComponent.class).getGlobal().getX(), entity.getComponent(PositionComponent.class).getGlobal().getY());

        movementSystem.update();

        Point newGlobal = entity.getComponent(PositionComponent.class).getGlobal();

        assertTrue(oldGlobal.distanceTo(newGlobal) > 0);
    }

    @Test
    void testDummyEntityLosesVelocityDueToHavingNoAcceleration() {
        entity.getComponent(AccelerationComponent.class).setAcceleration(new Vector(0, 0));
        entity.getComponent(VelocityComponent.class).setVelocity(new Vector(0.5, 0.5));

        movementSystem.update();

        assertTrue(entity.getComponent(VelocityComponent.class).getVelocity().getDx() < 0.5 && entity.getComponent(VelocityComponent.class).getVelocity().getDy() < 0.5);
    }

    @Test
    void testVelocityDoesNotExceedMaxLimit() {
        entity.getComponent(VelocityComponent.class).setVelocity(new Vector(0.49, 0.49));
        entity.getComponent(AccelerationComponent.class).setAcceleration(new Vector(1, 1));

        movementSystem.update();

        Vector velocity = entity.getComponent(VelocityComponent.class).getVelocity();
        assertTrue(velocity.getDx() <= 0.5 && velocity.getDy() <= 0.5, "Velocity should not exceed max limit of 0.5");
    }

    @Test
    void testVelocityIsNormalizedOnDiagonalMovement() {
        entity.getComponent(AccelerationComponent.class).setAcceleration(new Vector(1, 1));

        movementSystem.update();

        Vector velocity = entity.getComponent(VelocityComponent.class).getVelocity();
        double magnitude = velocity.magnitude();

        assertTrue(magnitude <= 0.5, "Velocity magnitude should respect max limit even diagonally.");
    }

    @Test
    void testEntityDoesNotMoveWithoutVelocityOrAcceleration() {
        entity.getComponent(AccelerationComponent.class).setAcceleration(new Vector(0, 0));
        entity.getComponent(VelocityComponent.class).setVelocity(new Vector(0, 0));

        Point before = entity.getComponent(PositionComponent.class).getGlobal();
        movementSystem.update();
        Point after = entity.getComponent(PositionComponent.class).getGlobal();

        assertEquals(before, after, "Entity should not move without velocity or acceleration.");
    }

}
