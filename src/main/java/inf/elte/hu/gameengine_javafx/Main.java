package inf.elte.hu.gameengine_javafx;

import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.EntityManager;
import inf.elte.hu.gameengine_javafx.Entities.LoggerEntity;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import inf.elte.hu.gameengine_javafx.Misc.Scenes.GameScene;
import inf.elte.hu.gameengine_javafx.Misc.Scenes.TestScene;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.GameLoopStartUp;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.LoggerStartUp;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.ResourceStartUp;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.SystemStartUp;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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
        //LoggerStartUp loggerStartUp = new LoggerStartUp();
        new ResourceStartUp();

        BorderPane root = new BorderPane();
        //root.setTop(loggerStartUp.getLoggerEntity().getTextArea());
        root.setCenter(Globals.canvas);

        stageSetup(stage, new TestScene(root, 1920, 1080));

        new SystemStartUp();
        new GameLoopStartUp();
    }

    private void stageSetup(Stage stage, Scene scene) {
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
}
