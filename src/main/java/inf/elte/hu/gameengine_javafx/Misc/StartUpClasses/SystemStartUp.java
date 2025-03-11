package inf.elte.hu.gameengine_javafx.Misc.StartUpClasses;

import inf.elte.hu.gameengine_javafx.Core.SystemHub;
import inf.elte.hu.gameengine_javafx.Systems.*;
import inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems.CollisionSystem;
import inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems.MovementSystem;
import inf.elte.hu.gameengine_javafx.Systems.RenderingSystems.*;
import inf.elte.hu.gameengine_javafx.Systems.ResourceSystems.*;

public class SystemStartUp {
    public SystemStartUp(Runnable startUpMethod) {
        SystemHub.getInstance();
        startUpSystems(startUpMethod);
    }

    public void startUpSystems(Runnable startUpMethod) {
        startUpMethod.run();
    }

    public void startUpSceneManagementSystem() {
        SystemHub.getInstance().addSystem(SceneManagementSystem.class, new SceneManagementSystem(), 9999);
    }
}
