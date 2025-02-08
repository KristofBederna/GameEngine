package inf.elte.hu.gameengine_javafx.Core.ResourceManagers;

import inf.elte.hu.gameengine_javafx.Core.ResourceManager;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class SoundResourceManager extends ResourceManager<Clip> {
    public SoundResourceManager() {
        super(key -> {
            try {
                AudioInputStream audioStream;

                InputStream resource = SoundResourceManager.class.getResourceAsStream(key);
                if (resource == null) {
                    System.err.println("Error: Sound file not found at" + key);
                    return null;
                }
                resource = new BufferedInputStream(resource);
                audioStream = AudioSystem.getAudioInputStream(resource);

                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                return clip;
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                System.err.println("Error loading sound: " + key);
                return null;
            }
        });
    }
}
