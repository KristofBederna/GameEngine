package core;

import components.ColorComponent;
import views.GameFrame;
import views.GamePanel;
import systems.BackgroundSystem;
import entities.BackgroundEntity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private GameFrame Frame;
    private GamePanel Panel;
    private GameLoop GameLoop;
    private BackgroundSystem backgroundSystem;
    private List<Entity> entities;

    public Game(String title, int width, int height) {
        Frame = new GameFrame(title, width, height);
        Panel = new GamePanel();
        entities = new ArrayList<>();

        BackgroundEntity backgroundEntity = new BackgroundEntity();
        entities.add(backgroundEntity);

        backgroundSystem = new BackgroundSystem();
    }

    public void start() {
        Panel.setPreferredSize(new Dimension(500, 500));
        Panel.setBackground(Color.BLACK);
        Panel.setLayout(new BorderLayout());
        Frame.add(Panel);
        Frame.pack();
        Frame.setVisible(true);

        GameLoop = new GameLoop(60) {
            @Override
            public void update() {
                backgroundSystem.update(1.0f / 60.0f, entities);

                ColorComponent colorComponent = entities.getFirst().getComponent(ColorComponent.class);
                if (colorComponent != null) {
                    Panel.setBackground(colorComponent.getColor());
                }
                Panel.repaint();
            }
        };

        GameLoop.startLoop();
    }

    public void stop() {
        GameLoop.stopLoop();
    }
}
