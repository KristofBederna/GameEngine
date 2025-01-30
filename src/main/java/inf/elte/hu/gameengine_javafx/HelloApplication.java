package inf.elte.hu.gameengine_javafx;

import inf.elte.hu.gameengine_javafx.Components.*;
import inf.elte.hu.gameengine_javafx.Core.Entity;
import inf.elte.hu.gameengine_javafx.Entities.DummyEntity;
import inf.elte.hu.gameengine_javafx.Entities.TileEntity;
import inf.elte.hu.gameengine_javafx.Misc.*;
import inf.elte.hu.gameengine_javafx.Systems.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelloApplication extends Application {
    private InputHandlingSystem inputHandlingSystem;
    private MovementSystem movementSystem;
    private CollisionSystem collisionSystem;
    private RenderSystem renderSystem;
    private AnimationSystem animationSystem;
    private List<Entity> entities = new ArrayList<>();

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(1000, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, 1000, 500);

        stage.setTitle("JavaFX Game Engine");
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> {
            System.out.println("Window is closing!");
            System.exit(0);
        });

        stage.show();

        KeyboardInputHandler inputHandler = new KeyboardInputHandler(scene);
        inputHandlingSystem = new InputHandlingSystem(inputHandler);
        movementSystem = new MovementSystem();
        collisionSystem = new CollisionSystem();
        renderSystem = new RenderSystem(gc);
        animationSystem = new AnimationSystem();

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
                animationSystem.update(1.0f / 60.0f, entities);
                renderSystem.update(1.0f / 60.0f, entities);
                movementSystem.update(1.0f / 60.0f, entities);
                inputHandlingSystem.update(1.0f / 60.0f, entities);
                collisionSystem.update(1.0f / 60.0f, entities);
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
