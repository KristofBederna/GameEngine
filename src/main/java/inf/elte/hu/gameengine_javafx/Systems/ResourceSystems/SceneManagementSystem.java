package inf.elte.hu.gameengine_javafx.Systems.ResourceSystems;

import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Misc.Config;
import inf.elte.hu.gameengine_javafx.Misc.Scenes.GameScene;
import inf.elte.hu.gameengine_javafx.Misc.Scenes.TestScene;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * The SceneManagementSystem is responsible for managing and switching between different scenes in the game.
 * It handles the current active scene, the transition to the next scene, and the cleanup of resources when switching scenes.
 */
public class SceneManagementSystem extends GameSystem {

    // The current active scene
    private GameScene currentScene;

    // The scene to switch to when requested
    private GameScene nextScene;

    // The main stage for displaying scenes
    private Stage stage;

    /**
     * Constructor to initialize the SceneManagementSystem with a default scene.
     * The default scene is a TestScene defined by a BorderPane.
     */
    public SceneManagementSystem() {
        // Initialize with a default scene
        currentScene = new TestScene(new BorderPane(), Config.gameCanvasWidth, Config.gameCanvasHeight);
    }

    /**
     * Starts the SceneManagementSystem by marking it as active.
     * This method is invoked at the beginning to activate the system.
     */
    @Override
    public void start() {
        active = true;
    }

    /**
     * Updates the SceneManagementSystem by checking if a scene switch is requested.
     * If a scene switch is requested, it invokes the `switchScene()` method.
     */
    @Override
    public void update() {
        if (nextScene != null) {
            switchScene();
        }
    }

    /**
     * Switches the current scene to the next scene.
     * This involves running the breakdown process on the current scene, setting up the new scene,
     * and updating the active scene.
     */
    public void switchScene() {
        if (nextScene != null) {
            // Run breakdown on the current scene to clean up
            currentScene.breakdown();

            // Set the current scene to the next scene
            currentScene = nextScene;

            // Perform any setup for the new scene
            currentScene.setup();

            // Reset nextScene to null as the switch has occurred
            nextScene = null;
        }
    }

    /**
     * Requests a scene change by setting the next scene to the provided new scene.
     * The system will switch to this new scene when `update()` is called.
     *
     * @param newScene The new scene to switch to.
     */
    public void requestSceneChange(GameScene newScene) {
        nextScene = newScene;
    }

    /**
     * Aborts the SceneManagementSystem by deactivating it and cleaning up the current scene.
     * This method is invoked when the system is being shut down or aborted.
     */
    @Override
    public void abort() {
        active = false;
        if (currentScene != null) {
            currentScene.breakdown();
        }
    }

    /**
     * Sets the main stage for displaying scenes.
     *
     * @param stage The stage that will be used to display scenes.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Gets the current active scene.
     *
     * @return The current active scene.
     */
    public Scene getCurrentScene() {
        return currentScene;
    }

    /**
     * Gets the main stage used for displaying scenes.
     *
     * @return The stage used for displaying scenes.
     */
    public Stage getStage() {
        return stage;
    }
}
