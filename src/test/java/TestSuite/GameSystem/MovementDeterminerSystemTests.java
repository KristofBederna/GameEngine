package TestSuite.GameSystem;

import TestSuite.Entities.TestEntity;
import inf.elte.hu.gameengine_javafx.Components.PhysicsComponents.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.DirectionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.StateComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Maths.Vector;
import inf.elte.hu.gameengine_javafx.Misc.Direction;
import inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems.MovementDeterminerSystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MovementDeterminerSystemTests {

    Entity entity;
    MovementDeterminerSystem movementSystem;

    @BeforeEach
    public void setUp() {
        EntityHub.resetInstance();
        entity = new TestEntity();
        entity.addComponent(new VelocityComponent());
        entity.addComponent(new StateComponent("idle"));
        entity.addComponent(new DirectionComponent(Direction.ALL));

        movementSystem = new MovementDeterminerSystem();
        movementSystem.start();
    }

    @Test
    public void testMovesRight() {
        entity.getComponent(VelocityComponent.class).setVelocity(new Vector(5, 0));

        movementSystem.update();

        assertEquals("right", entity.getComponent(StateComponent.class).getCurrentState());
        assertEquals(Direction.RIGHT, entity.getComponent(DirectionComponent.class).getDirection());
    }

    @Test
    public void testMovesLeft() {
        entity.getComponent(VelocityComponent.class).setVelocity(new Vector(-5, 0));

        movementSystem.update();

        assertEquals("left", entity.getComponent(StateComponent.class).getCurrentState());
        assertEquals(Direction.LEFT, entity.getComponent(DirectionComponent.class).getDirection());
    }

    @Test
    public void testMovesUpDominant() {
        entity.getComponent(VelocityComponent.class).setVelocity(new Vector(1, -5));

        movementSystem.update();

        assertEquals("up", entity.getComponent(StateComponent.class).getCurrentState());
        assertEquals(Direction.UP, entity.getComponent(DirectionComponent.class).getDirection());
    }

    @Test
    public void testMovesDownDominant() {
        entity.getComponent(VelocityComponent.class).setVelocity(new Vector(1, 5));

        movementSystem.update();

        assertEquals("down", entity.getComponent(StateComponent.class).getCurrentState());
        assertEquals(Direction.DOWN, entity.getComponent(DirectionComponent.class).getDirection());
    }

    @Test
    public void testIdleStateWhenStationary() {
        entity.getComponent(StateComponent.class).setCurrentState("idle");
        entity.getComponent(VelocityComponent.class).setVelocity(new Vector(0, 0));

        movementSystem.update();

        assertEquals("idle", entity.getComponent(StateComponent.class).getCurrentState());
        assertEquals(Direction.ALL, entity.getComponent(DirectionComponent.class).getDirection());
    }
}
