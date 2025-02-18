package inf.elte.hu.gameengine_javafx.Misc.Scenes;

import inf.elte.hu.gameengine_javafx.Components.*;
import inf.elte.hu.gameengine_javafx.Core.*;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Entities.DummyEntity;
import inf.elte.hu.gameengine_javafx.Entities.TileEntity;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import inf.elte.hu.gameengine_javafx.Misc.InputHandlers.MouseInputHandler;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.GameMap;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.MapLoader;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.TileLoader;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.TileSetLoader;
import inf.elte.hu.gameengine_javafx.Misc.Time;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;

import java.util.Arrays;
import java.util.function.Function;


public class TestScene extends GameScene{
    public TestScene(Parent parent, double width, double height, Runnable switchScene) {
        super(parent, width, height);

        TileLoader tileLoader = new TileLoader();
        TileSetLoader.loadSet("/assets/tileSets/testTiles2.txt", tileLoader);
        GameMap map = MapLoader.loadMap("/assets/maps/testMap.txt", tileLoader);

        InteractiveComponent dummyInteractiveComponent = Globals.playerEntity.getComponent(InteractiveComponent.class);
        dummyInteractiveComponent.mapInput(KeyCode.W, () -> moveUp(Globals.playerEntity), () -> counterVertical(Globals.playerEntity));
        dummyInteractiveComponent.mapInput(KeyCode.S, () -> moveDown(Globals.playerEntity), () -> counterVertical(Globals.playerEntity));
        dummyInteractiveComponent.mapInput(KeyCode.A, () -> moveLeft(Globals.playerEntity), () -> counterHorizontal(Globals.playerEntity));
        dummyInteractiveComponent.mapInput(KeyCode.D, () -> moveRight(Globals.playerEntity), () -> counterHorizontal(Globals.playerEntity));
        dummyInteractiveComponent.mapInput(MouseButton.PRIMARY, () -> {Globals.playerEntity.getComponent(PositionComponent.class).setLocalX(MouseInputHandler.getInstance().getMouseX(), Globals.playerEntity); Globals.playerEntity.getComponent(PositionComponent.class).setLocalY(MouseInputHandler.getInstance().getMouseY(), Globals.playerEntity);});
        dummyInteractiveComponent.mapInput(MouseButton.SECONDARY, () -> Globals.playerEntity.getComponent(SoundEffectStoreComponent.class).addSoundEffect("/assets/sound/sfx/explosion.wav","explosion"), ()->Globals.playerEntity.getComponent(SoundEffectStoreComponent.class).removeSoundEffect("/assets/sound/sfx/explosion.wav"));
        dummyInteractiveComponent.mapInput(KeyCode.ENTER, switchScene);




        EntityManager<TileEntity> tileEntityManager = new EntityManager<>();
        for (TileEntity[] row : map.getMapData()) {
            tileEntityManager.registerAll(Arrays.asList(row));
        }
        EntityHub.getInstance().addEntityManager(TileEntity.class, tileEntityManager);

        EntityManager<DummyEntity> dummyEntityManager = new EntityManager<>();
        dummyEntityManager.register((DummyEntity) Globals.playerEntity);
        //DummyEntity entity2 = new DummyEntity(80, 0, "idle", "/assets/images/PlayerIdle.png", 80, 80, 1920, 1080, 30*100, 15*100);
        //dummyEntityManager.register(entity2);
        //Globals.playerEntity.getComponent(ParentComponent.class).addChild(entity2);
        //entity2.getComponent(ParentComponent.class).setParent(Globals.playerEntity);
        EntityHub.getInstance().addEntityManager(DummyEntity.class, dummyEntityManager);
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
