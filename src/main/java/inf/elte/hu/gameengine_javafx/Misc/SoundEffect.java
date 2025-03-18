package inf.elte.hu.gameengine_javafx.Misc;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;

public class SoundEffect {
    private Entity owner;
    private String path;
    private String identifier;
    private float maxVolume;
    private float minVolume;
    private double maxDistance;

    public SoundEffect(Entity owner, String path, String identifier, float maxVolume, float minVolume, double maxDistance) {
        this.owner = owner;
        this.path = path;
        this.identifier = identifier;
        this.maxVolume = maxVolume;
        this.minVolume = minVolume;
        this.maxDistance = maxDistance;
    }

    public Entity getOwner() {
        return owner;
    }

    public String getPath() {
        return path;
    }

    public String getIdentifier() {
        return identifier;
    }

    public double getMaxDistance() {
        return maxDistance;
    }
    public float getMinVolume() {
        return minVolume;
    }
    public float getMaxVolume() {
        return maxVolume;
    }
}
