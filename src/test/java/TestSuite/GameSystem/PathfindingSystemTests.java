package TestSuite.GameSystem;

import TestSuite.Entities.DummyEntity;
import inf.elte.hu.gameengine_javafx.Components.PathfindingComponent;
import inf.elte.hu.gameengine_javafx.Components.PhysicsComponents.AccelerationComponent;
import inf.elte.hu.gameengine_javafx.Components.PhysicsComponents.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.CentralMassComponent;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Entities.WorldEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Misc.Time;
import inf.elte.hu.gameengine_javafx.Systems.PathfindingSystems.PathfindingSystem;
import inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems.MovementSystem;
import inf.elte.hu.gameengine_javafx.Systems.ResourceSystems.MapLoaderSystems.WorldLoaderSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PathfindingSystemTests {

    PathfindingSystem pathfindingSystem;
    MovementSystem movementSystem;

    @BeforeEach
    void setup() {
        EntityHub.resetInstance();
        WorldEntity.getInstance(32, 16, "/assets/maps/hardForAIMap.txt", "/assets/tileSets/testTiles.txt");
        WorldLoaderSystem loaderSystem = new WorldLoaderSystem();
        movementSystem = new MovementSystem();
        movementSystem.start();
        loaderSystem.start();
        Time.getInstance().setDeltaTime(1);
        pathfindingSystem = new PathfindingSystem();
        pathfindingSystem.start();
    }

    @Test
    void testDummyEntityStartsMovingTowardsTarget() {
        int startX = 100;
        int startY = 100;
        String state = "idle";
        String imagePath = "/assets/images/PlayerIdle.png";
        double width = 50;
        double height = 50;

        DummyEntity dummy = new DummyEntity(startX, startY, state, imagePath, width, height);

        PathfindingComponent path = dummy.getComponent(PathfindingComponent.class);
        CentralMassComponent cmc = dummy.getComponent(CentralMassComponent.class);
        AccelerationComponent accel = dummy.getComponent(AccelerationComponent.class);
        VelocityComponent velocity = dummy.getComponent(VelocityComponent.class);

        path.resetPathing(dummy);
        path.setEnd(new Point(550, 550));

        assertNotNull(path);
        assertNotNull(cmc);
        assertNotNull(accel);
        assertNotNull(velocity);

        double oldX = cmc.getCentralX();
        double oldY = cmc.getCentralY();

        pathfindingSystem.update();
        pathfindingSystem.update();
        movementSystem.update();

        double dx = accel.getAcceleration().getDx();
        double dy = accel.getAcceleration().getDy();

        assertTrue(dx != 0 || dy != 0, "Entity should be accelerating toward target");

        cmc.setCentralX(oldX + dx);
        cmc.setCentralY(oldY + dy);

        pathfindingSystem.update();
        movementSystem.update();

        assertNotEquals(oldX, cmc.getCentralX(), "Entity should have moved on x-axis");
        assertNotEquals(oldY, cmc.getCentralY(), "Entity should have moved on y-axis");
    }

    @Test
    void testPathfindingFailsForUnreachableTarget() {
        int startX = 550;
        int startY = 550;
        String state = "idle";
        String imagePath = "/assets/images/PlayerIdle.png";
        double width = 50;
        double height = 50;

        DummyEntity dummy = new DummyEntity(startX, startY, state, imagePath, width, height);

        PathfindingComponent path = dummy.getComponent(PathfindingComponent.class);
        CentralMassComponent cmc = dummy.getComponent(CentralMassComponent.class);

        assertNotNull(path);
        assertNotNull(cmc);

        path.resetPathing(dummy);

        Point unreachableTarget = new Point(150, 1450);
        path.setEnd(unreachableTarget);

        pathfindingSystem.update();

        assertNull(path.getPath(), "Path should not exist for unreachable destination");
    }

    @Test
    void testPathfindingSucceedsForReachableTarget() {
        int startX = 550;
        int startY = 550;
        String state = "idle";
        String imagePath = "/assets/images/PlayerIdle.png";
        double width = 50;
        double height = 50;

        DummyEntity dummy = new DummyEntity(startX, startY, state, imagePath, width, height);

        PathfindingComponent path = dummy.getComponent(PathfindingComponent.class);
        CentralMassComponent cmc = dummy.getComponent(CentralMassComponent.class);

        assertNotNull(path);
        assertNotNull(cmc);

        path.resetPathing(dummy);

        Point unreachableTarget = new Point(150, 150);
        path.setEnd(unreachableTarget);

        pathfindingSystem.update();

        assertNotNull(path.getPath(), "Path should exist reachable destination");
    }

    @Test
    void testShortestPathCalculatedCorrectlyOnClearGrid() {
        int startX = 150; // tile (1,1)
        int startY = 150;
        String state = "idle";
        String imagePath = "/assets/images/PlayerIdle.png";
        double width = 50;
        double height = 50;

        DummyEntity dummy = new DummyEntity(startX, startY, state, imagePath, width, height);

        PathfindingComponent path = dummy.getComponent(PathfindingComponent.class);
        CentralMassComponent cmc = dummy.getComponent(CentralMassComponent.class);

        assertNotNull(path);
        assertNotNull(cmc);

        path.resetPathing(dummy);

        // End tile (3,3) → center = (350, 350)
        Point endPoint = new Point(350, 350);
        path.setEnd(endPoint);

        pathfindingSystem.update();

        var calculatedPath = path.getPath();

        assertNotNull(calculatedPath, "Path should not be null");
        assertFalse(calculatedPath.isEmpty(), "Path should not be empty");

        // Expected path: [150,150] -> [250,250] -> [350,250] -> [350,350]
        assertEquals(4, calculatedPath.size(), "Path should contain 4 points");

        assertTrue(new Point(150, 150).distanceTo(calculatedPath.getFirst()) < 5,"Start tile mismatch");
        assertTrue(new Point(250, 250).distanceTo(calculatedPath.get(1)) < 5, "First step mismatch");
        assertTrue(new Point(350, 250).distanceTo(calculatedPath.get(2)) < 5, "Second step mismatch");
        assertTrue(new Point(350, 350).distanceTo(calculatedPath.get(3)) < 5, "End point mismatch");
    }

    @Test
    void testLongPathCalculatedCorrectly() {
        int startX = 150; // tile (1,1)
        int startY = 150;
        String state = "idle";
        String imagePath = "/assets/images/PlayerIdle.png";
        double width = 50;
        double height = 50;

        DummyEntity dummy = new DummyEntity(startX, startY, state, imagePath, width, height);

        PathfindingComponent path = dummy.getComponent(PathfindingComponent.class);
        CentralMassComponent cmc = dummy.getComponent(CentralMassComponent.class);

        assertNotNull(path);
        assertNotNull(cmc);

        path.resetPathing(dummy);

        // End tile (30,14) → center = (3050, 1450)
        Point endPoint = new Point(3050, 1450);
        path.setEnd(endPoint);

        pathfindingSystem.update();

        var calculatedPath = path.getPath();

        assertNotNull(calculatedPath, "Path should not be null");
        assertFalse(calculatedPath.isEmpty(), "Path should not be empty");

        Point finalPoint = calculatedPath.getLast();
        assertTrue(finalPoint.distanceTo(endPoint) < 5, "Final point should be near the goal");
        assertTrue(calculatedPath.size() >= 20, "Should be a long path across the map");
    }

    @Test
    void testNoPathIfAlreadyAtTarget() {
        DummyEntity dummy = new DummyEntity(150, 150, "idle", "/img.png", 50, 50);
        PathfindingComponent path = dummy.getComponent(PathfindingComponent.class);
        path.resetPathing(dummy);
        path.setEnd(new Point(150, 150)); // same as start
        pathfindingSystem.update();

        assertTrue(new Point(150, 150).distanceTo(path.getPath().getFirst()) < 5, "Path should be the start if already at the destination");
    }

    @Test
    void testRepathingWhenTargetChanges() {
        DummyEntity dummy = new DummyEntity(150, 150, "idle", "/img.png", 50, 50);
        PathfindingComponent path = dummy.getComponent(PathfindingComponent.class);
        path.resetPathing(dummy);
        path.setEnd(new Point(550, 550));
        pathfindingSystem.update();

        var oldPath = path.getPath();
        path.resetPathing(dummy);
        path.setEnd(new Point(750, 750)); // new target
        pathfindingSystem.update();

        var newPath = path.getPath();
        assertNotEquals(oldPath, newPath, "Path should change if the target changes");
    }
}
