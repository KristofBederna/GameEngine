package inf.elte.hu.gameengine_javafx.Entities;

import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.DimensionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ParticleComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ZIndexComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.NSidedShape;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import javafx.scene.paint.Color;

public class ParticleEntity extends Entity {
    public ParticleEntity() {
        this.addComponent(new ParticleComponent<>(new NSidedShape(new Point(500, 500), 100, 32),Color.PINK));
        this.addComponent(new PositionComponent(500, 500, this));
        this.addComponent(new DimensionComponent(100, 100));
        this.addComponent(new ZIndexComponent(3));

        addToManager();
    }
}
