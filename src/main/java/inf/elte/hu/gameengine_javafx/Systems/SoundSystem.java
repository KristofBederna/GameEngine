package inf.elte.hu.gameengine_javafx.Systems;

import inf.elte.hu.gameengine_javafx.Components.SoundEffectStoreComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.ResourceHub;
import inf.elte.hu.gameengine_javafx.Misc.SoundEffect;

import javax.sound.sampled.Clip;
import java.util.List;

public class SoundSystem extends GameSystem {
    @Override
    public void update(float deltaTime, List<Entity> entities) {
        for (Entity entity : entities) {
            SoundEffectStoreComponent soundStore = entity.getComponent(SoundEffectStoreComponent.class);
            if (soundStore != null) {
                for (SoundEffect soundEffect : soundStore.getSoundEffects()) {
                    Clip clip = ResourceHub.getInstance().getResourceManager(Clip.class).get(soundEffect.getPath());
                    if (clip != null) {
                        playSound(clip);
                    }
                }
            }
        }
    }

    private void playSound(Clip clip) {
        if (!clip.isRunning()) {
            clip.setFramePosition(0);
            clip.start();
        }
    }
}
