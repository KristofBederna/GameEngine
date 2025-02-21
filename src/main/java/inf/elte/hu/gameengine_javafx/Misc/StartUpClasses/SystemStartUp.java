package inf.elte.hu.gameengine_javafx.Misc.StartUpClasses;

import inf.elte.hu.gameengine_javafx.Core.SystemHub;
import inf.elte.hu.gameengine_javafx.Systems.*;

public class SystemStartUp {
    public SystemStartUp() {
        SystemHub.getInstance();
        startUpSystems();
    }

    public void startUpSystems() {
        //Define systems to be started up here
        SystemHub systemHub = SystemHub.getInstance();
        systemHub.addSystem(AnimationSystem.class, new AnimationSystem(), 1);
        systemHub.addSystem(RenderSystem.class, new RenderSystem(),2);
        systemHub.addSystem(MovementSystem.class, new MovementSystem(),3);
        systemHub.addSystem(InputHandlingSystem.class, new InputHandlingSystem(),4);
        systemHub.addSystem(CollisionSystem.class, new CollisionSystem(),5);
        systemHub.addSystem(ResourceSystem.class, new ResourceSystem(),6);
        systemHub.addSystem(CameraSystem.class, new CameraSystem(), 7);
        systemHub.addSystem(SoundSystem.class, new SoundSystem(), 8);
        systemHub.addSystem(WorldLoaderSystem.class, new WorldLoaderSystem(), 9);
    }

    public void startUpSceneManagementSystem() {
        SystemHub.getInstance().addSystem(SceneManagementSystem.class, new SceneManagementSystem(), 10);
    }
}
