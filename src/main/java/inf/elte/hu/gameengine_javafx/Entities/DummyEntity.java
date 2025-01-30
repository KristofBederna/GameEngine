package inf.elte.hu.gameengine_javafx.Entities;

import inf.elte.hu.gameengine_javafx.Components.*;
import inf.elte.hu.gameengine_javafx.Core.Entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class DummyEntity extends Entity {

    private String lastState;
    public DummyEntity(int x, int y, String state, String path, int width, int height) {
        this.addComponent(new PositionComponent(x, y));
        this.addComponent(new VelocityComponent());
        this.addComponent(new StateComponent(state));
        this.addComponent(new ImageComponent(path, width, height));
        this.addComponent(new InteractiveComponent());
        this.addComponent(new RectangularHitBoxComponent(x,y,width,height));
    }

    @Override
    public String toString() {
        return this.getComponent(PositionComponent.class).getX() + " " + this.getComponent(PositionComponent.class).getY() + " " + this.getComponent(VelocityComponent.class).getDx() + " " + this.getComponent(VelocityComponent.class).getDy();
    }

    public void setAnimationState() {
        if (Objects.equals(this.getComponent(StateComponent.class).getCurrentState(), lastState)) {
            return;
        }
        this.removeComponentsByType(AnimationComponent.class);
        ArrayList<String> downFramePath = new ArrayList<>();
        downFramePath.add("/assets/images/" + "PlayerDown_1.png");
        downFramePath.add("/assets/images/" + "PlayerDown_2.png");
        ArrayList<String> upFramePath = new ArrayList<>();
        upFramePath.add("/assets/images/" + "PlayerUp_1.png");
        upFramePath.add("/assets/images/" + "PlayerUp_2.png");
        ArrayList<String> leftFramePath = new ArrayList<>();
        leftFramePath.add("/assets/images/" + "PlayerLeft_1.png");
        leftFramePath.add("/assets/images/" + "PlayerLeft_2.png");
        ArrayList<String> rightFramePath = new ArrayList<>();
        rightFramePath.add("/assets/images/" + "PlayerRight_1.png");
        rightFramePath.add("/assets/images/" + "PlayerRight_2.png");

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
                    this.getComponent(ImageComponent.class).setImage("/assets/images/" + "PlayerIdle.png");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                break;
        }
    }
}
