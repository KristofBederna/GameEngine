package inf.elte.hu.gameengine_javafx.Misc.Scenes;

import inf.elte.hu.gameengine_javafx.Components.Default.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.InteractiveComponent;
import inf.elte.hu.gameengine_javafx.Components.PathfindingComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PlayerComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.MapMeshComponent;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.SystemHub;
import inf.elte.hu.gameengine_javafx.Entities.*;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.NSidedShape;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Misc.*;
import inf.elte.hu.gameengine_javafx.Misc.EventHandling.EventListeners.FullScreenToggleEventListener;
import inf.elte.hu.gameengine_javafx.Misc.EventHandling.EventListeners.ResolutionChangeEventListener;
import inf.elte.hu.gameengine_javafx.Misc.EventHandling.EventManager;
import inf.elte.hu.gameengine_javafx.Misc.EventHandling.Events.FullScreenToggleEvent;
import inf.elte.hu.gameengine_javafx.Misc.EventHandling.Events.ResolutionChangeEvent;
import inf.elte.hu.gameengine_javafx.Misc.InputHandlers.MouseInputHandler;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.GameLoopStartUp;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.ResourceStartUp;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.SystemStartUp;
import inf.elte.hu.gameengine_javafx.Systems.InputHandlingSystem;
import inf.elte.hu.gameengine_javafx.Systems.PathfindingSystem;
import inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems.CollisionSystem;
import inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems.MovementDeterminerSystem;
import inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems.MovementSystem;
import inf.elte.hu.gameengine_javafx.Systems.RenderingSystems.*;
import inf.elte.hu.gameengine_javafx.Systems.ResourceSystems.*;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Random;


public class TestScene extends GameScene {
    public TestScene(Parent parent, double width, double height) {
        super(parent, width, height);
        setup();
    }

