package inf.elte.hu.gameengine_javafx.Systems;

import inf.elte.hu.gameengine_javafx.Core.*;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Entities.DebugInfoEntity;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.util.List;
import java.util.Map;

public class DebugInfoSystem extends GameSystem {
    @Override
    public void update(float deltaTime, List<Entity> entities) {
        TextArea statusTextArea = null;

        for (Entity e : entities) {
            if (e.getClass() == DebugInfoEntity.class) {
                DebugInfoEntity info = (DebugInfoEntity) e;
                statusTextArea = (TextArea) info.getTextArea();

                if (statusTextArea == null) {
                    System.out.println("Error: TextArea is null for DebugInfoEntity");
                    return;
                }
                break;
            }
        }

        if (statusTextArea == null) {
            return;
        }

        StringBuilder sb = new StringBuilder();
//        sb.append("Interactive entities status:\n");
//
//        for (Entity entity : entities) {
//            if (entity.getAllComponents().containsKey(InteractiveComponent.class)) {
//                for (Component component : entity.getAllComponents().values()) {
//                    sb.append(component.getStatus()).append("\n");
//                }
//            }
//        }
//
//        sb.append("\nSystems currently running:\n");
//
//        for (GameSystem system : SystemHub.getInstance().getAllSystemsInPriorityOrder()) {
//            sb.append(system.getClass().getSimpleName()).append("\n");
//        }

        sb.append("\nResources currently loaded:\n");

        for (Map.Entry<Class<?>, ResourceManager<?>> entry : ResourceHub.getInstance().getAllResourceManagers().entrySet()) {
            Class<?> resourceType = entry.getKey();
            ResourceManager<?> manager = entry.getValue();

            sb.append(resourceType.getSimpleName()).append(":\n");

            for (String resourceKey : manager.getResources().keySet()) {
                sb.append("  - ").append(resourceKey).append("\n");
            }
        }

        TextArea finalStatusTextArea = statusTextArea;
        Platform.runLater(() -> finalStatusTextArea.setText(sb.toString()));
    }
}
