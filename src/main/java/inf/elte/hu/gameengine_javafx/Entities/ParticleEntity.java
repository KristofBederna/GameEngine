package inf.elte.hu.gameengine_javafx.Entities;

import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.DimensionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ColorComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ImageComponent;
import inf.elte.hu.gameengine_javafx.Components.ShapeComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.*;
import inf.elte.hu.gameengine_javafx.Misc.Time;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class ParticleEntity extends Entity {
    public ParticleEntity(double x, double y, double width, double height, Shape shape, Color color) {
        addComponent(new PositionComponent(x, y, this));
        addComponent(new VelocityComponent());
        addComponent(new DimensionComponent(width, height));
        addComponent(new ShapeComponent<>(shape));
        addComponent(new ColorComponent(color));

        addToManager();
    }
    public ParticleEntity(double x, double y, double width, double height, String imagePath) {
        addComponent(new PositionComponent(x, y, this));
        addComponent(new VelocityComponent());
        addComponent(new DimensionComponent(width, height));
        addComponent(new ImageComponent(imagePath, width, height));

        addToManager();
    }

    public void render(GraphicsContext gc) {
        if (getComponent(ImageComponent.class) != null) {
            return;
        }
        ShapeComponent shapeComponent = getComponent(ShapeComponent.class);
        if (shapeComponent != null) {
            Shape shape = shapeComponent.getShape();
            if (shape instanceof Rectangle) {
                ((Rectangle)shape).renderFill(gc, getComponent(ColorComponent.class).getColor());
            } else if (shape instanceof ComplexShape) {
                ((ComplexShape)shape).renderFill(gc, getComponent(ColorComponent.class).getColor());
            } else if (shape instanceof Line) {
                ((Line)shape).render(gc, getComponent(ColorComponent.class).getColor(), 5);
            } else if (shape instanceof NSidedShape) {
                ((NSidedShape)shape).renderFill(gc, getComponent(ColorComponent.class).getColor());
            } else if (shape instanceof Triangle) {
                ((Triangle)shape).renderFill(gc, getComponent(ColorComponent.class).getColor());
            }
        }
    }

    public void alignShapeWithEntity(Entity entity) {
        Shape shape = entity.getComponent(ShapeComponent.class).getShape();
        PositionComponent positionComponent = getComponent(PositionComponent.class);
        double x = positionComponent.getGlobalX();
        double y = positionComponent.getGlobalY();

        if (shape instanceof Rectangle) {
            ((Rectangle)shape).moveTo(new Point(x, y));
        } else if (shape instanceof ComplexShape) {
            ((ComplexShape)shape).moveTo(new Point(x, y));
        } else if (shape instanceof Line) {
            ((Line)shape).moveTo(new Point(x, y));
        } else if (shape instanceof NSidedShape) {
            ((NSidedShape)shape).moveTo(new Point(x, y));
        } else if (shape instanceof Triangle) {
            ((Triangle)shape).moveTo(new Point(x, y));
        }
    }
}