    @Override
    public void setup() {
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("/assets/styles/styles.css")).toExternalForm());
        new ResourceStartUp();
        WorldEntity.getInstance("/assets/maps/hardForAIMap.txt", "/assets/tileSets/testTiles.txt");
        entitySetup();
        UtilityFunctions.setUpCamera(1920, 1080, 32, 32);
        interactionSetup();
        new SystemStartUp(this::SystemStartUp);

        if (GameLoopStartUp.getGameLoop() == null) {
            new GameLoopStartUp();
        } else {
            GameLoopStartUp.getGameLoop().setRunning(true);
        }
    }

    @Override
    public void breakdown() {
      UtilityFunctions.defaultBreakdownMethod();
    }

    private void SystemStartUp() {
        //Define systems to be started up here
        SystemHub systemHub = SystemHub.getInstance();
        systemHub.addSystem(MovementDeterminerSystem.class, new MovementDeterminerSystem(), 0);
        systemHub.addSystem(AnimationSystem.class, new AnimationSystem(), 1);
        systemHub.addSystem(RenderSystem.class, new RenderSystem(), 2);
        systemHub.addSystem(BackgroundMusicSystem.class, new BackgroundMusicSystem(), 3);
        systemHub.addSystem(PathfindingSystem.class, new PathfindingSystem(), 4);
        systemHub.addSystem(MovementSystem.class, new MovementSystem(), 5);
        systemHub.addSystem(ParticleSystem.class, new ParticleSystem(), 6);
        systemHub.addSystem(InputHandlingSystem.class, new InputHandlingSystem(), 7);
        systemHub.addSystem(CollisionSystem.class, new CollisionSystem(), 8);
        systemHub.addSystem(ResourceSystem.class, new ResourceSystem(), 9);
        systemHub.addSystem(CameraSystem.class, new CameraSystem(), 10);
        systemHub.addSystem(SoundSystem.class, new SoundSystem(), 11);
        systemHub.addSystem(DynamicWorldLoaderSystem.class, new DynamicWorldLoaderSystem(2,2), 12);
    }

    private void entitySetup() {
        new PlayerEntity(420, 120, "idle", "/assets/images/PlayerIdle.png", 0.8 * Config.scaledTileSize, 0.8 * Config.scaledTileSize);
        new DummyEntity(220, 220, "idle", "/assets/images/PlayerIdle.png", 0.8 * Config.scaledTileSize, 0.8 * Config.scaledTileSize);
        new ParticleEmitterEntity(400, 400, new ParticleEntity(400, 400, 25, 25,
                new NSidedShape(new Point(400, 400), 25, 32), Color.ORANGE, Color.TRANSPARENT, 300), Direction.RIGHT, 50, 1000);
        new ParticleEmitterEntity(5*Config.scaledTileSize, 500, new ParticleEntity(0, 0, 20, 20, "/assets/images/snowflake.png", 2000), Direction.ALL, 20, 1000);
    }

    private void interactionSetup() {
        PlayerEntity player = (PlayerEntity) EntityHub.getInstance().getEntitiesWithComponent(PlayerComponent.class).getFirst();

        DummyEntity entity2 = (DummyEntity) EntityHub.getInstance().getEntitiesWithType(DummyEntity.class).getFirst();

        InteractiveComponent playerInteractiveComponent = player.getComponent(InteractiveComponent.class);
        UtilityFunctions.setUpMovement(playerInteractiveComponent, player);

        playerInteractiveComponent.mapInput(MouseButton.PRIMARY, 100, () -> {
            player.getComponent(PositionComponent.class).setLocalX(MouseInputHandler.getInstance().getMouseX(), player);
            player.getComponent(PositionComponent.class).setLocalY(MouseInputHandler.getInstance().getMouseY(), player);
        });
        //playerInteractiveComponent.mapInput(MouseButton.PRIMARY, () -> System.out.println(MouseInputHandler.getInstance().getMouseX() + " " + MouseInputHandler.getInstance().getMouseY()));
        playerInteractiveComponent.mapInput(MouseButton.SECONDARY, 400, () -> new SoundEffect(player, "/assets/sound/sfx/explosion.wav", "explosion", 1f, 0f, 1000, false), () -> SoundEffectStore.getInstance().remove("explosion"));
        playerInteractiveComponent.mapInput(KeyCode.F2, 10, () -> CameraEntity.getInstance().attachTo(entity2), () -> CameraEntity.getInstance().attachTo(player));
        playerInteractiveComponent.mapInput(KeyCode.F3, 100, () -> SystemHub.getInstance().getSystem(SceneManagementSystem.class).requestSceneChange(new Test2Scene(new BorderPane(), 1920, 1080)));

        EventManager eventManager = new EventManager();
        eventManager.registerListener(FullScreenToggleEvent.class, new FullScreenToggleEventListener());
        eventManager.registerListener(ResolutionChangeEvent.class, new ResolutionChangeEventListener());

        playerInteractiveComponent.mapInput(KeyCode.F11, 100, () -> eventManager.fireEvent(new FullScreenToggleEvent((Stage) getWindow())));

        playerInteractiveComponent.mapInput(KeyCode.F10, 100, () -> eventManager.fireEvent(new ResolutionChangeEvent(1280, 720)));
        playerInteractiveComponent.mapInput(KeyCode.F12, 100, () -> eventManager.fireEvent(new ResolutionChangeEvent(1920, 1080)));

        playerInteractiveComponent.mapInput(KeyCode.F4, 1000, () -> {
            Random random = new Random();
            Point target = null;
            while (target == null) {
                target = WorldEntity.getInstance().getComponent(MapMeshComponent.class).getMapCoordinates().get(random.nextInt(32)).get(random.nextInt(32));
            }
            PathfindingComponent pathfinding = entity2.getComponent(PathfindingComponent.class);
            if (pathfinding != null && pathfinding.getEnd() != null) {
                pathfinding.setEnd(target);
                pathfinding.resetPathing(entity2);
            }
        });
    }
}
