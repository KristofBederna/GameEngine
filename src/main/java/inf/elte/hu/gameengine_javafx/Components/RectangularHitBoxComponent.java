package inf.elte.hu.gameengine_javafx.Components;

import inf.elte.hu.gameengine_javafx.Core.Component;

import java.awt.*;

public class RectangularHitBoxComponent extends Component {
    private Rectangle hitBox;
    public RectangularHitBoxComponent(int x, int y, int width, int height) {
        this.hitBox = new Rectangle(x, y, width, height);
    }
    public Rectangle getHitBox() {
        return hitBox;
    }
    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }
}
