package components;

import core.Component;

import java.util.HashMap;
import java.util.Map;

public class MoveableComponent extends Component {
    private final Map<Integer, String> inputMapping;

    public MoveableComponent() {
        this.inputMapping = new HashMap<>();
    }

    public void mapInput(int keyCode, String direction) {
        inputMapping.put(keyCode, direction);
    }

    public Map<Integer, String> getInputMapping() {
        return inputMapping;
    }
}
