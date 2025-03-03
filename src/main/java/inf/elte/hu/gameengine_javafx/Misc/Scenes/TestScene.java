package inf.elte.hu.gameengine_javafx.Misc.Scenes;

import inf.elte.hu.gameengine_javafx.Components.*;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.StateComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.MapMeshComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.EntityManager;
import inf.elte.hu.gameengine_javafx.Core.ResourceHub;
import inf.elte.hu.gameengine_javafx.Core.SystemHub;
import inf.elte.hu.gameengine_javafx.Entities.*;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.NSidedShape;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Rectangle;
import inf.elte.hu.gameengine_javafx.Misc.Direction;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import inf.elte.hu.gameengine_javafx.Misc.InputHandlers.MouseInputHandler;
import inf.elte.hu.gameengine_javafx.Misc.Layers.uiRoot;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.GameLoopStartUp;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.ResourceStartUp;
import inf.elte.hu.gameengine_javafx.Misc.Time;
import inf.elte.hu.gameengine_javafx.Systems.ResourceSystems.SceneManagementSystem;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.util.Objects;
import java.util.Random;


public class TestScene extends GameScene{
    public TestScene(Parent parent, double width, double height) {
        super(parent, width, height);
        setup();
    }

    @Override
    public void setup() {
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("/assets/styles/styles.css")).toExternalForm());
        new ResourceStartUp();
        WorldEntity.getInstance(30, 15, "/assets/maps/hardForAIMap.txt", "/assets/tileSets/testTiles.txt");
        entitySetup();
        cameraSetup();
        interactionSetup();
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
        EntityManager<PlayerEntity> playerEntityManager = entityHub.getEntityManager(PlayerEntity.class);
        if (playerEntityManager != null) {
            playerEntityManager.unloadAll();
        }
        WorldEntity.resetInstance();
        if (EntityHub.getInstance().getEntitiesWithComponent(PlayerComponent.class).getFirst() != null) {
            InteractiveComponent interactiveComponent = EntityHub.getInstance().getEntitiesWithComponent(PlayerComponent.class).getFirst().getComponent(InteractiveComponent.class);
            if (interactiveComponent != null) {
                interactiveComponent.clearMappings();
            }
        }
        CameraEntity.resetInstance();
        SystemHub.getInstance().shutDownSystems();
        GameLoopStartUp.stopGameLoop();
        ResourceHub.getInstance().clearResources();
        uiRoot.getInstance().unloadAll();
    }

    private void cameraSetup() {
        CameraEntity.getInstance(1920, 1080, 30*Globals.tileSize, 15*Globals.tileSize);
        CameraEntity.getInstance().attachTo(EntityHub.getInstance().getEntitiesWithComponent(PlayerComponent.class).getFirst());
    }

    private void entitySetup() {
        new PlayerEntity(420, 120, "idle", "/assets/images/PlayerIdle.png", 0.8*Globals.tileSize, 0.8*Globals.tileSize);
        new DummyEntity(100, 100, "idle", "/assets/images/PlayerIdle.png", 80, 80);
        //new ParticleEmitterEntity(400, 400, new ParticleEntity(0, 0, 2, 2, new Rectangle(new Point(0, 0), 2, 2), Color.ORANGE), Direction.RIGHT);
        new ParticleEmitterEntity(15*Globals.tileSize, 750, new ParticleEntity(0, 0, 20, 20, "/assets/images/snowflake.png", 1000), Direction.ALL, 200, 2500);
//        ButtonEntity be = new ButtonEntity();
//        be.addStyleClass("my-custom-button");
//        new SliderEntity();
//        new CheckBoxEntity();
//        new LabelEntity();
//        new TextFieldEntity();
//        new ProgressBarEntity();
    }

    private void interactionSetup() {
        PlayerEntity player = (PlayerEntity)EntityHub.getInstance().getEntitiesWithComponent(PlayerComponent.class).getFirst();

        DummyEntity entity2 = (DummyEntity)EntityHub.getInstance().getEntitiesWithType(DummyEntity.class).getFirst();

        InteractiveComponent playerInteractiveComponent = player.getComponent(InteractiveComponent.class);
        playerInteractiveComponent.mapInput(KeyCode.UP, 10, () -> moveUp(player), () -> counterVertical(player));
        playerInteractiveComponent.mapInput(KeyCode.DOWN, 10, () -> moveDown(player), () -> counterVertical(player));
        playerInteractiveComponent.mapInput(KeyCode.LEFT, 10, () -> moveLeft(player), () -> counterHorizontal(player));
        playerInteractiveComponent.mapInput(KeyCode.RIGHT, 10, () -> moveRight(player), () -> counterHorizontal(player));
        playerInteractiveComponent.mapInput(MouseButton.PRIMARY, 100, () -> {player.getComponent(PositionComponent.class).setLocalX(MouseInputHandler.getInstance().getMouseX(), player); player.getComponent(PositionComponent.class).setLocalY(MouseInputHandler.getInstance().getMouseY(), player);});
        //playerInteractiveComponent.mapInput(MouseButton.PRIMARY, () -> System.out.println(MouseInputHandler.getInstance().getMouseX() + " " + MouseInputHandler.getInstance().getMouseY()));
        playerInteractiveComponent.mapInput(MouseButton.SECONDARY, 5000, () -> player.getComponent(SoundEffectStoreComponent.class).addSoundEffect("/assets/sound/sfx/explosion.wav","explosion"), ()->player.getComponent(SoundEffectStoreComponent.class).removeSoundEffect("/assets/sound/sfx/explosion.wav"));
        playerInteractiveComponent.mapInput(KeyCode.F2, 10, () -> CameraEntity.getInstance().attachTo(entity2), () -> CameraEntity.getInstance().attachTo(player));
        playerInteractiveComponent.mapInput(KeyCode.F3, 100, () -> SystemHub.getInstance().getSystem(SceneManagementSystem.class).requestSceneChange(new Test2Scene(new BorderPane(), 1920, 1080)));

        playerInteractiveComponent.mapInput(KeyCode.F4, 1000, () -> {
            Random random = new Random();
            Point target = null;
            while (target == null) {
                target = WorldEntity.getInstance().getComponent(MapMeshComponent.class).getMapCoordinates().get(random.nextInt(15)).get(random.nextInt(30));
            }
            PathfindingComponent pathfinding = entity2.getComponent(PathfindingComponent.class);
            if (pathfinding != null && pathfinding.getEnd() != null) {
                pathfinding.setEnd(target);
                pathfinding.resetPathing(entity2);
            }
        });
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
