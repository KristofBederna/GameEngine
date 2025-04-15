package TestSuite.Entities;

import static org.junit.jupiter.api.Assertions.*;

import inf.elte.hu.gameengine_javafx.Components.Default.ParentComponent;
import inf.elte.hu.gameengine_javafx.Entities.ParticleEmitterEntity;
import inf.elte.hu.gameengine_javafx.Entities.ParticleEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Rectangle;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import inf.elte.hu.gameengine_javafx.Misc.Direction;

public class ParticleEmitterEntityTests {

    private ParticleEmitterEntity emitterEntity;
    private ParticleEntity mockParticle;
    private Direction direction;
    private int amount;

    @BeforeEach
    public void setUp() {
        mockParticle = new ParticleEntity(0, 0, 0, 0, new Rectangle(new Point(0, 0), 10, 10), Color.ORANGE, Color.ORANGE, 10);
        direction = Direction.LEFT;
        amount = 5;

        emitterEntity = new ParticleEmitterEntity(100, 150, mockParticle, direction, amount, 1000);
    }

    @Test
    public void testCreateParticles() {
        assertEquals(amount, emitterEntity.getAmount(), "The number of particles created should match the specified amount.");
    }

    @Test
    public void testParticleEntityMock() {
        assertSame(mockParticle, emitterEntity.getMockParticle(), "The mock particle should be the same as the one passed to the emitter.");
    }

    @Test
    public void testCreateCopy() {
        assertEquals(5, emitterEntity.getComponent(ParentComponent.class).getChildren().size(), "The mock particle should have been created 5 times on spawn.");
        emitterEntity.createParticles(mockParticle, emitterEntity.getAmount(), emitterEntity.getComponent(ParentComponent.class));

        assertFalse(emitterEntity.getComponent(ParentComponent.class).getChildren().contains(mockParticle), "The mock particle should not have become a children.");
        assertEquals(10, emitterEntity.getComponent(ParentComponent.class).getChildren().size(), "The mock particle should have been created 5 more times.");
    }

    @Test
    public void testHardCopyBehavior() {
        ParticleEntity particleCopy = ParticleEntity.hardCopySelf(mockParticle);
        assertNotNull(particleCopy, "A hard copy of the particle entity should be created.");
    }
}
