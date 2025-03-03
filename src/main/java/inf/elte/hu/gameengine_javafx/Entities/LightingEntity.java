package inf.elte.hu.gameengine_javafx.Entities;

import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.RectangularHitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.LightComponent;
import inf.elte.hu.gameengine_javafx.Components.ParentComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Components.RadiusComponent;
import inf.elte.hu.gameengine_javafx.Components.RenderingComponents.ColorComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.*;
import inf.elte.hu.gameengine_javafx.Misc.LightType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class LightingEntity extends Entity {
    List<Line> listOfRays = new ArrayList<>();
    public LightingEntity(double x, double y, LightType type, double intensity, Color color, double radius, int rays) {
        addComponent(new LightComponent(type, intensity));
        addComponent(new ColorComponent(color));
        addComponent(new RadiusComponent(radius));
        addComponent(new PositionComponent(x, y, this));
        addComponent(new VelocityComponent());
        addComponent(new ParentComponent());

        listOfRays.addAll(calculateRays(rays));

        addToManager();
    }

    private List<Line> calculateRays(int rays) {
        List<Line> listOfRays = new ArrayList<>();
        PositionComponent pos = getComponent(PositionComponent.class);
        RadiusComponent rad = getComponent(RadiusComponent.class);
        double centerX = pos.getGlobalX();
        double centerY = pos.getGlobalY();
        double radius = rad.getRadius();

        for (int i = 0; i < rays; i++) {
            double angle = Math.toRadians(i * (360.0 / rays));
            double endX = centerX + radius * Math.cos(angle);
            double endY = centerY + radius * Math.sin(angle);

            Line ray = new Line(new Point(centerX, centerY), new Point(endX, endY));
            listOfRays.add(ray);
        }

        return listOfRays;
    }

    public void calculateCollisions() {
        listOfRays.clear();
        listOfRays.addAll(calculateRays(400));
        List<Entity> tileEntities = EntityHub.getInstance().getEntitiesWithType(TileEntity.class);
        RadiusComponent radiusComp = getComponent(RadiusComponent.class);
        double maxRayDistance = radiusComp.getRadius() + 100;

        for (Line line : listOfRays) {
            Point start = line.getPoints().getFirst();
            Point closestIntersection = null;
            double minDistance = Double.MAX_VALUE;

            for (Entity tileEntity : tileEntities) {
                RectangularHitBoxComponent hitboxComponent = tileEntity.getComponent(RectangularHitBoxComponent.class);
                if (hitboxComponent == null) continue;

                Rectangle hitbox = hitboxComponent.getHitBox();

                if (start.distanceTo(hitbox.getTopLeft()) > maxRayDistance) continue;

                for (Edge edge : hitbox.getEdges()) {
                    Point intersection = Shape.getIntersection(line, edge);
                    if (intersection != null) {
                        double distance = start.distanceTo(intersection);
                        if (distance < minDistance) {
                            minDistance = distance;
                            closestIntersection = intersection;
                        }
                    }
                }
            }

            if (closestIntersection != null) {
                line.getPoints().getLast().setX(closestIntersection.getX());
                line.getPoints().getLast().setY(closestIntersection.getY());
            }
        }
    }

    public ComplexShape createShapeFromLines() {
        List<Point> points = new ArrayList<>();
        for (Line line : listOfRays) {
            points.add(line.getEdges().getFirst().getEnd());
        }
        return new ComplexShape(points);
    }

    public void matchPositionToEntity(Entity entity) {
        PositionComponent pos = getComponent(PositionComponent.class);
        PositionComponent parentPos = entity.getComponent(PositionComponent.class);
        pos.setLocalPosition(parentPos.getGlobalX(), parentPos.getGlobalY(), this);
        pos.updateGlobalPosition(this);
    }


    public void renderRays(GraphicsContext gc) {
        for (Line ray : listOfRays) {
            ray.render(gc, getComponent(ColorComponent.class).getColor(), 5);
        }
    }
}
