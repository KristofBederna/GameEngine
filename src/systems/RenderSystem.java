package systems;

import components.ImageComponent;
import components.PositionComponent;
import core.Entity;
import core.GameMap;
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
            renderMap(map, g);
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

    private void renderMap(GameMap map, Graphics2D g) {
        int tileSize = 32;
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                int tileId = map.getElement(x, y);
                Color tileColor = getColorForTile(tileId);
                g.setColor(tileColor);
                g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
            }
        }
    }

    private Color getColorForTile(int tileId) {
        return switch (tileId) {
            case 0 -> Color.BLACK; // Empty space
            case 1 -> Color.GREEN; // Grass
            case 2 -> Color.GRAY;  // Stone
            case 3 -> Color.BLUE;  // Water
            default -> Color.RED;  // Error
        };
    }

}
