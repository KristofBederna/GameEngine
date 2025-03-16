package inf.elte.hu.gameengine_javafx.Misc.EventHandling;

import java.util.*;

public class EventManager {
    private final Map<Class<? extends Event>, List<EventListener<?>>> listeners = new HashMap<>();

    public <T extends Event> void registerListener(Class<T> eventType, EventListener<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public <T extends Event> void fireEvent(T event) {
        List<EventListener<?>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            for (EventListener<?> listener : eventListeners) {
                ((EventListener<T>) listener).onEvent(event);
            }
        }
    }
}
