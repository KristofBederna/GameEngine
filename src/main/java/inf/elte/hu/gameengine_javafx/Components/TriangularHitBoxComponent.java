package inf.elte.hu.gameengine_javafx.Components;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Triangle;

public class TriangularHitBoxComponent extends Component {
    private Triangle hitBox;

    public TriangularHitBoxComponent(Triangle hitBox) {
        this.hitBox = hitBox;
    }

    public Triangle getHitBox() {
        return hitBox;
    }
    public void setHitBox(Triangle hitBox) {
        this.hitBox = hitBox;
    }

    @Override
    public String getStatus() {
        return "";
    }
}
