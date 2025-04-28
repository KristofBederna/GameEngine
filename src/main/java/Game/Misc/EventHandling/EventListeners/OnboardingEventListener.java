package Game.Misc.EventHandling.EventListeners;

import Game.Misc.EventHandling.Events.OnboardingEvent;
import inf.elte.hu.gameengine_javafx.Misc.EventHandling.EventListener;

public class OnboardingEventListener implements EventListener<OnboardingEvent> {
    @Override
    public void onEvent(OnboardingEvent event) {
        //Declare what the event listener should do while the event is happening.
    }

    @Override
    public void onExit(OnboardingEvent event) {
        //Declare what the event listener should do when the event ends.
    }
}
