package TestSuite.Layers;

import inf.elte.hu.gameengine_javafx.Misc.Layers.GameCanvas;
import inf.elte.hu.gameengine_javafx.Misc.Layers.GameLayer;
import inf.elte.hu.gameengine_javafx.Misc.Layers.uiRoot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class LayerTests {
    private GameCanvas gameCanvas;
    private uiRoot uiInstance;
    private GameLayer gameLayer;

    @BeforeEach
    public void setUp() {
        GameCanvas.getInstance();
        gameCanvas = GameCanvas.getInstance();
        uiRoot.getInstance();
        uiInstance = uiRoot.getInstance();
        GameLayer.getInstance();
        gameLayer = GameLayer.getInstance();
    }
    @Test
    public void testSingletonInstanceGameCanvas() {
        GameCanvas anotherInstance = GameCanvas.getInstance();
        assertSame(gameCanvas, anotherInstance, "GameCanvas should be a singleton");
    }
    @Test
    public void testSingletonInstanceUIRoot() {
        uiRoot anotherInstance = uiRoot.getInstance();
        assertSame(uiInstance, anotherInstance, "UI Root should be a singleton");
    }
    @Test
    public void testSingletonInstanceGameLayer() {
        GameLayer anotherInstance = GameLayer.getInstance();
        assertSame(gameLayer, anotherInstance, "GameLayer should be a singleton");
    }
    @Test
    public void isPartOfGameLayer() {
        assertSame(gameCanvas, gameLayer.getCanvas(), "GameCanvas should be the same");
        assertSame(uiInstance, gameLayer.getUIRoot(), "UI Root should be the same");
    }
}
