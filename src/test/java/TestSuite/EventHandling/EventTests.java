package TestSuite.EventHandling;

import inf.elte.hu.gameengine_javafx.Misc.EventHandling.Event;
import inf.elte.hu.gameengine_javafx.Misc.EventHandling.EventListener;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventTests {
    @Test
    public void testEvent() {
        ArrayList<String> test = new ArrayList<>();
        TestEvent testEvent = new TestEvent(test);
        TestEventListener testListener = new TestEventListener();

        testListener.onEvent(testEvent);

        assertEquals(1, test.size());
        assertEquals(test.getFirst(), testEvent.getTestString());

        testListener.onExit(testEvent);
        assertEquals(0, test.size());
    }
}
