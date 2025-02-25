package inf.elte.hu.gameengine_javafx.Systems.ResourceSystems;

import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.SoundEffectStoreComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Core.ResourceHub;
import inf.elte.hu.gameengine_javafx.Entities.CameraEntity;
import inf.elte.hu.gameengine_javafx.Misc.SoundEffect;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.util.ArrayList;

public class SoundSystem extends GameSystem {
    private static final float MAX_VOLUME = 1.0f;
    private static final float MIN_VOLUME = 0;
    private static final double MAX_DISTANCE = 1000;

    private Entity listenerEntity;


    @Override
    public void start() {
        this.active = true;
    }

    @Override
    public void update() {
        listenerEntity = CameraEntity.getInstance().getOwner();
        var entitiesSnapshot = new ArrayList<>(EntityHub.getInstance().getAllEntities());

        PositionComponent listenerPos = listenerEntity.getComponent(PositionComponent.class);
        if (listenerPos == null) return;

        for (Entity entity : entitiesSnapshot) {
            if (entity == null) continue;

            SoundEffectStoreComponent soundStore = entity.getComponent(SoundEffectStoreComponent.class);
            PositionComponent entityPos = entity.getComponent(PositionComponent.class);

            if (soundStore != null && entityPos != null) {
                double distance = calculateDistance(listenerPos, entityPos);
                float volume = calculateVolume(distance);

                for (SoundEffect soundEffect : soundStore.getSoundEffects()) {
                    Clip clip = ResourceHub.getInstance().getResourceManager(Clip.class).get(soundEffect.getPath());
                    if (clip != null) {
                        setVolume(clip, volume);
                        playSound(clip, volume);
                    }
                }
            }
        }
    }

    private double calculateDistance(PositionComponent a, PositionComponent b) {
        double dx = b.getGlobalX() - a.getGlobalX();
        double dy = b.getGlobalY() - a.getGlobalY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    private float calculateVolume(double distance) {
        if (distance > MAX_DISTANCE) return MIN_VOLUME;
        return (float)(MAX_VOLUME / (1 + (distance / MAX_DISTANCE) * (distance / MAX_DISTANCE)));
    }

    private void setVolume(Clip clip, float volume) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float minGain = gainControl.getMinimum();
        float maxGain = gainControl.getMaximum();
        float gain = minGain + (maxGain - minGain) * volume;
        gainControl.setValue(gain);
    }

    private void playSound(Clip clip, float volume) {
        if (volume == 0) return;
        if (!clip.isRunning()) {
            clip.setFramePosition(0);
            clip.start();
        }
    }
}
