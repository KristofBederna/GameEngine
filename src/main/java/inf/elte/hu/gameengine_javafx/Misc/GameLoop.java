package inf.elte.hu.gameengine_javafx.Misc;

import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.SystemHub;

public abstract class GameLoop extends Thread {
    private boolean running;
    private final Time time;

    public GameLoop() {
        this.running = false;
        this.time = Time.getInstance();
        time.setFPSCap(144);
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
        while (running) {
            long frameStartTime = System.nanoTime();
            time.update();

            update();

            if (time.isFPSCapEnabled()) {
                long frameDuration = System.nanoTime() - frameStartTime;
                long sleepTime = (1_000_000_000L / time.getFPSCap()) - frameDuration;

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
    }

    public abstract void update();
}
