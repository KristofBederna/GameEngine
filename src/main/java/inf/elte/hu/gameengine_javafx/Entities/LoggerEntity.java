package inf.elte.hu.gameengine_javafx.Entities;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.EntityManager;
import javafx.scene.Node;
import javafx.scene.control.TextArea;

public class LoggerEntity extends Entity {
    private final TextArea debugInfoTextArea;

    public LoggerEntity() {
        debugInfoTextArea = new TextArea();
        debugInfoTextArea.setEditable(false);
        debugInfoTextArea.setFocusTraversable(false);
        debugInfoTextArea.setMouseTransparent(true);
        debugInfoTextArea.setPrefSize(300, 500);
        debugInfoTextArea.setWrapText(true);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addToManager() {
        EntityManager<LoggerEntity> manager = EntityHub.getInstance().getEntityManager((Class<LoggerEntity>)this.getClass());

        if (manager != null) {
            manager.register(this);
        } else {
            manager = new EntityManager<>();
            EntityHub.getInstance().addEntityManager(LoggerEntity.class, manager);
        }
    }

    public Node getTextArea() {
        return debugInfoTextArea;
    }
}
