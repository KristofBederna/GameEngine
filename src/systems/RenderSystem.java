package systems;

import components.ImageComponent;
import components.PositionComponent;
import core.Entity;
import views.GamePanel;

import java.util.List;

public class RenderSystem extends core.System {
    private GamePanel panel;
    private List<Entity> entities;

    public RenderSystem(GamePanel panel) {
        this.panel = panel;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
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
