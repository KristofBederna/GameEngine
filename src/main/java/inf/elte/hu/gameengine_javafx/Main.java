package inf.elte.hu.gameengine_javafx;

import inf.elte.hu.gameengine_javafx.Core.SystemHub;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import inf.elte.hu.gameengine_javafx.Misc.Scenes.GameScene;
import inf.elte.hu.gameengine_javafx.Misc.Scenes.Test2Scene;
import inf.elte.hu.gameengine_javafx.Misc.Scenes.TestScene;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.GameLoopStartUp;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.ResourceStartUp;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.SystemStartUp;
import inf.elte.hu.gameengine_javafx.Systems.SceneManagementSystem;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
//    private final Runnable switchScene = this::switchSceneImpl;

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
        //LoggerStartUp loggerStartUp = new LoggerStartUp();
        sceneManagementSystem.setStage(stage);

        BorderPane root = (BorderPane) sceneManagementSystem.getCurrentScene().getRoot();
        //root.setTop(loggerStartUp.getLoggerEntity().getTextArea());
        root.setCenter(Globals.canvas);

        stageSetup();
    }

    private void stageSetup() {
        Stage stage = SystemHub.getInstance().getSystem(SceneManagementSystem.class).getStage();
        Scene scene = SystemHub.getInstance().getSystem(SceneManagementSystem.class).getCurrentScene();

        //Set window title here
        stage.setTitle("JavaFX Game Engine");

        //Assigns the stage as the parent container of the scene
        stage.setScene(scene);

        //Define on close behaviour here
        stage.setOnCloseRequest(event -> {
            System.out.println("Window is closing!");
            System.exit(0);
        });

        //Makes the window visible
        stage.show();
    }

//    private void switchSceneImpl() {
//        Platform.runLater(() -> {
//            currentScene.breakdown();
//            GameScene newScene = new Test2Scene(new BorderPane(), 1920, 1080, switchScene);
//            stage.setScene(null);
//            stage.setScene(newScene);
//            BorderPane root = (BorderPane) newScene.getRoot();
//            root.setCenter(Globals.canvas);
//        });
//    }
}
