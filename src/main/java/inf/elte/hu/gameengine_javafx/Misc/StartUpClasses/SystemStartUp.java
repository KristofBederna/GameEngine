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
        SystemHub.getInstance().addSystem(AnimationSystem.class, new AnimationSystem(), 1);
        SystemHub.getInstance().addSystem(RenderSystem.class, new RenderSystem(),2);
        SystemHub.getInstance().addSystem(MovementSystem.class, new MovementSystem(),3);
        SystemHub.getInstance().addSystem(InputHandlingSystem.class, new InputHandlingSystem(),4);
        SystemHub.getInstance().addSystem(CollisionSystem.class, new CollisionSystem(),5);
        //SystemHub.getInstance().addSystem(LogSystem.class, new LogSystem(),6);
        SystemHub.getInstance().addSystem(ResourceSystem.class, new ResourceSystem(),7);
        SystemHub.getInstance().addSystem(CameraSystem.class, new CameraSystem(), 8);
        SystemHub.getInstance().addSystem(SoundSystem.class, new SoundSystem(), 9);
    }
}
