package inf.elte.hu.gameengine_javafx.Entities;

import inf.elte.hu.gameengine_javafx.Components.DirectionComponent;
import inf.elte.hu.gameengine_javafx.Components.ParentComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.DimensionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ColorComponent;
import inf.elte.hu.gameengine_javafx.Components.ShapeComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Shape;
import inf.elte.hu.gameengine_javafx.Misc.Direction;
import javafx.scene.paint.Color;

public class ParticleEmitterEntity extends Entity {
    public ParticleEmitterEntity(double x, double y, ParticleEntity particleEntity) {
        ParentComponent parent = new ParentComponent();
        parent.addChild(particleEntity);
        particleEntity.getComponent(ParentComponent.class).setParent(this);
        for (int i = 0; i < 100; i++) {
            ParticleEntity copy = ParticleEntity.hardCopySelf(particleEntity);
            if (copy == null) {
                continue;
            }
            copy.getComponent(ParentComponent.class).setParent(this);
            parent.addChild(copy);
        }
        addComponent(parent);
        addComponent(new DirectionComponent(Direction.UP));
        addComponent(new PositionComponent(x, y, this));

        addToManager();
    }
}
