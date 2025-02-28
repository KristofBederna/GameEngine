package inf.elte.hu.gameengine_javafx.Components.HitBoxComponents;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.ComplexShape;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import java.util.List;

public class ComplexHitBoxComponent extends HitBoxComponent {
    private ComplexShape hitBox;

    public ComplexHitBoxComponent(List<Point> points) {
        this.hitBox = new ComplexShape(points);
    }

    public ComplexHitBoxComponent(ComplexShape hitBox) {
        this.hitBox = new ComplexShape(hitBox);
    }

    public ComplexShape getHitBox() {
        return hitBox;
    }

    public void setHitBox(ComplexShape hitBox) {
        this.hitBox = hitBox;
    }

    @Override
    public String getStatus() {
        return "ComplexHitBoxComponent with " + hitBox.getPoints().size() + " points";
    }
}