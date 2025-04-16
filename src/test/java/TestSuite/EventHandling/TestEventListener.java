package TestSuite.EventHandling;

import inf.elte.hu.gameengine_javafx.Misc.EventHandling.Event;
import inf.elte.hu.gameengine_javafx.Misc.EventHandling.EventListener;

public class TestEventListener implements EventListener<TestEvent> {
    @Override
    public void onEvent(TestEvent event) {
        event.getTestList().add(event.getTestString());
    }

    @Override
    public void onExit(TestEvent event) {
        event.getTestList().remove(event.getTestString());
    }
}
