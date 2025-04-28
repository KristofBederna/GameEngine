package Game.Misc.Scenes;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Entities.WorldEntity;
import inf.elte.hu.gameengine_javafx.Misc.Scenes.GameScene;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.GameLoopStartUp;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.ResourceStartUp;
import inf.elte.hu.gameengine_javafx.Misc.StartUpClasses.SystemStartUp;
import inf.elte.hu.gameengine_javafx.Misc.UtilityFunctions;
import javafx.scene.Parent;

/**
 * This class is an example of how a scene needs to be created.
 */
public class OnboardingScene extends GameScene {
    /**
     * Constructs a new {@code GameScene} with the specified parent node, width, and height.
     *
     * @param parent The root node of the scene.
     * @param width  The width of the scene in pixels.
     * @param height The height of the scene in pixels.
     */
    public OnboardingScene(Parent parent, double width, double height) {
        super(parent, width, height);
    }

    @Override
    public void setup() {
        //Start up resources, initialize the WorldEntity, set up entities, set up the camera, set up interactions, call SystemStartUp and start the game loop.
    }

    @Override
    public void breakdown() {
        //Delete everything from the memory not needed for the next scene, call the UtilityFunction: defaultBreakdownMethod.
    }

    @Override
    protected void systemStartUp() {
        //Call the SystemHub,create every system to be started up here, add them to the SystemHub.
    }
}
