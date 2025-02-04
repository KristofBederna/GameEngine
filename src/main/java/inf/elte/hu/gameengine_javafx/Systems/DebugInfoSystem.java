package inf.elte.hu.gameengine_javafx.Systems;

import inf.elte.hu.gameengine_javafx.Components.InteractiveComponent;
import inf.elte.hu.gameengine_javafx.Core.*;
import inf.elte.hu.gameengine_javafx.Entities.DebugInfoEntity;
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
                break;
            }
        }
        if (statusTextArea == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
//        sb.append("Interactive entities status:");
//        for (Entity entity : entities) {
//            if (entity.getAllComponents().containsKey(InteractiveComponent.class)) {
//                for (Component component : entity.getAllComponents().values()) {
//                    sb.append(component.getStatus()).append("\n");
//                }
//            }
//        }

//        sb.append("Systems currently running:\n");
//        for (GameSystem system : SystemHub.getInstance().getAllSystems()) {
//            sb.append(system.getClass().getSimpleName()).append("\n");
//        }
//        sb.append("\n");

        sb.append("Resources currently loaded:\n");
        for (Map.Entry<Class<?>, ResourceManager<?>> entry : ResourceHub.getInstance().getAllResourceManagers().entrySet()) {
            Class<?> resourceType = entry.getKey();
            ResourceManager<?> manager = entry.getValue();

            sb.append(resourceType.getSimpleName()).append(":\n");

            for (String resourceKey : manager.getResources().keySet()) {
                sb.append("  - ").append(resourceKey).append("\n");
            }
        }


        statusTextArea.setText(sb.toString());
    }
}
