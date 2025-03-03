package inf.elte.hu.gameengine_javafx.Entities;

import inf.elte.hu.gameengine_javafx.Components.LightComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.RadiusComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ColorComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Misc.LightType;
import javafx.scene.paint.Color;

public class LightingEntity extends Entity {
    public LightingEntity(double x, double y, LightType type, double intensity, Color color, double radius) {
        addComponent(new LightComponent(type, intensity));
        addComponent(new ColorComponent(color));
        addComponent(new RadiusComponent(radius));
        addComponent(new PositionComponent(x, y, this));

        addToManager();
    }
}
