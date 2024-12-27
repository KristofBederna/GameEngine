package components;

import core.Component;
import miscs.Tuple;
import java.util.HashMap;
import java.util.Map;

public class InteractiveComponent extends Component {
    private final Map<Integer, Tuple<Runnable, Runnable>> inputMapping;

    public InteractiveComponent() {
        this.inputMapping = new HashMap<>();
    }

    public void mapInput(int keyCode, Runnable action) {
        inputMapping.put(keyCode, new Tuple<>(action, null));
    }

    public void mapInput(int keyCode, Runnable action, Runnable counterAction) {
        inputMapping.put(keyCode, new Tuple<>(action, counterAction));
    }

    public Map<Integer, Tuple<Runnable, Runnable>> getInputMapping() {
        return inputMapping;
    }
}
