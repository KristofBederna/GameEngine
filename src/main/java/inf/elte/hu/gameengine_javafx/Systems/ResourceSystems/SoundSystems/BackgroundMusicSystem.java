package inf.elte.hu.gameengine_javafx.Systems.ResourceSystems.SoundSystems;

import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.ResourceHub;
import inf.elte.hu.gameengine_javafx.Misc.Sound.BackgroundMusic;
import inf.elte.hu.gameengine_javafx.Misc.Sound.BackgroundMusicStore;
import inf.elte.hu.gameengine_javafx.Misc.Configs.ResourceConfig;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * The BackgroundMusicSystem handles the playback of background music tracks in the game.
 * It supports features like shuffle mode, preventing immediate repeats, and volume control.
 */
public class BackgroundMusicSystem extends GameSystem {

    private Clip currentClip;
    private LineListener currentListener;
    private BackgroundMusic lastPlayed;
    private final Random random = new Random();
    private boolean shuffleMode = true;
    private boolean preventImmediateRepeats = true;
    private List<BackgroundMusic> playQueue = new ArrayList<>();
    private final Object playbackLock = new Object();

    /**
     * Starts the system. The system becomes active and begins to check for music playback.
     */
    @Override
    public void start() {
        this.active = true;
    }

    /**
     * Updates the system by checking the status of the current clip and playing a new track if necessary.
     * This is called every game loop iteration.
     */
    @Override
    protected void update() {
        synchronized (playbackLock) {
            if (currentClip != null && currentClip.isOpen()) {
                setVolume(currentClip, ResourceConfig.backgroundMusicVolume * ResourceConfig.masterVolume);
            }

            if (shouldPlayNewTrack()) {
                playNextMusic();
            }
        }
    }

    /**
     * Determines whether a new track should be played.
     * A new track is played if there is no current clip or if the current clip is not playing or not open.
     *
     * @return true if a new track should be played, false otherwise
     */
    private boolean shouldPlayNewTrack() {
        return currentClip == null || !currentClip.isOpen() || !currentClip.isActive();
    }

    /**
     * Selects and plays the next music track from the available music list.
     */
    private void playNextMusic() {
        synchronized (playbackLock) {
            BackgroundMusicStore store = BackgroundMusicStore.getInstance();
            if (store == null || store.getBackgroundMusics().isEmpty()) return;

            List<BackgroundMusic> availableMusic = getAvailableMusicList(store);
            if (availableMusic.isEmpty()) {
                availableMusic = new ArrayList<>(store.getBackgroundMusics());
            }

            BackgroundMusic selectedMusic = selectMusic(availableMusic);
            playMusic(selectedMusic);
        }
    }

