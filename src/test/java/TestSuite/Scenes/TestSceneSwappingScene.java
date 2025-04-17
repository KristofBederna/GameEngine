package TestSuite.Scenes;

import TestSuite.Entities.DummyEntity;
import inf.elte.hu.gameengine_javafx.Components.Default.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.InteractiveComponent;
import inf.elte.hu.gameengine_javafx.Components.PathfindingComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PlayerComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.MapMeshComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.SystemHub;
import inf.elte.hu.gameengine_javafx.Entities.*;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Misc.Configs.MapConfig;
import inf.elte.hu.gameengine_javafx.Misc.InputHandlers.MouseInputHandler;
import inf.elte.hu.gameengine_javafx.Misc.Scenes.GameScene;
import inf.elte.hu.gameengine_javafx.Misc.SoundEffect;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.GameLoopStartUp;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.ResourceStartUp;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.SystemStartUp;
import inf.elte.hu.gameengine_javafx.Misc.UtilityFunctions;
import inf.elte.hu.gameengine_javafx.Systems.InputHandlingSystem;
import inf.elte.hu.gameengine_javafx.Systems.PathfindingSystem;
import inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems.CollisionSystem;
import inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems.MovementDeterminerSystem;
import inf.elte.hu.gameengine_javafx.Systems.PhysicsSystems.MovementSystem;
import inf.elte.hu.gameengine_javafx.Systems.RenderingSystems.AnimationSystem;
import inf.elte.hu.gameengine_javafx.Systems.RenderingSystems.CameraSystem;
import inf.elte.hu.gameengine_javafx.Systems.RenderingSystems.ParticleSystem;
import inf.elte.hu.gameengine_javafx.Systems.RenderingSystems.RenderSystem;
import inf.elte.hu.gameengine_javafx.Systems.ResourceSystems.*;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;

import java.util.Random;


public class TestSceneSwappingScene extends GameScene {
    public TestSceneSwappingScene(Parent parent, double width, double height) {
        super(parent, width, height);
    }

    @Override
    public void setup() {
        new ResourceStartUp();
        WorldEntity.getInstance(32, 16, "/assets/maps/hardForAIMap.txt", "/assets/tileSets/testTiles.txt");
        Entity entity2 = entitySetup();
        UtilityFunctions.setUpCamera(1920, 1080, 32, 16);
        interactionSetup(entity2);
        new SystemStartUp(this::SystemStartUp);

        GameLoopStartUp.restartLoop();
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
        systemHub.addSystem(WorldLoaderSystem.class, new WorldLoaderSystem(), 12);
    }


    @Override
    public void breakdown() {
        UtilityFunctions.defaultBreakdownMethod();
    }

    private Entity entitySetup() {
        new PlayerEntity(420, 120, "idle", "/assets/images/PlayerIdle.png", 0.8 * MapConfig.scaledTileSize, 0.8 * MapConfig.scaledTileSize);
        DummyEntity entity2 = new DummyEntity(100, 100, "idle", "/assets/images/PlayerIdle.png", 80, 80);
        return entity2;
    }

    private void interactionSetup(Entity entity2) {
        PlayerEntity player = (PlayerEntity) EntityHub.getInstance().getEntitiesWithComponent(PlayerComponent.class).getFirst();
        InteractiveComponent dummyInteractiveComponent = player.getComponent(InteractiveComponent.class);
        UtilityFunctions.setUpMovement(dummyInteractiveComponent, player);
        dummyInteractiveComponent.mapInput(MouseButton.PRIMARY, 100, () -> {
            player.getComponent(PositionComponent.class).setLocalX(MouseInputHandler.getInstance().getMouseX(), player);
            player.getComponent(PositionComponent.class).setLocalY(MouseInputHandler.getInstance().getMouseY(), player);
        });
        //dummyInteractiveComponent.mapInput(MouseButton.PRIMARY, () -> System.out.println(MouseInputHandler.getInstance().getMouseX() + " " + MouseInputHandler.getInstance().getMouseY()));
        dummyInteractiveComponent.mapInput(MouseButton.SECONDARY, 400, () -> new SoundEffect(player, "/assets/sound/sfx/explosion.wav", "explosion", 1f, 0f, 1000, false));
        dummyInteractiveComponent.mapInput(KeyCode.F2, 10, () -> CameraEntity.getInstance().attachTo(entity2), () -> CameraEntity.getInstance().attachTo(player));
        dummyInteractiveComponent.mapInput(KeyCode.F3, 100, () -> SystemHub.getInstance().getSystem(SceneManagementSystem.class).requestSceneChange(new TestSceneSwappingScene(new BorderPane(), 1920, 1080)));

        dummyInteractiveComponent.mapInput(KeyCode.F4, 1000, () -> {
            Random random = new Random();
            Point target = null;
            while (target == null) {
                target = WorldEntity.getInstance().getComponent(MapMeshComponent.class).getMapCoordinates().get(random.nextInt(16)).get(random.nextInt(32));
            }
            PathfindingComponent pathfinding = entity2.getComponent(PathfindingComponent.class);
            if (pathfinding != null && pathfinding.getEnd() != null) {
                pathfinding.setEnd(target);
                pathfinding.resetPathing(entity2);
            }
        });
    }
}

