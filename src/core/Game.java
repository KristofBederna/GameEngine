package core;

import entities.ImageEntity;
import systems.MovementSystem;
import systems.RenderSystem;
import views.GameFrame;
import views.GamePanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private GameFrame frame;
    private GamePanel panel;
    private GameLoop gameLoop;
    private RenderSystem renderSystem;
    private MovementSystem movementSystem;
    private List<Entity> entities;

    public Game(String title, int width, int height) {
        frame = new GameFrame(title, width, height);
        panel = new GamePanel();
        entities = new ArrayList<>();

        entities.add(new ImageEntity(100, 100, "/assets/test.png"));

        renderSystem = new RenderSystem(panel);
        movementSystem = new MovementSystem();
    }

    public void start() {
        panel.setPreferredSize(new Dimension(500, 500));
        panel.setBackground(Color.BLACK);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        gameLoop = new GameLoop(60) {
            @Override
            public void update() {
                renderSystem.update(1.0f / 60.0f, entities);
                movementSystem.update(1.0f / 60.0f, entities);
            }
        };

        gameLoop.startLoop();
    }

    public void stop() {
        gameLoop.stopLoop();
    }
}
