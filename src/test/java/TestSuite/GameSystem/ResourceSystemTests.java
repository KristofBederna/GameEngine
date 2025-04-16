package TestSuite.GameSystem;

import inf.elte.hu.gameengine_javafx.Core.ResourceHub;
import inf.elte.hu.gameengine_javafx.Core.ResourceManager;
import inf.elte.hu.gameengine_javafx.Misc.Configs.ResourceConfig;
import inf.elte.hu.gameengine_javafx.Systems.ResourceSystems.ResourceSystem;
import org.junit.jupiter.api.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceSystemTests {

    @BeforeEach
    public void setup() {
        ResourceHub.getInstance().unloadAll();
    }

    @Test
    public void testOldResourcesAreRemoved() throws InterruptedException {
        ResourceConfig.resourceUnloadThresholdTime = 500;
        ResourceManager<String> dummyManager = new ResourceManager<>(key -> "Loaded:" + key);

        dummyManager.get("delete");

        Thread.sleep(1100);

        dummyManager.get("keep");

        ResourceHub.getInstance().addResourceManager(String.class, dummyManager);

        ResourceSystem resourceSystem = new ResourceSystem();
        resourceSystem.start();
        resourceSystem.update();

        Map<String, String> remaining = dummyManager.getResources();
        assertTrue(remaining.containsKey("keep"));
        assertFalse(remaining.containsKey("delete"));
    }
}
