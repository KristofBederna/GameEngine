package TestSuite.Resources;

import static org.junit.jupiter.api.Assertions.*;

import inf.elte.hu.gameengine_javafx.Core.ResourceHub;
import inf.elte.hu.gameengine_javafx.Core.ResourceManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ResourceHubTests {

    private ResourceHub resourceHub;

    @BeforeEach
    public void setUp() {
        ResourceHub.resetInstance();
        resourceHub = ResourceHub.getInstance();
    }

    @Test
    public void testSingletonInstance() {
        ResourceHub anotherInstance = ResourceHub.getInstance();
        assertSame(resourceHub, anotherInstance, "ResourceHub should be a singleton");
    }

    @Test
    public void testAddAndGetResourceManager() {
        ResourceManager<String> stringResourceManager = new ResourceManager<>(String::new);

        resourceHub.addResourceManager(String.class, stringResourceManager);

        ResourceManager<String> retrievedManager = resourceHub.getResourceManager(String.class);
        assertNotNull(retrievedManager, "ResourceManager should not be null");
        assertSame(stringResourceManager, retrievedManager, "Retrieved ResourceManager should be the same");
    }

    @Test
    public void testGetResourceManagerWithInvalidType() {
        ResourceManager<Integer> integerResourceManager = new ResourceManager<>(Integer::valueOf);
        resourceHub.addResourceManager(Integer.class, integerResourceManager);

        ResourceManager<String> retrievedManager = resourceHub.getResourceManager(String.class);
        assertNull(retrievedManager, "ResourceManager should be null for unmatched type");
    }

    @Test
    public void testRemoveResourceManager() {
        ResourceManager<Integer> integerResourceManager = new ResourceManager<>(Integer::valueOf);
        resourceHub.addResourceManager(Integer.class, integerResourceManager);

        resourceHub.removeResourceManager(Integer.class);

        ResourceManager<Integer> retrievedManager = resourceHub.getResourceManager(Integer.class);
        assertNull(retrievedManager, "ResourceManager should be null after removal");
    }

    @Test
    public void testUnloadAll() {
        ResourceManager<String> stringResourceManager = new ResourceManager<>(String::new);
        ResourceManager<Integer> integerResourceManager = new ResourceManager<>(Integer::valueOf);

        resourceHub.addResourceManager(String.class, stringResourceManager);
        resourceHub.addResourceManager(Integer.class, integerResourceManager);

        resourceHub.unloadAll();

        assertTrue(resourceHub.getResourceManager(String.class).getResources().isEmpty() && resourceHub.getResourceManager(Integer.class).getResources().isEmpty(), "ResourceManagers should be empty");
    }

    @Test
    public void testClearResources() {
        resourceHub.addResourceManager(String.class, new ResourceManager<>(String::new));
        resourceHub.addResourceManager(Integer.class, new ResourceManager<>(Integer::valueOf));

        resourceHub.clearResources();

        assertTrue(resourceHub.getAllResourceManagers().isEmpty(), "Resource managers should be cleared");
    }

    @Test
    public void testResetInstance() {
        resourceHub = ResourceHub.getInstance();
        ResourceHub.resetInstance();

        ResourceHub newInstance = ResourceHub.getInstance();
        assertNotSame(resourceHub, newInstance, "Instance should be reset and a new instance should be created");
    }
}

