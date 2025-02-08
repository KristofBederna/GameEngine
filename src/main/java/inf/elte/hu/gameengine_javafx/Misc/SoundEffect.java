package inf.elte.hu.gameengine_javafx.Misc;

import javax.sound.sampled.Clip;

public class SoundEffect {
    private String path;
    private String identifier;

    public SoundEffect(String path, String identifier) {
        this.path = path;
        this.identifier = identifier;
    }

    public String getPath() {
        return path;
    }

    public String getIdentifier() {
        return identifier;
    }
}
