package inf.elte.hu.gameengine_javafx.Components.RenderingComponents;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;
import inf.elte.hu.gameengine_javafx.Misc.AnimationFrame;
import inf.elte.hu.gameengine_javafx.Misc.Time;

import java.util.List;

public class AnimationComponent extends Component {
    private List<AnimationFrame> frames;
    private int currentFrame = 0;
    private double elapsedTime = 0;

    public AnimationComponent(List<AnimationFrame> frames) {
        this.frames = frames;
    }

    public void setFrames(List<AnimationFrame> frames) {
        this.frames = frames;
    }

    public String getNextFrame() {
        updateElapsedTime();
        advanceFrameIfNeeded();
        return getCurrentFrameImage();
    }

    private void updateElapsedTime() {
        elapsedTime += Time.getInstance().getDeltaTime();
    }

    private void advanceFrameIfNeeded() {
        double frameDuration = getCurrentFrameDuration();
        if (elapsedTime >= frameDuration) {
            advanceFrame();
            elapsedTime = 0;
        }
    }

    private double getCurrentFrameDuration() {
        int fps = Time.getInstance().getFPS();
        return (double) frames.get(currentFrame).getFrame().second() / fps;
    }

    private void advanceFrame() {
        currentFrame++;
        if (currentFrame >= frames.size()) {
            currentFrame = 0;
        }
    }

    private String getCurrentFrameImage() {
        return frames.get(currentFrame).getImage();
    }


    @Override
    public String getStatus() {
        return "";
    }
}
