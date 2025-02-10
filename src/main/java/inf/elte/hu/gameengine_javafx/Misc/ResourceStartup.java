package inf.elte.hu.gameengine_javafx.Misc;

import inf.elte.hu.gameengine_javafx.Core.ResourceHub;
import inf.elte.hu.gameengine_javafx.Core.ResourceManagers.ImageResourceManager;
import inf.elte.hu.gameengine_javafx.Core.ResourceManagers.SoundResourceManager;
import javafx.scene.image.Image;

import javax.sound.sampled.Clip;

public class ResourceStartup {
    public ResourceStartup() {
        ResourceHub.getInstance();
        startUpResourceManagers();
    }
    public void startUpResourceManagers() {
        //Define resourceManagers here
        ResourceHub.getInstance().addResourceManager(Image.class, new ImageResourceManager());
        ResourceHub.getInstance().addResourceManager(Clip.class, new SoundResourceManager());
    }
}
