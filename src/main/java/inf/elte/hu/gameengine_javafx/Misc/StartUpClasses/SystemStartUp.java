package inf.elte.hu.gameengine_javafx.Misc.StartUpClasses;

import inf.elte.hu.gameengine_javafx.Core.SystemHub;
import inf.elte.hu.gameengine_javafx.Systems.*;
import inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems.CollisionSystem;
import inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems.MovementSystem;
import inf.elte.hu.gameengine_javafx.Systems.RenderingSystems.*;
import inf.elte.hu.gameengine_javafx.Systems.ResourceSystems.*;

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
        systemHub.addSystem(LightingSystem.class, new LightingSystem(),3);
        systemHub.addSystem(PathfindingSystem.class, new PathfindingSystem(),4);
        systemHub.addSystem(MovementSystem.class, new MovementSystem(),5);
        systemHub.addSystem(ParticleSystem.class, new ParticleSystem(),6);
        systemHub.addSystem(InputHandlingSystem.class, new InputHandlingSystem(),7);
        systemHub.addSystem(CollisionSystem.class, new CollisionSystem(),8);
        systemHub.addSystem(ResourceSystem.class, new ResourceSystem(),9);
        systemHub.addSystem(CameraSystem.class, new CameraSystem(), 10);
        systemHub.addSystem(SoundSystem.class, new SoundSystem(), 11);
        systemHub.addSystem(InfiniteWorldLoaderSystem.class, new InfiniteWorldLoaderSystem(), 12);
    }

    public void startUpSceneManagementSystem() {
        SystemHub.getInstance().addSystem(SceneManagementSystem.class, new SceneManagementSystem(), 13);
    }
}
