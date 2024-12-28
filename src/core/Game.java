package core;

import assets.GlobalPaths;
import components.InteractiveComponent;
import components.StateComponent;
import components.VelocityComponent;
import entities.ImageEntity;
import entities.TileEntity;
import miscs.KeyboardInputHandler;
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
    private int tileSize;

    public Game(String title) {
        frame = new GameFrame(title);
        panel = new GamePanel();
        panel.setBackground(Color.BLACK);
        entities = new ArrayList<>();

        //Testing code
        tileLoader = new TileLoader();
        tileSize = 25;
        TileSetLoader.loadSet(GlobalPaths.TileSetsPath + "testTiles.txt", tileLoader);
        map = MapLoader.loadMap(GlobalPaths.MapsPath + "testMap.txt", tileSize, tileLoader);

        ImageEntity moveable = new ImageEntity(50, 50, GlobalPaths.ImagesPath + "PlayerIdle.png");

        moveable.getComponent(InteractiveComponent.class).mapInput(KeyEvent.VK_W, () -> moveUp(moveable), () -> counterVertical(moveable));
        moveable.getComponent(InteractiveComponent.class).mapInput(KeyEvent.VK_S, () -> moveDown(moveable), () -> counterVertical(moveable));
        moveable.getComponent(InteractiveComponent.class).mapInput(KeyEvent.VK_A, () -> moveLeft(moveable), () -> counterHorizontal(moveable));
        moveable.getComponent(InteractiveComponent.class).mapInput(KeyEvent.VK_D, () -> moveRight(moveable), () -> counterHorizontal(moveable));

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
        panel.setPreferredSize(new Dimension(map.getWidth()*tileSize, map.getHeight()*tileSize));
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

    //Custom functions
    private void moveUp(Entity e) {
        e.getComponent(VelocityComponent.class).setDy(-1);
        e.getComponent(StateComponent.class).setCurrentState("up");
    }
    private void moveDown(Entity e) {
        e.getComponent(VelocityComponent.class).setDy(1);
        e.getComponent(StateComponent.class).setCurrentState("down");
    }
    private void moveLeft(Entity e) {
        e.getComponent(VelocityComponent.class).setDx(-1);
        e.getComponent(StateComponent.class).setCurrentState("left");
    }
    private void moveRight(Entity e) {
        e.getComponent(VelocityComponent.class).setDx(1);
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
