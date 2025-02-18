package inf.elte.hu.gameengine_javafx;

import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.EntityManager;
import inf.elte.hu.gameengine_javafx.Entities.LoggerEntity;
import inf.elte.hu.gameengine_javafx.Misc.Camera;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import inf.elte.hu.gameengine_javafx.Misc.Scenes.GameScene;
import inf.elte.hu.gameengine_javafx.Misc.Scenes.Test2Scene;
import inf.elte.hu.gameengine_javafx.Misc.Scenes.TestScene;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.GameLoopStartUp;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.LoggerStartUp;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.ResourceStartUp;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.SystemStartUp;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Main extends Application {
    private final Runnable switchScene = this::switchSceneImpl;
    List<GameScene> scenes = new ArrayList<>(List.of(new TestScene(new BorderPane(), 1920, 1080, switchScene), new Test2Scene(new BorderPane(), 1920, 1080, switchScene)));
    Stage stage;
    int currentSceneIndex = 0;

    @Override
    public void start(Stage stage) {
        startUpGame(stage);
    }

    public static void main(String[] args) {
        launch();
    }

    private void startUpGame(Stage stage) {
        //LoggerStartUp loggerStartUp = new LoggerStartUp();
        new ResourceStartUp();
        Camera.getInstance(1920, 1080, 30*100, 15*100);
        Camera.getInstance().attachTo(Globals.playerEntity);

        this.stage = stage;

        BorderPane root = (BorderPane) scenes.get(currentSceneIndex).getRoot();
        //root.setTop(loggerStartUp.getLoggerEntity().getTextArea());
        root.setCenter(Globals.canvas);

        stageSetup(scenes.get(currentSceneIndex));

        new SystemStartUp();
        new GameLoopStartUp();
    }

    private void stageSetup(Scene scene) {
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

    private void switchSceneImpl() {
        Platform.runLater(() -> {
            currentSceneIndex = (currentSceneIndex + 1) % scenes.size();
            stage.setScene(scenes.get(currentSceneIndex));
            BorderPane root = (BorderPane) scenes.get(currentSceneIndex).getRoot();
            root.setCenter(Globals.canvas);
        });
    }
}
