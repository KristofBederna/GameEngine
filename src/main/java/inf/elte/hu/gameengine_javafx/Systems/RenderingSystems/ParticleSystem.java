package inf.elte.hu.gameengine_javafx.Systems.RenderingSystems;

import inf.elte.hu.gameengine_javafx.Components.MaxDistanceFromOriginComponent;
import inf.elte.hu.gameengine_javafx.Components.Default.ParentComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.DirectionComponent;
import inf.elte.hu.gameengine_javafx.Components.Default.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.PhysicsComponents.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Components.TimeComponent;
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

            if (parent == null) {
                continue;
            }

            if (System.currentTimeMillis() >= entity.getComponent(TimeComponent.class).getLastOccurrence() + entity.getComponent(TimeComponent.class).getTimeBetweenOccurrences()) {
                ((ParticleEmitterEntity) entity).createParticles((ParticleEntity) parent.getChildren().iterator().next(), 250, entity.getComponent(ParentComponent.class));
                entity.getComponent(TimeComponent.class).setLastOccurrence();
            }

            Set<Entity> toBeRemoved = new HashSet<>();
            for (Entity particle : parent.getChildren()) {
                ParticleEntity particleEntity = (ParticleEntity) particle;
                VelocityComponent velocity = particleEntity.getComponent(VelocityComponent.class);
                PositionComponent position = particleEntity.getComponent(PositionComponent.class);

                if (velocity == null || position == null) {
                    continue;
                }

                if (velocity.getVelocity().getDx() == 0 && velocity.getVelocity().getDy() == 0) {
                    Random random = new Random();

                    double speed = 50 + random.nextDouble(0, 50);
                    double angle = random.nextDouble(-Math.PI / 4, Math.PI / 4);

                    double dx = 0;
                    double dy = switch (direction) {
                        case UP -> {
                            dx = random.nextDouble(-500, 500) * Time.getInstance().getDeltaTime();
                            yield -speed * Time.getInstance().getDeltaTime();
                        }
                        case DOWN -> {
                            dx = random.nextDouble(-500, 500) * Time.getInstance().getDeltaTime();
                            yield speed * Time.getInstance().getDeltaTime();
                        }
                        case LEFT -> {
                            dx = -speed * Time.getInstance().getDeltaTime();
                            yield random.nextDouble(-500, 500) * Time.getInstance().getDeltaTime();
                        }
                        case RIGHT -> {
                            dx = speed * Time.getInstance().getDeltaTime();
                            yield random.nextDouble(-500, 500) * Time.getInstance().getDeltaTime();
                        }
                        case ALL -> {
                            dx = random.nextDouble(-500, 500) * Time.getInstance().getDeltaTime();
                            yield random.nextDouble(-500, 500) * Time.getInstance().getDeltaTime();
                        }
                    };

                    dx += Math.cos(angle) * random.nextDouble(0, 20) * Time.getInstance().getDeltaTime();
                    dy += Math.sin(angle) * random.nextDouble(0, 20) * Time.getInstance().getDeltaTime();

                    velocity.setVelocity(dx, dy);
                    double drag = 0.98;
                    velocity.setVelocity(velocity.getVelocity().getDx() * drag, velocity.getVelocity().getDy() * drag);
                }

                if (particleEntity.getComponent(MaxDistanceFromOriginComponent.class).isOverMaxDistance(particleEntity)) {
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
