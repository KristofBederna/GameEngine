package inf.elte.hu.gameengine_javafx.Components;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Rectangle;


public class RectangularHitBoxComponent extends Component {
    private Rectangle hitBox;
    public RectangularHitBoxComponent(Point topLeft, Point topRight, Point bottomLeft, Point bottomRight) {
        this.hitBox = new Rectangle(topLeft, topRight, bottomLeft, bottomRight);
    }
    public Rectangle getHitBox() {
        return hitBox;
    }
    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    @Override
    public String getStatus() {
        return "";
    }
}
