package inf.elte.hu.gameengine_javafx.Misc;

public abstract class GameLoop extends Thread {
    private final int FPS;
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
        final double frameTime = 1_000_000_000.0 / FPS;
        long lastTime = System.nanoTime();
        long lastFrameTime = lastTime;

        while (running) {
            long currentTime = System.nanoTime();
            double elapsedTime = currentTime - lastTime;
            lastTime = currentTime;

            if (elapsedTime >= frameTime) {
                update();
                frameCount++;
                lastFrameTime = currentTime;
            }

            long sleepTime = (lastFrameTime + (long) frameTime) - System.nanoTime();
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime / 1_000_000, (int) (sleepTime % 1_000_000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                Thread.yield();
            }
        }
    }

    public abstract void update();
}
