package inf.elte.hu.gameengine_javafx.Components.RenderingComponents;

import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.DimensionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.*;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Rectangle;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import javax.swing.text.Position;
import java.awt.*;

public class ParticleComponent<T> extends Component {
    T particle;
    Color color;

    public ParticleComponent(T particle) {
        this.particle = particle;
    }
    public ParticleComponent(T particle, Color color) {
        this.particle = particle;
        this.color = color;
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
            } else if (particle instanceof Shape) {
                switch (particle) {
                    case NSidedShape nSidedShape -> nSidedShape.renderFill(gc, color);
                    case ComplexShape complexShape -> complexShape.render(gc, color);
                    case Line line -> line.render(gc, color, 2);
                    case Rectangle rectangle -> rectangle.render(gc, color);
                    case Triangle triangle -> triangle.render(gc, color);
                    default -> {
                    }
                }
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
