package TestSuite.GameSystem;

import TestSuite.Scenes.TestGameScene;
import inf.elte.hu.gameengine_javafx.Core.SystemHub;
import inf.elte.hu.gameengine_javafx.Misc.Scenes.GameScene;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.SystemStartUp;
import inf.elte.hu.gameengine_javafx.Systems.ResourceSystems.SceneManagementSystem;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

public class SceneManagementSystemTests {
    private SceneManagementSystem sceneSystem;
    private static boolean javafxInitialized = false;

    @BeforeEach
    public void setUp() throws Exception {
        SystemHub.resetInstance();

        if (!javafxInitialized) {
            try {
                if (!Platform.isFxApplicationThread()) {
                    final CountDownLatch latch = new CountDownLatch(1);
                    Platform.startup(latch::countDown);
                    latch.await();
                }
            } catch (IllegalStateException ignored) {
            }
            javafxInitialized = true;
        }

        CountDownLatch initLatch = new CountDownLatch(1);
        Platform.runLater(() -> {
            Stage stage = new Stage();
            SystemStartUp systemStartUp = new SystemStartUp(() -> {});
            systemStartUp.startUpSceneManagementSystem();

            sceneSystem = SystemHub.getInstance().getSystem(SceneManagementSystem.class);
            sceneSystem.setStage(stage);
            initLatch.countDown();
        });
        initLatch.await();
    }


    @Test
    public void testStartInitializesCurrentScene() {
        assertNull(sceneSystem.getCurrentScene());
        sceneSystem.start();
        assertNotNull(sceneSystem.getCurrentScene());
        assertTrue(sceneSystem.getIsActive());
    }

    @Test
    public void testRequestSceneChangeSetsNextScene() {
        sceneSystem.start();
        TestGameScene mockScene = new TestGameScene(new BorderPane(), 800, 600);

        sceneSystem.requestSceneChange(mockScene);
        sceneSystem.update();

        assertEquals(mockScene, sceneSystem.getCurrentScene());
    }

    @Test
    public void testSwitchSceneReplacesCurrentScene() {
        sceneSystem.start();
        GameScene originalScene = sceneSystem.getCurrentScene();

        TestGameScene newScene = new TestGameScene(new BorderPane(), 800, 600);

        sceneSystem.requestSceneChange(newScene);
        sceneSystem.update();

        assertNotSame(originalScene, sceneSystem.getCurrentScene());
        assertTrue(newScene.isSetupCalled());
    }

    @Test
    public void testAbortDeactivatesSystemAndCallsBreakdown() {
        sceneSystem.start();

        TestGameScene monitoredScene = new TestGameScene(new BorderPane(), 800, 600);

        sceneSystem.requestSceneChange(monitoredScene);
        sceneSystem.update();

        sceneSystem.abort();
        assertFalse(sceneSystem.getIsActive());
        assertTrue(monitoredScene.isBreakdownCalled());
    }
}
