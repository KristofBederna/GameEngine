package inf.elte.hu.gameengine_javafx.Components;


import inf.elte.hu.gameengine_javafx.Core.Component;
import inf.elte.hu.gameengine_javafx.Misc.Tuple;
import javafx.scene.input.KeyCode;

import java.util.HashMap;
import java.util.Map;

public class InteractiveComponent extends Component {
    private final Map<KeyCode, Tuple<Runnable, Runnable>> inputMapping;

    public InteractiveComponent() {
        this.inputMapping = new HashMap<>();
    }

    public void mapInput(KeyCode keyCode, Runnable action) {
        inputMapping.put(keyCode, new Tuple<>(action, null));
    }

    public void mapInput(KeyCode keyCode, Runnable action, Runnable counterAction) {
        inputMapping.put(keyCode, new Tuple<>(action, counterAction));
    }

    public Map<KeyCode, Tuple<Runnable, Runnable>> getInputMapping() {
        return inputMapping;
    }
}
