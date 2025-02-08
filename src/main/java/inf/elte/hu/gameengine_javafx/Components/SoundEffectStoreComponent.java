package inf.elte.hu.gameengine_javafx.Components;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;
import inf.elte.hu.gameengine_javafx.Misc.SoundEffect;
import inf.elte.hu.gameengine_javafx.Core.ResourceManagers.SoundResourceManager;

import java.util.ArrayList;
import java.util.List;

public class SoundEffectStoreComponent extends Component {
    private List<SoundEffect> soundEffects;

    public SoundEffectStoreComponent() {
        soundEffects = new ArrayList<>();
    }

    public List<SoundEffect> getSoundEffects() {
        return soundEffects;
    }

    public void setSoundEffects(List<SoundEffect> soundEffects) {
        this.soundEffects = soundEffects;
    }

    public void addSoundEffect(String path, String identifier) {
        soundEffects.add(new SoundEffect(path, identifier));
    }

    public void removeSoundEffect(String path) {
        soundEffects.removeIf(soundEffect -> soundEffect.getPath().equals(path));
    }

    @Override
    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("---").append(this.getClass().getSimpleName()).append("---\n");

        for (SoundEffect sound : soundEffects) {
            sb.append("Sound: ").append(sound.getIdentifier()).append(", Path: ").append(sound.getPath()).append("\n");
        }

        return sb.toString();
    }

    public void loadSounds(SoundResourceManager soundManager) {
        // Load the clip for each sound effect
        for (SoundEffect sound : soundEffects) {
            soundManager.get(sound.getPath());
        }
    }
}
