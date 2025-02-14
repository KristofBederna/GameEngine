package inf.elte.hu.gameengine_javafx.Misc.Scenes;

import inf.elte.hu.gameengine_javafx.Components.*;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.EntityManager;
import inf.elte.hu.gameengine_javafx.Entities.DummyEntity;
import inf.elte.hu.gameengine_javafx.Entities.TileEntity;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import inf.elte.hu.gameengine_javafx.Misc.InputHandlers.MouseInputHandler;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.GameMap;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.MapLoader;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.TileLoader;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.TileSetLoader;
import inf.elte.hu.gameengine_javafx.Misc.Time;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

import java.util.Arrays;


public class Test2Scene extends GameScene{
    public Test2Scene(Parent parent, double width, double height) {
        super(parent, width, height);

        TileLoader tileLoader = new TileLoader();
        TileSetLoader.loadSet("/assets/tileSets/testTiles.txt", tileLoader);
        GameMap map = MapLoader.loadMap("/assets/maps/testMap.txt", tileLoader);

        InteractiveComponent dummyInteractiveComponent = Globals.playerEntity.getComponent(InteractiveComponent.class);
        dummyInteractiveComponent.mapInput(KeyCode.UP, () -> moveUp(Globals.playerEntity), () -> counterVertical(Globals.playerEntity));
        dummyInteractiveComponent.mapInput(KeyCode.DOWN, () -> moveDown(Globals.playerEntity), () -> counterVertical(Globals.playerEntity));
        dummyInteractiveComponent.mapInput(KeyCode.LEFT, () -> moveLeft(Globals.playerEntity), () -> counterHorizontal(Globals.playerEntity));
        dummyInteractiveComponent.mapInput(KeyCode.RIGHT, () -> moveRight(Globals.playerEntity), () -> counterHorizontal(Globals.playerEntity));
        dummyInteractiveComponent.mapInput(MouseButton.PRIMARY, () -> {Globals.playerEntity.getComponent(PositionComponent.class).setX(MouseInputHandler.getInstance().getMouseX()); Globals.playerEntity.getComponent(PositionComponent.class).setY(MouseInputHandler.getInstance().getMouseY());});
        dummyInteractiveComponent.mapInput(MouseButton.SECONDARY, () -> Globals.playerEntity.getComponent(SoundEffectStoreComponent.class).addSoundEffect("/assets/sound/sfx/explosion.wav","explosion"), ()->Globals.playerEntity.getComponent(SoundEffectStoreComponent.class).removeSoundEffect("/assets/sound/sfx/explosion.wav"));



        EntityManager<TileEntity> tileEntityManager = new EntityManager<>();
        for (TileEntity[] row : map.getMapData()) {
            tileEntityManager.registerAll(Arrays.asList(row));
        }
        EntityHub.getInstance().addEntityManager(TileEntity.class, tileEntityManager);

        EntityManager<DummyEntity> dummyEntityManager = new EntityManager<>();
        dummyEntityManager.register((DummyEntity) Globals.playerEntity);
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
