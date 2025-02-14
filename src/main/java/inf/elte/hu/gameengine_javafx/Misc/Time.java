package inf.elte.hu.gameengine_javafx.Misc;

public class Time {
    private static final Time instance = new Time();

    private long lastFrameTime;
    private double deltaTime;
    private double unscaledDeltaTime;
    private double timeScale = 1.0;
    private long startTime;

    private boolean fpsCapEnabled = false;
    private int fpsCap = 60;
    private long targetFrameTime = 16_666_667L;
    private long lastFPSUpdate;
    private int fps;
    private long frameCount;

    private Time() {
        lastFrameTime = System.nanoTime();
        startTime = lastFrameTime;
        frameCount = 0;
    }

    public static Time getInstance() {
        return instance;
    }

    public void update() {
        long currentTime = System.nanoTime();
        long elapsedTime = currentTime - lastFrameTime;
        lastFrameTime = currentTime;
        frameCount++;

        unscaledDeltaTime = elapsedTime / 1_000_000_000.0;
        deltaTime = unscaledDeltaTime * timeScale;

        if (fpsCapEnabled) {
            applyFPSCap(currentTime);
        }
    }

    private void applyFPSCap(long frameStartTime) {
        long frameDuration = System.nanoTime() - frameStartTime;
        long sleepTime = targetFrameTime - frameDuration;

        if (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime / 1_000_000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setFPSCap(int fps) {
        if (fps > 0) {
            this.fpsCap = fps;
            this.targetFrameTime = 1_000_000_000L / fps;
            this.fpsCapEnabled = true;
        } else {
            this.fpsCapEnabled = false;
        }
    }

    public boolean isFPSCapEnabled() {
        return fpsCapEnabled;
    }

    public int getFPSCap() {
        return fpsCapEnabled ? fpsCap : -1;
    }

    public double getDeltaTime() {
        return deltaTime;
    }

    public double getUnscaledDeltaTime() {
        return unscaledDeltaTime;
    }

    public void setTimeScale(double scale) {
        this.timeScale = Math.max(0, scale);
    }

    public double getTimeScale() {
        return timeScale;
    }

    public double getElapsedTime() {
        return (System.nanoTime() - startTime) / 1_000_000_000.0;
    }

    public int getFPS() {
        if (System.nanoTime() - lastFPSUpdate >= 1_000_000_000L) {
            fps = (int) frameCount;
            frameCount = 0;
            lastFPSUpdate = System.nanoTime();
        }
        if (fpsCapEnabled && fps > fpsCap) {
            fps = fpsCap;
        }
        return fps;
    }
}
