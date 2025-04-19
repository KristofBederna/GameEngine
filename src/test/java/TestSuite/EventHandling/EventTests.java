package TestSuite.EventHandling;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
