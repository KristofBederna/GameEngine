package inf.elte.hu.gameengine_javafx.Components.PropertyComponents;

import inf.elte.hu.gameengine_javafx.Components.ParentComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;

public class PositionComponent extends Component {
    private double localX;
    private double localY;
    private double globalX;
    private double globalY;

    public PositionComponent(double localX, double localY, Entity entity) {
        this.localX = localX;
        this.localY = localY;
        updateGlobalPosition(entity);
    }

    public double getLocalX() {
        return localX;
    }

    public double getLocalY() {
        return localY;
    }

    public double getGlobalX() {
        return globalX;
    }

    public double getGlobalY() {
        return globalY;
    }

    public void setLocalX(double localX,  Entity entity) {
        this.localX = localX;
        updateGlobalPosition(entity);
    }

    public void setLocalY(double localY,  Entity entity) {
        this.localY = localY;
        updateGlobalPosition(entity);
    }

    public void setLocalPosition(double x, double y,  Entity entity) {
        this.localX = x;
        this.localY = y;
        updateGlobalPosition(entity);
    }

    public void updateGlobalPosition(Entity entity) {
        if (entity != null) {
            ParentComponent parentComponent = entity.getComponent(ParentComponent.class);
            if (parentComponent != null && parentComponent.getParent() != null) {
                PositionComponent parentPosition = parentComponent.getParent().getComponent(PositionComponent.class);
                if (parentPosition != null) {
                    this.globalX = parentPosition.getGlobalX() + localX;
                    this.globalY = parentPosition.getGlobalY() + localY;
                    return;
                }
            }
        }
        this.globalX = localX;
        this.globalY = localY;
    }

    @Override
    public String getStatus() {
        return this.getClass().getSimpleName() +
                ": Local(X: " + localX + ", Y: " + localY + ") | Global(X: " + globalX + ", Y: " + globalY + ")";
    }
}
