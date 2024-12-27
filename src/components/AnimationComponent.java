package components;

import core.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class AnimationComponent extends Component {
    private final ArrayList<BufferedImage> frames;
    private final ArrayList<Integer> durations;
    private int currentFrame = 0;
    private int frameDurationCounter = 0;

    public AnimationComponent(ArrayList<String> paths, ArrayList<Integer> durations) {
        if (paths.size() != durations.size()) {
            throw new IllegalArgumentException("Paths and durations must have the same size.");
        }
        this.durations = durations;
        this.frames = new ArrayList<>();
        try {
            setFrames(paths);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load frames", e);
        }
    }

    public void setFrames(ArrayList<String> paths) throws IOException {
        for (String path : paths) {
            try (var resource = getClass().getResourceAsStream(path)) {
                if (resource == null) {
                    throw new IOException("Image resource not found: " + path);
                }
                this.frames.add(ImageIO.read(resource));
            }
        }
    }

    public BufferedImage getNextFrame() {
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
}
