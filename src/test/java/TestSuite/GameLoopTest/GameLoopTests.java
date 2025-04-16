package TestSuite.GameLoopTest;

import inf.elte.hu.gameengine_javafx.Misc.Configs.DisplayConfig;
import inf.elte.hu.gameengine_javafx.Misc.GameLoop;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameLoopTests {
    private TestGameLoop testLoop;

    @BeforeEach
    void setUp() {
        DisplayConfig.fpsCap = 60;
        testLoop = new TestGameLoop();
    }

    @Test
    void testStartAndStopLoop() throws InterruptedException {
        testLoop.startLoop();
        Thread.sleep(100);
        testLoop.stopLoop();

        assertTrue(testLoop.wasUpdateCalled(), "update() should have been called during loop");
        assertTrue(testLoop.isStopped(), "Thread should be stopped");
    }

    @Test
    void testFPSCapEnforced() throws InterruptedException {
        long fpsCap = 30;
        DisplayConfig.fpsCap = (int) fpsCap;
        testLoop = new TestGameLoop();

        long start = System.currentTimeMillis();
        testLoop.startLoop();

        Thread.sleep(500);

        testLoop.stopLoop();
        testLoop.join();

        int updates = testLoop.getUpdateCallCount();
        long elapsed = System.currentTimeMillis() - start;

        double expectedMaxFrames = (elapsed / 1000.0) * fpsCap;

        assertTrue(updates <= expectedMaxFrames + 2, "update() should not exceed fpsCap");
    }

    private static class TestGameLoop extends GameLoop {

        private int updateCallCount = 0;

        @Override
        public void update() {
            updateCallCount++;
            if (updateCallCount >= 10) {
                stopLoop();
            }
        }

        public boolean wasUpdateCalled() {
            return updateCallCount > 0;
        }

        public int getUpdateCallCount() {
            return updateCallCount;
        }

        public boolean isStopped() {
            return !running;
        }
    }
}