    /**
     * Filters and returns the list of available music tracks to play based on the current settings.
     *
     * @param store the BackgroundMusicStore containing all music tracks
     * @return a list of available music tracks
     */
    private List<BackgroundMusic> getAvailableMusicList(BackgroundMusicStore store) {
        List<BackgroundMusic> allMusic = store.getBackgroundMusics();

        if (preventImmediateRepeats && lastPlayed != null) {
            return allMusic.stream()
                    .filter(music -> !music.equals(lastPlayed))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(allMusic);
    }

    /**
     * Selects a music track to play. It can either pick randomly (shuffle mode) or follow the play queue.
     *
     * @param availableMusic the list of available music tracks to choose from
     * @return the selected music track
     */
    private BackgroundMusic selectMusic(List<BackgroundMusic> availableMusic) {
        if (shuffleMode) {
            return availableMusic.get(random.nextInt(availableMusic.size()));
        } else {
            if (playQueue.isEmpty()) {
                playQueue.addAll(availableMusic);
            }
            return playQueue.remove(0); // Pop the first item from the queue
        }
    }

    /**
     * Plays the selected background music.
     *
     * @param music the background music to play
     */
    private void playMusic(BackgroundMusic music) {
        synchronized (playbackLock) {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            String path = music.getPath();
            Clip clip = ResourceHub.getInstance().getResourceManager(Clip.class).get(path);

            if (!isValidClip(clip)) {
                System.err.println("Invalid clip: " + path);
                return;
            }

            cleanupCurrentClip();

            try {
                currentClip = clip;
                lastPlayed = music;
                setVolume(currentClip, ResourceConfig.backgroundMusicVolume * ResourceConfig.masterVolume);
                currentClip.setFramePosition(0);

                currentListener = event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        synchronized (playbackLock) {
                            if (currentClip != null &&
                                    event.getFramePosition() >= currentClip.getFrameLength() - 1) {
                                cleanupCurrentClip();
                                if (this.active) {
                                    playNextMusic();
                                }
                            }
                        }
                    }
                };

                currentClip.addLineListener(currentListener);
                currentClip.start();
            } catch (Exception e) {
                System.err.println("Error playing music: " + e.getMessage());
                cleanupCurrentClip();
            }
        }
    }

    /**
     * Cleans up the current music clip by stopping it and removing any listeners.
     */
    private void cleanupCurrentClip() {
        synchronized (playbackLock) {
            if (currentClip != null) {
                try {
                    if (currentListener != null) {
                        currentClip.removeLineListener(currentListener);
                        currentListener = null;
                    }

                    if (currentClip.isRunning()) {
                        currentClip.stop();
                    }
                    currentClip.setFramePosition(0);
                } catch (Exception e) {
                    System.err.println("Error cleaning up clip: " + e.getMessage());
                } finally {
                    currentClip = null;
                }
            }
        }
    }

    /**
     * Checks if the provided clip is valid and ready for playback.
     *
     * @param clip the clip to check
     * @return true if the clip is valid, false otherwise
     */
    private boolean isValidClip(Clip clip) {
        return clip != null && clip.isOpen() && clip.getBufferSize() > 0;
    }

    /**
     * Sets the volume of the given clip.
     *
     * @param clip the clip to adjust the volume for
     * @param volume the volume level to set (0.0 to 1.0)
     */
    private void setVolume(Clip clip, float volume) {
        if (!isValidClip(clip)) return;

        try {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            if (ResourceConfig.linearVolumeControl) {
                float minGain = gainControl.getMinimum();
                float maxGain = gainControl.getMaximum();
                float gain = minGain + (maxGain - minGain) * volume;
                gainControl.setValue(gain);
            } else {
                float dB = (float) (Math.log10(Math.max(volume, 0.0001f)) * 20);
                gainControl.setValue(dB);
            }
        } catch (Exception e) {
            System.err.println("Failed to set volume: " + e.getMessage());
        }
    }

    // Public control methods

    /**
     * Sets whether shuffle mode is enabled for music playback.
     *
     * @param shuffle true to enable shuffle mode, false to disable it
     */
    public void setShuffleMode(boolean shuffle) {
        synchronized (playbackLock) {
            this.shuffleMode = shuffle;
            if (!shuffle) playQueue.clear();
        }
    }

    /**
     * Sets whether to prevent immediate repeats of the last played track.
     *
     * @param prevent true to prevent immediate repeats, false to allow them
     */
    public void setPreventImmediateRepeats(boolean prevent) {
        synchronized (playbackLock) {
            this.preventImmediateRepeats = prevent;
        }
    }

    /**
     * Stops the current background music.
     */
    public void stopMusic() {
        synchronized (playbackLock) {
            cleanupCurrentClip();
        }
    }

    /**
     * Pauses the current background music.
     */
    public void pauseMusic() {
        synchronized (playbackLock) {
            if (isValidClip(currentClip) && currentClip.isRunning()) {
                currentClip.stop();
            }
        }
    }

    /**
     * Resumes the current background music if it was paused.
     */
    public void resumeMusic() {
        synchronized (playbackLock) {
            if (isValidClip(currentClip) && !currentClip.isRunning()) {
                currentClip.start();
            }
        }
    }

    /**
     * Checks if music is currently playing.
     *
     * @return true if music is playing, false otherwise
     */
    public boolean isPlaying() {
        synchronized (playbackLock) {
            return isValidClip(currentClip) && currentClip.isRunning();
        }
    }
}
