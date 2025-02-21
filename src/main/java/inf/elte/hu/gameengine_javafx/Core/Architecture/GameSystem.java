package inf.elte.hu.gameengine_javafx.Core.Architecture;

import inf.elte.hu.gameengine_javafx.Core.SystemHub;

public abstract class GameSystem {
    protected boolean active = false;
    protected boolean aborting = false;

    public void run() {
        if (active) {
            update();
        }
    }

    public abstract void start();

    protected abstract void update();

    public void abort() {
        active = false;
        aborting = true;
    }

    public Integer getPriority() {
        return SystemHub.getInstance().getAllSystemsInPriorityOrder().indexOf(this);
    }

    public boolean getIsActive() {
        return active;
    }

    public boolean getIsAborting() {
        return aborting;
    }
}
