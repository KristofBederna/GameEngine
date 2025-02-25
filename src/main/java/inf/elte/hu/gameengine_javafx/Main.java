package inf.elte.hu.gameengine_javafx;

import inf.elte.hu.gameengine_javafx.Core.SystemHub;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import inf.elte.hu.gameengine_javafx.Misc.Layers.GameCanvas;
import inf.elte.hu.gameengine_javafx.Misc.Layers.GameLayer;
import inf.elte.hu.gameengine_javafx.Misc.Layers.uiRoot;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.SystemStartUp;
import inf.elte.hu.gameengine_javafx.Systems.ResourceSystems.SceneManagementSystem;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        startUpGame(stage);
    }

    public static void main(String[] args) {
        launch();
    }

    private void startUpGame(Stage stage) {
        SystemStartUp systemStartUp = new SystemStartUp();
        systemStartUp.startUpSceneManagementSystem();
        SceneManagementSystem sceneManagementSystem = SystemHub.getInstance().getSystem(SceneManagementSystem.class);
        sceneManagementSystem.setStage(stage);

        BorderPane root = (BorderPane) sceneManagementSystem.getCurrentScene().getRoot();

        GameCanvas gameCanvas = GameCanvas.createInstance(1920, 1080);
        uiRoot uiRoot = inf.elte.hu.gameengine_javafx.Misc.Layers.uiRoot.getInstance();
        GameLayer gameLayer = GameLayer.getInstance();
        gameLayer.getChildren().addAll(gameCanvas, uiRoot);
        uiRoot.setFocusTraversable(true);

        root.setCenter(gameLayer);

        stageSetup();
    }

    private void stageSetup() {
        Stage stage = SystemHub.getInstance().getSystem(SceneManagementSystem.class).getStage();
        Scene scene = SystemHub.getInstance().getSystem(SceneManagementSystem.class).getCurrentScene();

        // Set window title here
        stage.setTitle("JavaFX Game Engine");

        // Assigns the stage as the parent container of the scene
        stage.setScene(scene);

        // Define on close behaviour here
        stage.setOnCloseRequest(event -> {
            System.out.println("Window is closing!");
            System.exit(0);
        });

        // Makes the window visible
        stage.show();
    }
}
