package inf.elte.hu.gameengine_javafx.Entities;

import inf.elte.hu.gameengine_javafx.Components.Default.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.HitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.InteractiveComponent;
import inf.elte.hu.gameengine_javafx.Components.PathfindingComponent;
import inf.elte.hu.gameengine_javafx.Components.PhysicsComponents.AccelerationComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.*;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.AnimationComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ImageComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ZIndexComponent;
import inf.elte.hu.gameengine_javafx.Components.SoundEffectStoreComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Rectangle;
import inf.elte.hu.gameengine_javafx.Misc.Config;
import inf.elte.hu.gameengine_javafx.Misc.Time;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class DummyEntity extends Entity {

    private String lastState;

    public DummyEntity(int x, int y, String state, String path, double width, double height) {
        this.getComponent(PositionComponent.class).setLocalPosition(x, y, this);
        this.addComponent(new VelocityComponent());
        this.addComponent(new StateComponent(state));
        this.addComponent(new ImageComponent(path, width, height));
        this.addComponent(new InteractiveComponent());
        this.addComponent(new DimensionComponent(width, height));
        this.addComponent(new AccelerationComponent());
        this.addComponent(new HitBoxComponent(new Rectangle(new Point(x, y), width, height).getPoints()));
        this.addComponent(new SoundEffectStoreComponent());
        this.addComponent(new ZIndexComponent(2));
        this.addComponent(new CentralMassComponent((double) x + width / 2, (double) y + height / 2));
        this.addComponent(new PathfindingComponent(new Point(this.getComponent(CentralMassComponent.class).getCentralX(), this.getComponent(CentralMassComponent.class).getCentralY()), new Point(x + (Config.tileSize * 21) + 50, y + (Config.tileSize * 7) + 50)));
        addToManager();
    }


    @Override
    public String toString() {
        return this.getComponent(PositionComponent.class).getGlobalX() + " " + this.getComponent(PositionComponent.class).getGlobalY() + " " + this.getComponent(VelocityComponent.class).getVelocity().getDx() + " " + this.getComponent(VelocityComponent.class).getVelocity().getDy();
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
        durations.add(15 * Time.getInstance().getFPS() / 60);
        durations.add(15 * Time.getInstance().getFPS() / 60);

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
