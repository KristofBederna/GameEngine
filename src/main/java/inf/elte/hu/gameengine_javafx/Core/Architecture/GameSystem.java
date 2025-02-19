package inf.elte.hu.gameengine_javafx.Core.Architecture;

import inf.elte.hu.gameengine_javafx.Core.SystemHub;

public abstract class GameSystem {
    protected boolean active = true;

    public void run() {
        if (active) {
            update();
        }
    }

    protected abstract void update();

    public void abort() {
        active = false;
    }

    public Integer getPriority() {
        return SystemHub.getInstance().getAllSystemsInPriorityOrder().indexOf(this);
    }
}
