package core;

public abstract class GameLoop extends Thread {
    private int FPS;
    private boolean running;
    private long frameCount;

    public GameLoop(int FPS) {
        this.FPS = FPS;
        this.running = false;
        this.frameCount = 0;
    }

    public void startLoop() {
        running = true;
        this.start();
    }

    public void stopLoop() {
        running = false;
    }

    @Override
    public void run() {
        double frameTime = 1_000_000_000.0 / FPS;
        double frameDelta = 0;
        long lastTime = java.lang.System.nanoTime();

        while (running) {
            long currentTime = java.lang.System.nanoTime();
            frameDelta += (currentTime - lastTime) / frameTime;
            lastTime = currentTime;

            if (frameDelta >= 1) {
                frameDelta--;
                frameCount++;
            }
        }
    }

    public void update() {
    }
}

