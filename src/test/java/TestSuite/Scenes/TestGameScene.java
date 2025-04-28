package TestSuite.Scenes;

import inf.elte.hu.gameengine_javafx.Misc.Scenes.GameScene;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class TestGameScene extends GameScene {
    /**
     * Constructs a new {@code GameScene} with the specified parent node, width, and height.
     *
     * @param parent The root node of the scene.
     * @param width  The width of the scene in pixels.
     * @param height The height of the scene in pixels.
     */
    public TestGameScene(Parent parent, double width, double height) {
        super(parent, width, height);
    }

    private boolean setupCalled = false;
    private boolean breakdownCalled = false;

    @Override
    public void setup() {
        setupCalled = true;
    }

    @Override
    public void breakdown() {
        breakdownCalled = true;
    }

    @Override
    protected void systemStartUp() {

    }

    public boolean isSetupCalled() {
        return setupCalled;
    }
    public boolean isBreakdownCalled() {
        return breakdownCalled;
    }
}
