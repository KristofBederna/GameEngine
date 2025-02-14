package inf.elte.hu.gameengine_javafx.Components;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;
import inf.elte.hu.gameengine_javafx.Misc.Time;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnimationComponent extends Component {
    private List<String> frames;
    private final List<Integer> durations;
    private int currentFrame = 0;
    private double elapsedTime = 0;

    public AnimationComponent(List<String> frames, List<Integer> durations) {
        if (frames.size() != durations.size()) {
            throw new IllegalArgumentException("Frames and durations must have the same size.");
        }
        this.durations = durations;
        this.frames = frames;
    }

    public void setFrames(List<String> frames) throws IOException {
        this.frames = frames;
    }

    public String getNextFrame() {
        int fps = Time.getInstance().getFPS();
        elapsedTime += Time.getInstance().getDeltaTime();

        double frameDurationInSeconds = (double) durations.get(currentFrame) / fps;

        if (elapsedTime >= frameDurationInSeconds) {
            currentFrame++;
            if (currentFrame >= frames.size()) {
                currentFrame = 0;
            }
            elapsedTime = 0;
        }

        return frames.get(currentFrame);
    }

    public void setDurations(ArrayList<Integer> newDurations) {
        durations.clear();
        durations.addAll(newDurations);
    }

    @Override
    public String getStatus() {
        return (this.getClass().getSimpleName() + ": current frame: " + currentFrame + ", current duration: " + durations.get(currentFrame));
    }
}
