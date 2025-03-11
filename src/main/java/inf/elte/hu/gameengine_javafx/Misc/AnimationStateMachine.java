package inf.elte.hu.gameengine_javafx.Misc;

import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.StateComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.AnimationComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ImageComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public abstract class AnimationStateMachine {
    protected Entity entity;
    protected String lastState;

    public AnimationStateMachine(Entity entity) {
        this.entity = entity;
    }

    public abstract void setAnimationState();
}
