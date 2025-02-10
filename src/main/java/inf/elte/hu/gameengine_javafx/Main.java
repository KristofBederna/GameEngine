package inf.elte.hu.gameengine_javafx;

import inf.elte.hu.gameengine_javafx.Core.Globals;
import inf.elte.hu.gameengine_javafx.Misc.GameLoopStartUp;
import inf.elte.hu.gameengine_javafx.Misc.ResourceStartup;
import inf.elte.hu.gameengine_javafx.Misc.Scenes.GameScene;
import inf.elte.hu.gameengine_javafx.Misc.Scenes.TestScene;
import inf.elte.hu.gameengine_javafx.Misc.SystemStartup;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import static inf.elte.hu.gameengine_javafx.Core.Globals.canvas;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        startUpGame(stage);
    }

    public static void main(String[] args) {
        launch();
    }

    private void startUpGame(Stage stage) {
//        LoggerEntity loggerEntity = new LoggerEntity();
//        EntityManager<LoggerEntity> loggerEntityManager = new EntityManager<>();
//        loggerEntityManager.register(loggerEntity);
//        EntityHub.getInstance().addEntityManager(LoggerEntity.class, loggerEntityManager);

        new ResourceStartup();

        BorderPane root = new BorderPane();
//        root.setTop(loggerEntity.getTextArea());
        root.setCenter(Globals.canvas);
        GameScene gameScene = new TestScene(root, 500, 500);

        new SystemStartup();

        stage.setTitle("JavaFX Game Engine");
        stage.setScene(gameScene);

        stage.setOnCloseRequest(event -> {
            System.out.println("Window is closing!");
            System.exit(0);
        });

        stage.show();

        new GameLoopStartUp();
    }
}
