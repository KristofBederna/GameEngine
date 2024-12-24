package core;

import components.MoveableComponent;
import components.VelocityComponent;
import entities.ImageEntity;
import systems.InputHandlingSystem;
import systems.KeyboardInputHandler;
import systems.MovementSystem;
import systems.RenderSystem;
import views.GameFrame;
import views.GamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private GameFrame frame;
    private GamePanel panel;
    private GameLoop gameLoop;
    private RenderSystem renderSystem;
    private MovementSystem movementSystem;
    private InputHandlingSystem inputHandlingSystem;
    private List<Entity> entities;

    public Game(String title, int width, int height) {
        frame = new GameFrame(title, width, height);
        panel = new GamePanel();
        entities = new ArrayList<>();

        //Testing code
        ImageEntity moveable = new ImageEntity(100, 100, "/assets/test.png");
        moveable.getComponent(MoveableComponent.class).mapInput(KeyEvent.VK_W, () -> {
                moveable.getComponent(VelocityComponent.class).setDy(-1);
                java.lang.System.out.println("moving up");
            }, () -> {
            moveable.getComponent(VelocityComponent.class).setDy(0);
            java.lang.System.out.println("stop moving up");
        });
        moveable.getComponent(MoveableComponent.class).mapInput(KeyEvent.VK_S, () -> moveable.getComponent(VelocityComponent.class).setDy(1), () -> moveable.getComponent(VelocityComponent.class).setDy(0));
        moveable.getComponent(MoveableComponent.class).mapInput(KeyEvent.VK_A, () -> moveable.getComponent(VelocityComponent.class).setDx(-1), () -> moveable.getComponent(VelocityComponent.class).setDx(0));
        moveable.getComponent(MoveableComponent.class).mapInput(KeyEvent.VK_D, () -> moveable.getComponent(VelocityComponent.class).setDx(1), () -> moveable.getComponent(VelocityComponent.class).setDx(0));

        entities.add(moveable);

        renderSystem = new RenderSystem(panel);
        movementSystem = new MovementSystem();
        KeyboardInputHandler inputHandler = new KeyboardInputHandler(this.panel);
        inputHandlingSystem = new InputHandlingSystem(inputHandler);
        //Testing code end
    }

    public void start() {
        panel.setPreferredSize(new Dimension(500, 500));
        panel.setBackground(Color.BLACK);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        panel.requestFocusInWindow();

        gameLoop = new GameLoop(60) {
            @Override
            public void update() {
                //Testing code
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
