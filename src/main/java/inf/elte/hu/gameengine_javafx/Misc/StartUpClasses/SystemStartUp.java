package inf.elte.hu.gameengine_javafx.Misc.StartUpClasses;

import inf.elte.hu.gameengine_javafx.Core.SystemHub;
import inf.elte.hu.gameengine_javafx.Systems.*;
import inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems.CollisionSystem;
import inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems.MovementSystem;
import inf.elte.hu.gameengine_javafx.Systems.RenderingSystems.AnimationSystem;
import inf.elte.hu.gameengine_javafx.Systems.RenderingSystems.CameraSystem;
import inf.elte.hu.gameengine_javafx.Systems.RenderingSystems.RenderSystem;
import inf.elte.hu.gameengine_javafx.Systems.ResourceSystems.ResourceSystem;
import inf.elte.hu.gameengine_javafx.Systems.ResourceSystems.SceneManagementSystem;
import inf.elte.hu.gameengine_javafx.Systems.ResourceSystems.SoundSystem;
import inf.elte.hu.gameengine_javafx.Systems.ResourceSystems.WorldLoaderSystem;

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
        systemHub.addSystem(MovementSystem.class, new MovementSystem(),4);
        systemHub.addSystem(InputHandlingSystem.class, new InputHandlingSystem(),5);
        systemHub.addSystem(CollisionSystem.class, new CollisionSystem(),6);
        systemHub.addSystem(ResourceSystem.class, new ResourceSystem(),7);
        systemHub.addSystem(CameraSystem.class, new CameraSystem(), 8);
        systemHub.addSystem(SoundSystem.class, new SoundSystem(), 9);
        systemHub.addSystem(WorldLoaderSystem.class, new WorldLoaderSystem(), 10);
    }

    public void startUpSceneManagementSystem() {
        SystemHub.getInstance().addSystem(SceneManagementSystem.class, new SceneManagementSystem(), 11);
    }
}
