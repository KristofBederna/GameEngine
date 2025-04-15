package TestSuite.GameSystem;

import static org.junit.jupiter.api.Assertions.*;

import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.SystemHub;
import inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems.MovementSystem;
import inf.elte.hu.gameengine_javafx.Systems.ResourceSystems.BackgroundMusicSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SystemHubTest {

    private SystemHub systemHub;

    @BeforeEach
    public void setUp() {
        SystemHub instance = SystemHub.getInstance();
        instance = null;
        systemHub = SystemHub.getInstance();
    }

    @Test
    public void testSingletonInstance() {
        SystemHub anotherInstance = SystemHub.getInstance();
        assertSame(systemHub, anotherInstance, "SystemHub should be a singleton");
    }

    @Test
    public void testAddSystem() {
        MovementSystem system = new MovementSystem();

        systemHub.addSystem(MovementSystem.class, system, 1);

        GameSystem retrievedSystem = systemHub.getSystem(MovementSystem.class);
        assertNotNull(retrievedSystem, "System should be added successfully");
        assertSame(system, retrievedSystem, "The retrieved system should match the added system");
    }

    @Test
    public void testAddMultipleSystems() {
        MovementSystem system1 = new MovementSystem();
        BackgroundMusicSystem system2 = new BackgroundMusicSystem();

        systemHub.addSystem(MovementSystem.class, system1, 1);
        systemHub.addSystem(BackgroundMusicSystem.class, system2, 2);

        GameSystem retrievedSystem1 = systemHub.getSystem(MovementSystem.class);
        GameSystem retrievedSystem2 = systemHub.getSystem(BackgroundMusicSystem.class);

        assertNotNull(retrievedSystem1, "System 1 should be retrieved successfully");
        assertSame(system1, retrievedSystem1, "System 1 should match the added system");

        assertNotNull(retrievedSystem2, "System 2 should be retrieved successfully");
        assertSame(system2, retrievedSystem2, "System 2 should match the added system");
    }

    @Test
    public void testRemoveSystem() {
        MovementSystem system = new MovementSystem();

        // Add a system and then remove it
        systemHub.addSystem(MovementSystem.class, system, 1);
        systemHub.removeSystem(MovementSystem.class);

        // Ensure the system is removed
        GameSystem removedSystem = systemHub.getSystem(MovementSystem.class);
        assertNull(removedSystem, "System should be removed and not found in SystemHub");
    }

    @Test
    public void testGetAllSystemsInPriorityOrder() {
        MovementSystem system1 = new MovementSystem();
        BackgroundMusicSystem system2 = new BackgroundMusicSystem();

        // Add systems with different priorities
        systemHub.addSystem(MovementSystem.class, system1, 2);
        systemHub.addSystem(BackgroundMusicSystem.class, system2, 1);

        // Get all systems in priority order
        List<GameSystem> systems = systemHub.getAllSystemsInPriorityOrder();
        assertEquals(2, systems.size(), "There should be two systems");
        assertSame(system2, systems.get(0), "The system with the highest priority should be first");
        assertSame(system1, systems.get(1), "The system with the second priority should be second");
    }
}

