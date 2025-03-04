package inf.elte.hu.gameengine_javafx.Entities;

import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.RectangularHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.InteractiveComponent;
import inf.elte.hu.gameengine_javafx.Components.ParentComponent;
import inf.elte.hu.gameengine_javafx.Components.PlayerComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.*;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.AnimationComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ImageComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ZIndexComponent;
import inf.elte.hu.gameengine_javafx.Components.SoundEffectStoreComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Misc.Time;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class PlayerEntity extends Entity {
    private String lastState;
    public PlayerEntity(double x, double y, String state, String path, double width, double height) {
        this.addComponent(new PositionComponent(x, y, this));
        this.addComponent(new VelocityComponent());
        this.addComponent(new StateComponent(state));
        this.addComponent(new ImageComponent(path, width, height));
        this.addComponent(new InteractiveComponent());
        this.addComponent(new DimensionComponent(width, height));
        //this.addComponent(new TriangularHitBoxComponent(new Triangle(new Point(x, y), new Point(x + width, y), new Point(x + width/2, y + height))));
        this.addComponent(new RectangularHitBoxComponent(new Point(x, y), width, height));
        //this.addComponent(new NSidedHitBoxComponent(new NSidedShape(new Point(x+width/2, y+height/2), width, 6, 0)));
        this.addComponent(new SoundEffectStoreComponent());
        this.addComponent(new ZIndexComponent(2));
        this.addComponent(new ParentComponent());
        this.addComponent(PlayerComponent.getInstance());
        this.addComponent(new CentralMassComponent(x+width/2, y+height/2));

        addToManager();
    }


    @Override
    public String toString() {
        return this.getComponent(PositionComponent.class).getGlobalX() + " " + this.getComponent(PositionComponent.class).getGlobalY() + " " + this.getComponent(VelocityComponent.class).getDx() + " " + this.getComponent(VelocityComponent.class).getDy();
    }

    public void setAnimationState() {
        if (Objects.equals(this.getComponent(StateComponent.class).getCurrentState(), lastState)) {
            return;
        }

        AnimationComponent animationComponent = this.getComponent(AnimationComponent.class);

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
        durations.add(15* Time.getInstance().getFPS()/60);
        durations.add(15*Time.getInstance().getFPS()/60);

        String currentState = this.getComponent(StateComponent.class).getCurrentState();
        lastState = currentState;

        switch (currentState) {
            case "up":
                if (animationComponent != null) {
                    try {
                        animationComponent.setFrames(upFramePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    animationComponent.setDurations(durations);
                } else {
                    this.addComponent(new AnimationComponent(upFramePath, durations));
                }
                break;
            case "down":
                if (animationComponent != null) {
                    try {
                        animationComponent.setFrames(downFramePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    animationComponent.setDurations(durations);
                } else {
                    this.addComponent(new AnimationComponent(downFramePath, durations));
                }
                break;
            case "left":
                if (animationComponent != null) {
                    try {
                        animationComponent.setFrames(leftFramePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    animationComponent.setDurations(durations);
                } else {
                    this.addComponent(new AnimationComponent(leftFramePath, durations));
                }
                break;
            case "right":
                if (animationComponent != null) {
                    try {
                        animationComponent.setFrames(rightFramePath);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    animationComponent.setDurations(durations);
                } else {
                    this.addComponent(new AnimationComponent(rightFramePath, durations));
                }
                break;
            case "idle":
                this.removeComponentsByType(AnimationComponent.class);
                this.getComponent(ImageComponent.class).setNextFrame("/assets/images/PlayerIdle.png");
                break;
        }
    }
}
