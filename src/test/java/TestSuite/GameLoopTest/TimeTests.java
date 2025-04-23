package TestSuite.GameLoopTest;

import inf.elte.hu.gameengine_javafx.Misc.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TimeTests {
    private Time time;

    @BeforeEach
    void setUp() {
        time = Time.getInstance();
        time.setTimeScale(1.0);
        time.setFPSCap(60);
    }

    @Test
    void testSingletonInstance() {
        Time anotherInstance = Time.getInstance();
        assertSame(time, anotherInstance, "Time should be a singleton");
    }

    @Test
    void testDeltaTimeCalculation() throws InterruptedException {
        time.update();
        Thread.sleep(50);
        time.update();

        double delta = time.getDeltaTime();
        assertTrue(delta > 0.02 && delta < 0.09, "Delta time should reflect ~50ms sleep");
    }

    @Test
    void testUnscaledDeltaTimeUnaffectedByTimeScale() throws InterruptedException {
        time.setTimeScale(0.1);
        time.update();
        Thread.sleep(40);
        time.update();

        double unscaled = time.getUnscaledDeltaTime();
        double scaled = time.getDeltaTime();

        assertTrue(unscaled > scaled, "Unscaled delta time should be larger than scaled");
        assertEquals(unscaled * 0.1, scaled, 0.001, "Scaled delta should match scale * unscaled");
    }

    @Test
    void testFPSCapSetterAndGetter() {
        time.setFPSCap(144);
        assertTrue(time.isFPSCapEnabled(), "FPS cap should be enabled");
        assertEquals(144, time.getFPSCap(), "FPS cap should be correctly set");
    }

    @Test
    void testFPSCalculationIncreasesOverTime() throws InterruptedException {
        int initialFPS = time.getFPS();

        for (int i = 0; i < 60; i++) {
            time.update();
            Thread.sleep(5);
        }

        int laterFPS = time.getFPS();

        assertTrue(laterFPS >= initialFPS, "FPS should increase with updates");
    }

    @Test
    void testElapsedTimeIsMonotonic() throws InterruptedException {
        double t1 = time.getElapsedTime();
        Thread.sleep(30);
        double t2 = time.getElapsedTime();

        assertTrue(t2 > t1, "Elapsed time should increase over real time");
    }

    @Test
    void testTimeScaleCannotBeNegative() {
        time.setTimeScale(-1.0);
        assertEquals(0.0, time.getTimeScale(), "Time scale should not be negative");
    }

    @Test
    void testFPSCapDisabledWhenSetToZero() {
        time.setFPSCap(0);
        assertFalse(time.isFPSCapEnabled(), "FPS cap should be disabled when set to zero");
        assertEquals(-1, time.getFPSCap(), "FPS cap should return -1 when disabled");
    }
}
