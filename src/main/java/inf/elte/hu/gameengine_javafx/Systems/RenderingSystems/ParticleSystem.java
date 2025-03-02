package inf.elte.hu.gameengine_javafx.Systems.RenderingSystems;

import inf.elte.hu.gameengine_javafx.Components.DirectionComponent;
import inf.elte.hu.gameengine_javafx.Components.ParentComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Entities.ParticleEmitterEntity;
import inf.elte.hu.gameengine_javafx.Entities.ParticleEntity;
import inf.elte.hu.gameengine_javafx.Misc.Direction;
import inf.elte.hu.gameengine_javafx.Misc.Time;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ParticleSystem extends GameSystem {
    private static final double MAX_DISTANCE = 200.0;

    @Override
    public void start() {
        this.active = true;
    }

    @Override
    protected void update() {
        for (Entity entity : EntityHub.getInstance().getEntitiesWithType(ParticleEmitterEntity.class)) {
            ParentComponent parent = entity.getComponent(ParentComponent.class);
            DirectionComponent directionComponent = entity.getComponent(DirectionComponent.class);
            Direction direction = directionComponent.getDirection();

            PositionComponent emitterPosition = entity.getComponent(PositionComponent.class);
            double emitterX = emitterPosition.getGlobalX();
            double emitterY = emitterPosition.getGlobalY();

            if (parent == null) {
                continue;
            }

            Set<Entity> toBeRemoved = new HashSet<>();
            for (Entity particle : parent.getChildren()) {
                ParticleEntity particleEntity = (ParticleEntity) particle;
                VelocityComponent velocity = particleEntity.getComponent(VelocityComponent.class);
                PositionComponent position = particleEntity.getComponent(PositionComponent.class);

                if (velocity == null || position == null) {
                    continue;
                }

                if (velocity.getDx() == 0 && velocity.getDy() == 0) {
                    Random random = new Random();

                    double speed = 50 + random.nextDouble(0, 50);
                    double angle = random.nextDouble(-Math.PI / 4, Math.PI / 4);

                    double dx = 0;
                    double dy = 0;

                    switch (direction) {
                        case UP:
                            dx = random.nextDouble(-50, 50) * Time.getInstance().getDeltaTime();
                            dy = -speed * Time.getInstance().getDeltaTime();
                            break;
                        case DOWN:
                            dx = random.nextDouble(-50, 50) * Time.getInstance().getDeltaTime();
                            dy = speed * Time.getInstance().getDeltaTime();
                            break;
                        case LEFT:
                            dx = -speed * Time.getInstance().getDeltaTime();
                            dy = random.nextDouble(-50, 50) * Time.getInstance().getDeltaTime();
                            break;
                        case RIGHT:
                            dx = speed * Time.getInstance().getDeltaTime();
                            dy = random.nextDouble(-50, 50) * Time.getInstance().getDeltaTime();
                            break;
                    }

                    dx += Math.cos(angle) * random.nextDouble(0, 20) * Time.getInstance().getDeltaTime();
                    dy += Math.sin(angle) * random.nextDouble(0, 20) * Time.getInstance().getDeltaTime();

                    velocity.setDx(dx);
                    velocity.setDy(dy);

                    double drag = 0.98;
                    velocity.setDx(velocity.getDx() * drag);
                    velocity.setDy(velocity.getDy() * drag);
                }

                double distanceFromEmitter = Math.sqrt(Math.pow(position.getGlobalX() - emitterX, 2) + Math.pow(position.getGlobalY() - emitterY, 2));
                if (distanceFromEmitter > MAX_DISTANCE) {
                    toBeRemoved.add(particleEntity);
                }
            }

            entity.getComponent(ParentComponent.class).removeChildren(toBeRemoved);
            for (Entity particle : toBeRemoved) {
                particle.getComponent(ParentComponent.class).setParent(null);
                EntityHub.getInstance().getEntityManager(ParticleEntity.class).unload(particle.getId());
            }
        }
    }
}
