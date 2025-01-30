package inf.elte.hu.gameengine_javafx.Components;


import inf.elte.hu.gameengine_javafx.Core.Component;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnimationComponent extends Component {
    private final List<Image> frames;
    private final List<Integer> durations;
    private int currentFrame = 0;
    private int frameDurationCounter = 0;

    public AnimationComponent(List<String> frames, List<Integer> durations) {
        if (frames.size() != durations.size()) {
            throw new IllegalArgumentException("Frames and durations must have the same size.");
        }
        this.durations = durations;
        this.frames = new ArrayList<>();
        try {
            setFrames(frames);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load frames", e);
        }
    }

    public void setFrames(List<String> frames) throws IOException {
        for (String frame : frames) {
            try (var resource = getClass().getResourceAsStream(frame)) {
                if (resource == null) {
                    throw new IOException("Image resource not found: " + frame);
                }
                this.frames.add(new Image(resource));
            }
        }
    }

    public Image getNextFrame() {
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
}
