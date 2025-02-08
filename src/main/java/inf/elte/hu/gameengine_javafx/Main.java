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
import javafx.scene.input.MouseButton;
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
        startUpGame(stage);
    }

    public static void main(String[] args) {
        launch();
    }

    private void startUpGame(Stage stage) {
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

        Canvas canvas = new Canvas(500, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();

       // DebugInfoEntity debugInfoEntity = new DebugInfoEntity();
        // entities.add(debugInfoEntity);

        BorderPane root = new BorderPane();
        //root.setTop(debugInfoEntity.getTextArea());
        root.setCenter(canvas);
        Scene scene = new Scene(root, 500, 500);

        stage.setTitle("JavaFX Game Engine");
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            System.out.println("Window is closing!");
            System.exit(0);
        });

        stage.show();

        TileLoader tileLoader = new TileLoader();
        int tileSize = 100;
        TileSetLoader.loadSet("/assets/tileSets/testTiles.txt", tileLoader);
        GameMap map = MapLoader.loadMap("/assets/maps/testMap.txt", tileSize, tileLoader);

        DummyEntity dummyEntity = new DummyEntity(100, 100, "idle", "/assets/images/PlayerIdle.png", 100, 100, 500, 500, 30*100, 15*100);

        dummyEntity.getComponent(InteractiveComponent.class).mapInput(KeyCode.W, () -> moveUp(dummyEntity), () -> counterVertical(dummyEntity));
        dummyEntity.getComponent(InteractiveComponent.class).mapInput(KeyCode.S, () -> moveDown(dummyEntity), () -> counterVertical(dummyEntity));
        dummyEntity.getComponent(InteractiveComponent.class).mapInput(KeyCode.A, () -> moveLeft(dummyEntity), () -> counterHorizontal(dummyEntity));
        dummyEntity.getComponent(InteractiveComponent.class).mapInput(KeyCode.D, () -> moveRight(dummyEntity), () -> counterHorizontal(dummyEntity));
        dummyEntity.getComponent(InteractiveComponent.class).mapInput(MouseButton.PRIMARY, () -> {dummyEntity.getComponent(PositionComponent.class).setX(100); dummyEntity.getComponent(PositionComponent.class).setY(200);}, () -> {dummyEntity.getComponent(PositionComponent.class).setX(500); dummyEntity.getComponent(PositionComponent.class).setY(300);});

//        DummyEntity dummyEntity2 = new DummyEntity(200, 200, "idle", "/assets/images/PlayerIdle.png", 50, 50);
//
//        dummyEntity2.getComponent(InteractiveComponent.class).mapInput(KeyCode.W, () -> moveUp(dummyEntity2), () -> counterVertical(dummyEntity2));
//        dummyEntity2.getComponent(InteractiveComponent.class).mapInput(KeyCode.S, () -> moveDown(dummyEntity2), () -> counterVertical(dummyEntity2));
//        dummyEntity2.getComponent(InteractiveComponent.class).mapInput(KeyCode.A, () -> moveLeft(dummyEntity2), () -> counterHorizontal(dummyEntity2));
//        dummyEntity2.getComponent(InteractiveComponent.class).mapInput(KeyCode.D, () -> moveRight(dummyEntity2), () -> counterHorizontal(dummyEntity2));

        for (TileEntity[] row : map.getMapData()) {
            entities.addAll(Arrays.asList(row));
        }
        entities.add(dummyEntity);
//        entities.add(dummyEntity2);

        systemHub.addSystem(InputHandlingSystem.class, new InputHandlingSystem(new KeyboardInputHandler(scene), new MouseInputHandler(scene)),4);
        systemHub.addSystem(MovementSystem.class, new MovementSystem(),3);
        systemHub.addSystem(CollisionSystem.class, new CollisionSystem(),5);
        systemHub.addSystem(RenderSystem.class, new RenderSystem(gc, dummyEntity.getComponent(CameraComponent.class)),2);
        systemHub.addSystem(CameraSystem.class, new CameraSystem(dummyEntity, dummyEntity.getComponent(CameraComponent.class)), 8);
        systemHub.addSystem(AnimationSystem.class, new AnimationSystem(), 1);
        systemHub.addSystem(DebugInfoSystem.class, new DebugInfoSystem(),6);
        systemHub.addSystem(ResourceSystem.class, new ResourceSystem(),7);

        GameLoop gameLoop = new GameLoop(60) {
            @Override
            public void update() {
                for (GameSystem system : SystemHub.getInstance().getAllSystemsInPriorityOrder()) {
                    system.update(1.0f/60.0f, entities);
                }
            }
        };

        gameLoop.startLoop();
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
