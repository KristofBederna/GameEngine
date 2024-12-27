//Testing entity class to showcase progress

package entities;

import assets.GlobalPaths;
import components.*;
import core.Component;
import core.Entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ImageEntity extends Entity {
    String lastState = "";
    public ImageEntity(int x, int y, String path) {
        super(0);
        this.addComponent(new ImageComponent(path, 25, 25));
        this.addComponent(new PositionComponent(x, y));
        this.addComponent(new VelocityComponent(0, 0));
        this.addComponent(new InteractiveComponent());
        this.addComponent(new StateComponent("idle"));
    }

    public void setAnimationState() {
        if (Objects.equals(this.getComponent(StateComponent.class).getCurrentState(), lastState)) {
            return;
        }
        this.removeComponentsByType(AnimationComponent.class);
        ArrayList<String> downFramePath = new ArrayList<>();
        downFramePath.add(GlobalPaths.ImagesPath + "PlayerDown_1.png");
        downFramePath.add(GlobalPaths.ImagesPath + "PlayerDown_2.png");
        ArrayList<String> upFramePath = new ArrayList<>();
        upFramePath.add(GlobalPaths.ImagesPath + "PlayerUp_1.png");
        upFramePath.add(GlobalPaths.ImagesPath + "PlayerUp_2.png");
        ArrayList<String> leftFramePath = new ArrayList<>();
        leftFramePath.add(GlobalPaths.ImagesPath + "PlayerLeft_1.png");
        leftFramePath.add(GlobalPaths.ImagesPath + "PlayerLeft_2.png");
        ArrayList<String> rightFramePath = new ArrayList<>();
        rightFramePath.add(GlobalPaths.ImagesPath + "PlayerRight_1.png");
        rightFramePath.add(GlobalPaths.ImagesPath + "PlayerRight_2.png");

        ArrayList<Integer> durations = new ArrayList<>();
        durations.add(15);
        durations.add(15);

        switch (this.getComponent(StateComponent.class).getCurrentState()) {
            case "up":
                lastState = "up";
                this.addComponent(new AnimationComponent(upFramePath, durations));
                break;
            case "down":
                lastState = "down";
                this.addComponent(new AnimationComponent(downFramePath, durations));
                break;
            case "left":
                lastState = "left";
                this.addComponent(new AnimationComponent(leftFramePath, durations));
                break;
            case "right":
                lastState = "right";
                this.addComponent(new AnimationComponent(rightFramePath, durations));
                break;
            case "idle":
                this.removeComponentsByType(AnimationComponent.class);
                try {
                    this.getComponent(ImageComponent.class).setImage(GlobalPaths.ImagesPath + "PlayerIdle.png");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }
}
