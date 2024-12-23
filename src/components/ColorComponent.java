package components;

import core.Component;
import java.awt.*;

public class ColorComponent extends Component {
    private Color color;

    public ColorComponent(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
