package inf.elte.hu.gameengine_javafx.Misc;

import inf.elte.hu.gameengine_javafx.Components.CameraComponent;
import inf.elte.hu.gameengine_javafx.Core.Globals;
import inf.elte.hu.gameengine_javafx.Core.SystemHub;
import inf.elte.hu.gameengine_javafx.Misc.InputHandlers.KeyboardInputHandler;
import inf.elte.hu.gameengine_javafx.Misc.InputHandlers.MouseInputHandler;
import inf.elte.hu.gameengine_javafx.Systems.*;

import static inf.elte.hu.gameengine_javafx.Core.Globals.canvas;
public class SystemStartup {
    public SystemStartup() {
        SystemHub.getInstance();
        startUpSystems();
    }
    public void startUpSystems() {
        //Define systems to be started up here
        SystemHub.getInstance().addSystem(AnimationSystem.class, new AnimationSystem(), 1);
        SystemHub.getInstance().addSystem(RenderSystem.class, new RenderSystem(canvas.getGraphicsContext2D(), Globals.playerEntity.getComponent(CameraComponent.class)),2);
        SystemHub.getInstance().addSystem(MovementSystem.class, new MovementSystem(),3);
        SystemHub.getInstance().addSystem(InputHandlingSystem.class, new InputHandlingSystem(new KeyboardInputHandler(Globals.canvas.getScene()), MouseInputHandler.getInstance(canvas.getScene())),4);
        SystemHub.getInstance().addSystem(CollisionSystem.class, new CollisionSystem(),5);
        SystemHub.getInstance().addSystem(LogSystem.class, new LogSystem(),6);
        SystemHub.getInstance().addSystem(ResourceSystem.class, new ResourceSystem(),7);
        SystemHub.getInstance().addSystem(CameraSystem.class, new CameraSystem(Globals.playerEntity, Globals.playerEntity.getComponent(CameraComponent.class)), 8);
        SystemHub.getInstance().addSystem(SoundSystem.class, new SoundSystem(), 9);
    }
}
