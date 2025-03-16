package inf.elte.hu.gameengine_javafx.Misc.EventHandling;

public interface EventListener<T extends Event> {
    void onEvent(T event);
}

