package core;

import assets.GlobalPaths;
import components.AnimationComponent;
import components.InteractiveComponent;
import components.StateComponent;
import components.VelocityComponent;
import entities.ImageEntity;
import entities.TileEntity;
import miscs.MapLoader;
import miscs.TileLoader;
import miscs.TileSetLoader;
import systems.*;
import views.GameFrame;
import views.GamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    private GameFrame frame;
    private GamePanel panel;
    private GameLoop gameLoop;
    private RenderSystem renderSystem;
    private MovementSystem movementSystem;
    private InputHandlingSystem inputHandlingSystem;
    private AnimationSystem animationSystem;
    private GameMap map;
    private List<Entity> entities;
    private TileLoader tileLoader;

    public Game(String title, int width, int height) {
        frame = new GameFrame(title, width, height);
        panel = new GamePanel();
        panel.setBackground(Color.BLACK);
        entities = new ArrayList<>();

        //Testing code
        tileLoader = new TileLoader();
        TileSetLoader.loadSet(GlobalPaths.TileSetsPath + "testTiles.txt", tileLoader);
        map = MapLoader.loadMap(GlobalPaths.MapsPath + "testMap.txt", 25, tileLoader);


        ImageEntity moveable = new ImageEntity(50, 50, GlobalPaths.ImagesPath + "PlayerIdle.png");

        moveable.getComponent(InteractiveComponent.class).mapInput(KeyEvent.VK_W, () -> {
            moveable.getComponent(VelocityComponent.class).setDy(-1);
            moveable.getComponent(StateComponent.class).setCurrentState("up");
        }, () -> {
            moveable.getComponent(VelocityComponent.class).setDy(0);
            moveable.getComponent(StateComponent.class).setCurrentState("idle");
        });
        moveable.getComponent(InteractiveComponent.class).mapInput(KeyEvent.VK_S, () -> {
            moveable.getComponent(VelocityComponent.class).setDy(1);
            moveable.getComponent(StateComponent.class).setCurrentState("down");
        }, () -> {
            moveable.getComponent(VelocityComponent.class).setDy(0);
            moveable.getComponent(StateComponent.class).setCurrentState("idle");
        });
        moveable.getComponent(InteractiveComponent.class).mapInput(KeyEvent.VK_A, () -> {
            moveable.getComponent(VelocityComponent.class).setDx(-1);
            moveable.getComponent(StateComponent.class).setCurrentState("left");
        }, () -> {
            moveable.getComponent(VelocityComponent.class).setDx(0);
            moveable.getComponent(StateComponent.class).setCurrentState("idle");
        });
        moveable.getComponent(InteractiveComponent.class).mapInput(KeyEvent.VK_D, () -> {
            moveable.getComponent(VelocityComponent.class).setDx(1);
            moveable.getComponent(StateComponent.class).setCurrentState("right");
        }, () -> {
            moveable.getComponent(VelocityComponent.class).setDx(0);
            moveable.getComponent(StateComponent.class).setCurrentState("idle");
        });

        for (TileEntity[] row: map.getMapData()) {
            entities.addAll(Arrays.asList(row));
        }
        entities.add(moveable);

        animationSystem = new AnimationSystem();
        renderSystem = new RenderSystem(panel);
        movementSystem = new MovementSystem();
        KeyboardInputHandler inputHandler = new KeyboardInputHandler(this.panel);
        inputHandlingSystem = new InputHandlingSystem(inputHandler);
        //Testing code end
    }

    public void start() {
        panel.setPreferredSize(new Dimension(500, 500));
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        panel.requestFocusInWindow();

        gameLoop = new GameLoop(60) {
            @Override
            public void update() {
                //Testing code
                animationSystem.update(1.0f / 60.0f, entities);
                renderSystem.update(1.0f / 60.0f, entities);
                movementSystem.update(1.0f / 60.0f, entities);
                inputHandlingSystem.update(1.0f / 60.0f, entities);
                //Testing code end
            }
        };

        gameLoop.startLoop();
    }

    public void stop() {
        gameLoop.stopLoop();
    }
}
