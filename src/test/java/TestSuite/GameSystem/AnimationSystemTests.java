package TestSuite.GameSystem;

import TestSuite.Entities.DummyEntity;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.AnimationComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ImageComponent;
import inf.elte.hu.gameengine_javafx.Misc.Time;
import inf.elte.hu.gameengine_javafx.Systems.RenderingSystems.AnimationSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnimationSystemTests {

    private AnimationSystem animationSystem;

    @BeforeEach
    void setup() {
        animationSystem = new AnimationSystem();
        animationSystem.start();
        Time.getInstance().setFPSCap(60);
    }

    @Test
    void testAnimationPlaysCorrectlyForStateChange() {
        DummyEntity dummy = new DummyEntity(0, 0, "down", "/assets/images/PlayerIdle.png", 32, 32);

        Time.getInstance().update();
        animationSystem.update();

        AnimationComponent animationComponent = dummy.getComponent(AnimationComponent.class);
        assertNotNull(animationComponent, "AnimationComponent should be added for 'down' state");

        ImageComponent imageComponent = dummy.getComponent(ImageComponent.class);
        assertNotNull(imageComponent, "ImageComponent should exist");

        String currentFramePath = imageComponent.getImagePath();
        assertTrue(currentFramePath.contains("PlayerDown_1") || currentFramePath.contains("PlayerDown_2"),
                "Current frame path should be from 'down' animation set");
    }

    @Test
    void testAnimationSwitchesFramesOverTime() {
        DummyEntity dummy = new DummyEntity(0, 0, "left", "/assets/images/PlayerIdle.png", 32, 32);

        Time.getInstance().update();
        animationSystem.update();
        String firstFrame = dummy.getComponent(ImageComponent.class).getImagePath();
        String secondFrame = dummy.getComponent(ImageComponent.class).getImagePath();
        while (firstFrame.equals(secondFrame)) {
            Time.getInstance().update();
            animationSystem.update();
            secondFrame = dummy.getComponent(ImageComponent.class).getImagePath();
        }

        assertNotEquals(firstFrame, secondFrame,
                "Frame should change after elapsed time");
    }

    @Test
    void testSingleIdleFrameRemovesAnimationComponent() {
        DummyEntity dummy = new DummyEntity(0, 0, "idle", "/assets/images/PlayerIdle.png", 32, 32);

        Time.getInstance().update();
        animationSystem.update();

        assertNull(dummy.getComponent(AnimationComponent.class),
                "AnimationComponent should be removed if only one frame is present");

        String frame = dummy.getComponent(ImageComponent.class).getImagePath();
        assertEquals("/assets/images/PlayerIdle.png", frame,
                "Idle image path should remain static");
    }
}
