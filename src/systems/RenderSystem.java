package systems;

import components.ImageComponent;
import components.PositionComponent;
import core.Entity;
import core.GameMap;
import entities.TileEntity;
import views.GamePanel;

import java.awt.*;
import java.util.List;

public class RenderSystem extends core.System {
    private GamePanel panel;
    private List<Entity> entities;
    private GameMap map;

    public RenderSystem(GamePanel panel) {
        this.panel = panel;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    @Override
    public void update(float deltaTime, List<Entity> entities) {
        setEntities(entities);

        panel.setRenderCallback(g -> {
            for (Entity entity : entities) {
                PositionComponent position = entity.getComponent(PositionComponent.class);
                ImageComponent img = entity.getComponent(ImageComponent.class);
                if (position != null && img != null) {
                    int width = (img.getWidth() >= 0) ? img.getWidth() : img.getImage().getWidth(null);
                    int height = (img.getHeight() >= 0) ? img.getHeight() : img.getImage().getHeight(null);

                    g.drawImage(img.getImage(), position.getX(), position.getY(), width, height, null);
                }
            }
        });

        panel.repaint();
    }
}
