package inf.elte.hu.gameengine_javafx.Components;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.NSidedShape;

public class NSidedHitBoxComponent extends Component {
    private NSidedShape hitBox;

    public NSidedHitBoxComponent(NSidedShape hitBox) {
        this.hitBox = hitBox;
    }

    public NSidedShape getHitBox() {
        return hitBox;
    }

    public void setHitBox(NSidedShape hitBox) {
        this.hitBox = hitBox;
    }

    @Override
    public String getStatus() {
        return "";
    }
}
