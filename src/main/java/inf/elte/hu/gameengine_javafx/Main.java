package inf.elte.hu.gameengine_javafx;

import inf.elte.hu.gameengine_javafx.Components.*;
import inf.elte.hu.gameengine_javafx.Core.*;
import inf.elte.hu.gameengine_javafx.Entities.DebugInfoEntity;
import inf.elte.hu.gameengine_javafx.Entities.DummyEntity;
import inf.elte.hu.gameengine_javafx.Entities.TileEntity;
import inf.elte.hu.gameengine_javafx.Misc.*;
import inf.elte.hu.gameengine_javafx.Systems.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {
    private final List<Entity> entities = new ArrayList<>();
    SystemHub systemHub = SystemHub.getInstance();
    ResourceHub resourceHub = ResourceHub.getInstance();

    @Override
    public void start(Stage stage) {
        ResourceManager<Image> imageManager = new ResourceManager<>(key -> {
            try {
                if (key.startsWith("file:")) {
                    return new Image(key);
                }

                InputStream resource = ResourceManager.class.getResourceAsStream(key);
                if (resource != null) {
                    return new Image(resource);
                }
                return new Image("file:" + key);
            } catch (Exception e) {
                System.err.println("Error loading image: " + key);
                return null;
            }
        });
        resourceHub.addResourceManager(Image.class, imageManager);

        Canvas canvas = new Canvas(1920, 1080);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        DebugInfoEntity debugInfoEntity = new DebugInfoEntity();
        entities.add(debugInfoEntity);

        BorderPane root = new BorderPane();
        root.setTop(debugInfoEntity.getTextArea());
        root.setCenter(canvas);
        Scene scene = new Scene(root, 1920, 1080);

        stage.setTitle("JavaFX Game Engine");
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            System.out.println("Window is closing!");
            System.exit(0);
        });

        stage.show();

        systemHub.addSystem(InputHandlingSystem.class, new InputHandlingSystem(new KeyboardInputHandler(scene)));
        systemHub.addSystem(MovementSystem.class, new MovementSystem());
        systemHub.addSystem(CollisionSystem.class, new CollisionSystem());
        systemHub.addSystem(RenderSystem.class, new RenderSystem(gc));
        systemHub.addSystem(AnimationSystem.class, new AnimationSystem());
        systemHub.addSystem(DebugInfoSystem.class, new DebugInfoSystem());

        TileLoader tileLoader = new TileLoader();
        int tileSize = 100;
        TileSetLoader.loadSet("/assets/tileSets/testTiles.txt", tileLoader);
        GameMap map = MapLoader.loadMap("/assets/maps/testMap.txt", tileSize, tileLoader);

        DummyEntity dummyEntity = new DummyEntity(100, 100, "idle", "/assets/images/PlayerIdle.png", 100, 100);

        dummyEntity.getComponent(InteractiveComponent.class).mapInput(KeyCode.W, () -> moveUp(dummyEntity), () -> counterVertical(dummyEntity));
        dummyEntity.getComponent(InteractiveComponent.class).mapInput(KeyCode.S, () -> moveDown(dummyEntity), () -> counterVertical(dummyEntity));
        dummyEntity.getComponent(InteractiveComponent.class).mapInput(KeyCode.A, () -> moveLeft(dummyEntity), () -> counterHorizontal(dummyEntity));
        dummyEntity.getComponent(InteractiveComponent.class).mapInput(KeyCode.D, () -> moveRight(dummyEntity), () -> counterHorizontal(dummyEntity));

        for (TileEntity[] row : map.getMapData()) {
            entities.addAll(Arrays.asList(row));
        }
        entities.add(dummyEntity);


        GameLoop gameLoop = new GameLoop(60) {
            @Override
            public void update() {
                systemHub.getSystem(AnimationSystem.class).update(1.0f/60.0f, entities);
                systemHub.getSystem(RenderSystem.class).update(1.0f/60.0f, entities);
                systemHub.getSystem(MovementSystem.class).update(1.0f/60.0f, entities);
                systemHub.getSystem(InputHandlingSystem.class).update(1.0f/60.0f, entities);
                systemHub.getSystem(CollisionSystem.class).update(1.0f/60.0f, entities);
                systemHub.getSystem(DebugInfoSystem.class).update(1.0f/60.0f, entities);
            }
        };

        gameLoop.startLoop();
    }

    public static void main(String[] args) {
        launch();
    }

    private void moveUp(Entity e) {
        e.getComponent(VelocityComponent.class).setDy(-10);
        e.getComponent(StateComponent.class).setCurrentState("up");
    }

    private void moveDown(Entity e) {
        e.getComponent(VelocityComponent.class).setDy(10);
        e.getComponent(StateComponent.class).setCurrentState("down");
    }

    private void moveLeft(Entity e) {
        e.getComponent(VelocityComponent.class).setDx(-10);
        e.getComponent(StateComponent.class).setCurrentState("left");
    }

    private void moveRight(Entity e) {
        e.getComponent(VelocityComponent.class).setDx(10);
        e.getComponent(StateComponent.class).setCurrentState("right");
    }

    private void counterVertical(Entity e) {
        e.getComponent(VelocityComponent.class).setDy(0);
        e.getComponent(StateComponent.class).setCurrentState("idle");
    }

    private void counterHorizontal(Entity e) {
        e.getComponent(VelocityComponent.class).setDx(0);
        e.getComponent(StateComponent.class).setCurrentState("idle");
    }
}
