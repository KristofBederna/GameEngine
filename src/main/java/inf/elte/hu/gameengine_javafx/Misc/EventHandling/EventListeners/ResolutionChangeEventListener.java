package inf.elte.hu.gameengine_javafx.Misc.EventHandling.EventListeners;

import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import inf.elte.hu.gameengine_javafx.Misc.EventHandling.EventListener;
import inf.elte.hu.gameengine_javafx.Misc.EventHandling.Events.ResolutionChangeEvent;
import inf.elte.hu.gameengine_javafx.Misc.Layers.GameCanvas;

public class ResolutionChangeEventListener implements EventListener<ResolutionChangeEvent> {
    @Override
    public void onEvent(ResolutionChangeEvent event) {
        GameCanvas canvas = GameCanvas.getInstance();
        canvas.setWidth(event.getWidth());
        canvas.setHeight(event.getHeight());
        CameraEntity.getInstance().setHeight(canvas.getHeight());
        CameraEntity.getInstance().setWidth(canvas.getWidth());
    }
}
