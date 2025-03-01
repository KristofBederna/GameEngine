package inf.elte.hu.gameengine_javafx.Components.RenderingComponents;

import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.DimensionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class ParticleComponent<T> extends Component {
    T particle;

    public ParticleComponent(T particle) {
        this.particle = particle;
    }

    public T getParticle() {
        return particle;
    }
    public void setParticle(T particle) {
        this.particle = particle;
    }

    public void render(GraphicsContext gc, Entity entity) {
        CameraEntity cameraEntity = CameraEntity.getInstance();
        double x = entity.getComponent(PositionComponent.class).getGlobalX() - cameraEntity.getComponent(PositionComponent.class).getGlobalX();
        double y = entity.getComponent(PositionComponent.class).getGlobalY() - cameraEntity.getComponent(PositionComponent.class).getGlobalY();

        if (particle != null) {
            if (particle instanceof Image) {
                gc.drawImage((Image) particle, x, y);
            } else if (particle instanceof Color) {
                gc.setFill((Color) particle);
                gc.fillOval(x, y,
                        entity.getComponent(DimensionComponent.class).getWidth(),
                        entity.getComponent(DimensionComponent.class).getHeight()
                );
            } else {
                System.err.println("Particle Class: " + particle.getClass() + " is not an Image or a Color");
            }
        }
    }


    @Override
    public String getStatus() {
        return "";
    }
}
