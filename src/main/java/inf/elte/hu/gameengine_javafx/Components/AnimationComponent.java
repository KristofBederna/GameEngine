package inf.elte.hu.gameengine_javafx.Components;


import inf.elte.hu.gameengine_javafx.Core.Component;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnimationComponent extends Component {
    private List<String> frames;
    private final List<Integer> durations;
    private int currentFrame = 0;
    private int frameDurationCounter = 0;

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
        frameDurationCounter++;
        calculateCurrentFrame();
        return frames.get(currentFrame);
    }

    private void calculateCurrentFrame() {
        int partialSum = 0;
        for (int i = 0; i < durations.size(); i++) {
            partialSum += durations.get(i);
            if (frameDurationCounter < partialSum) {
                currentFrame = i;
                return;
            }
        }
        frameDurationCounter = 0;
        currentFrame = 0;
    }

    public void setDurations(ArrayList<Integer> newDurations) {
        durations.clear();
        durations.addAll(newDurations);
    }

    @Override
    public String getStatus() {
        return(this.getClass().getSimpleName() + ": current frame: " + currentFrame + ", current duration: " + durations.get(currentFrame));
    }
}
