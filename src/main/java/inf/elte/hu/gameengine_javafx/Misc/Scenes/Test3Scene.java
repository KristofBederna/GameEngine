package inf.elte.hu.gameengine_javafx.Misc.Scenes;

import inf.elte.hu.gameengine_javafx.Components.*;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.EntityManager;
import inf.elte.hu.gameengine_javafx.Core.ResourceHub;
import inf.elte.hu.gameengine_javafx.Core.SystemHub;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import inf.elte.hu.gameengine_javafx.Entities.DummyEntity;
import inf.elte.hu.gameengine_javafx.Entities.TileEntity;
import inf.elte.hu.gameengine_javafx.Entities.WorldEntity;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import inf.elte.hu.gameengine_javafx.Misc.InputHandlers.MouseInputHandler;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.GameLoopStartUp;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.ResourceStartUp;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.SystemStartUp;
import inf.elte.hu.gameengine_javafx.Misc.Time;
import inf.elte.hu.gameengine_javafx.Systems.SceneManagementSystem;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;

import java.util.List;


public class Test3Scene extends GameScene{
    public Test3Scene(Parent parent, double width, double height) {
        super(parent, width, height);
    }

    @Override
    public void setup() {
        new ResourceStartUp();
        WorldEntity map = WorldEntity.getInstance(30, 15, "/assets/maps/testMap.txt", "/assets/tileSets/testTiles2.txt");
        Entity entity2 = entitySetup(map);
        cameraSetup();
        interactionSetup(entity2);
        new SystemStartUp();
        new GameLoopStartUp();
    }

    @Override
    public void breakdown() {
        EntityHub entityHub = EntityHub.getInstance();
        entityHub.removeEntityManager(TileEntity.class);
        entityHub.removeEntityManager(DummyEntity.class);

        EntityManager<TileEntity> tileEntityManager = entityHub.getEntityManager(TileEntity.class);
        if (tileEntityManager != null) {
            tileEntityManager.unloadAll();
        }

        EntityManager<DummyEntity> dummyEntityManager = entityHub.getEntityManager(DummyEntity.class);
        if (dummyEntityManager != null) {
            dummyEntityManager.unloadAll();
        }
        WorldEntity.resetInstance();
        if (Globals.playerEntity != null) {
            InteractiveComponent interactiveComponent = Globals.playerEntity.getComponent(InteractiveComponent.class);
            if (interactiveComponent != null) {
                interactiveComponent.clearMappings();
            }
        }
        CameraEntity.resetInstance();
        SystemHub.getInstance().shutDownSystems();
        GameLoopStartUp.stopGameLoop();
        ResourceHub.getInstance().clearResources();
        Globals.playerEntity = null;
    }

    private void cameraSetup() {
        CameraEntity.getInstance(1920, 1080, 30*100, 15*100);
        CameraEntity.getInstance().attachTo(Globals.playerEntity);
    }

    private Entity entitySetup(WorldEntity map) {
        Globals.playerEntity = new DummyEntity(420, 100, "idle", "/assets/images/PlayerIdle.png", 80, 80);

        EntityManager<TileEntity> tileEntityManager = new EntityManager<>();
        for (List<TileEntity> row : map.getComponent(WorldDataComponent.class).getMapData()) {
            tileEntityManager.registerAll(row);
        }
        EntityHub.getInstance().addEntityManager(TileEntity.class, tileEntityManager);

        EntityManager<DummyEntity> dummyEntityManager = new EntityManager<>();
        dummyEntityManager.register((DummyEntity) Globals.playerEntity);
        DummyEntity entity2 = new DummyEntity(100, 100, "idle", "/assets/images/PlayerIdle.png", 80, 80);
        dummyEntityManager.register(entity2);
        EntityHub.getInstance().addEntityManager(DummyEntity.class, dummyEntityManager);

        return entity2;
    }

    private void interactionSetup(Entity entity2) {
        InteractiveComponent dummyInteractiveComponent = Globals.playerEntity.getComponent(InteractiveComponent.class);
        dummyInteractiveComponent.mapInput(KeyCode.UP, () -> moveUp(Globals.playerEntity), () -> counterVertical(Globals.playerEntity));
        dummyInteractiveComponent.mapInput(KeyCode.DOWN, () -> moveDown(Globals.playerEntity), () -> counterVertical(Globals.playerEntity));
        dummyInteractiveComponent.mapInput(KeyCode.LEFT, () -> moveLeft(Globals.playerEntity), () -> counterHorizontal(Globals.playerEntity));
        dummyInteractiveComponent.mapInput(KeyCode.RIGHT, () -> moveRight(Globals.playerEntity), () -> counterHorizontal(Globals.playerEntity));
        dummyInteractiveComponent.mapInput(MouseButton.PRIMARY, () -> {Globals.playerEntity.getComponent(PositionComponent.class).setLocalX(MouseInputHandler.getInstance().getMouseX(), Globals.playerEntity); Globals.playerEntity.getComponent(PositionComponent.class).setLocalY(MouseInputHandler.getInstance().getMouseY(), Globals.playerEntity);});
        dummyInteractiveComponent.mapInput(MouseButton.SECONDARY, () -> Globals.playerEntity.getComponent(SoundEffectStoreComponent.class).addSoundEffect("/assets/sound/sfx/explosion.wav","explosion"), ()->Globals.playerEntity.getComponent(SoundEffectStoreComponent.class).removeSoundEffect("/assets/sound/sfx/explosion.wav"));
        dummyInteractiveComponent.mapInput(KeyCode.F2, () -> CameraEntity.getInstance().attachTo(entity2), () -> CameraEntity.getInstance().attachTo(Globals.playerEntity));
        dummyInteractiveComponent.mapInput(KeyCode.ENTER, () -> SystemHub.getInstance().getSystem(SceneManagementSystem.class).requestSceneChange(new Test2Scene(new BorderPane(), 1920, 1080)));
    }


    private void moveUp(Entity e) {
        double dy = -400 * Time.getInstance().getDeltaTime();
        e.getComponent(VelocityComponent.class).setDy(dy);
        e.getComponent(StateComponent.class).setCurrentState("up");
    }

    private void moveDown(Entity e) {
        double dy = 400 * Time.getInstance().getDeltaTime();
        e.getComponent(VelocityComponent.class).setDy(dy);
        e.getComponent(StateComponent.class).setCurrentState("down");
    }

    private void moveLeft(Entity e) {
        double dx = -400 * Time.getInstance().getDeltaTime();
        e.getComponent(VelocityComponent.class).setDx(dx);
        e.getComponent(StateComponent.class).setCurrentState("left");
    }

    private void moveRight(Entity e) {
        double dx = 400 * Time.getInstance().getDeltaTime();
        e.getComponent(VelocityComponent.class).setDx(dx);
        e.getComponent(StateComponent.class).setCurrentState("right");
    }

    private void counterVertical(Entity e) {
        e.getComponent(VelocityComponent.class).setDy(0);
        e.getComponent(StateComponent.class).setCurrentState("idle");
    }


    private void counterHorizontal(Entity e) {
        e.getComponent(VelocityComponent.class).setDx(0);
        e.getComponent(StateComponent.class).setCurrentState("idle");
    }
}
