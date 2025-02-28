package inf.elte.hu.gameengine_javafx.Systems;

import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.HitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.PathfindingComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.PositionComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.MapMeshComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.WorldDataComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.WorldDimensionComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Entities.WorldEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Edge;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Line;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Misc.Globals;
import inf.elte.hu.gameengine_javafx.Misc.Layers.GameCanvas;

import java.util.ArrayList;
import java.util.List;

public class PathfindingSystem extends GameSystem {
    @Override
    public void start() {
        this.active = true;
    }

    @Override
    protected void update() {
        var pathfinderEntities = EntityHub.getInstance().getEntitiesWithComponent(PathfindingComponent.class);
        for (var entity : pathfinderEntities) {
            PathfindingComponent pathfindingComponent = entity.getComponent(PathfindingComponent.class);
            Point start = pathfindingComponent.getStart();
            Point end = pathfindingComponent.getEnd();
            if (start == null && end == null) {
                continue;
            }
            boolean hasHitBox = WorldEntity.getInstance().getComponent(WorldDataComponent.class)
                    .getElement((int) end.getX() / Globals.tileSize, (int) end.getY() / Globals.tileSize)
                    .getAllComponents().keySet()
                    .stream()
                    .anyMatch(HitBoxComponent.class::isAssignableFrom);
            if (hasHitBox) {
                System.out.println("Pathfinder end point cannot be reached inside a hit box");
                continue;
            }
            if (pathfindingComponent.getPath() == null) {
                pathfindingComponent.setPath(selectPath(start, end, entity));
            }
            if (pathfindingComponent.getPath() != null) {

                if (entity.getComponent(VelocityComponent.class) == null) {
                    System.out.println("Entity has no velocity component");
                    continue;
                }
                Point node = entity.getComponent(PathfindingComponent.class).getPath().getFirst();
                Point position = new Point(entity.getComponent(PositionComponent.class).getGlobalX(), entity.getComponent(PositionComponent.class).getGlobalY());
                entity.getComponent(VelocityComponent.class).moveTowards(node, entity);
                if (position.compareCoordinates(node)) {
                    System.out.println("entity reached checkpoint");
                    entity.getComponent(PathfindingComponent.class).getPath().removeFirst();
                }
                if (entity.getComponent(PathfindingComponent.class).getPath().isEmpty()) {
                    entity.getComponent(VelocityComponent.class).stopMovement();
                }

            }
            Point position = new Point(entity.getComponent(PositionComponent.class).getGlobalX(), entity.getComponent(PositionComponent.class).getGlobalY());

            if (position.compareCoordinates(end)) {
                System.out.println("entity reached goal");
                pathfindingComponent.setDone();
                pathfindingComponent.setNeighbours(pathfindingComponent.getNeighbours(end));
            }
        }
    }

    private List<Point> selectPath(Point start, Point end, Entity entity) {
        System.out.println("This should only run once");
        List<Point> path = new ArrayList<>();
        path.add(start);
        while (path.getLast().distanceTo(end) > 0.1) {
            Point current = path.getLast();
            List<Point> neighbours = entity.getComponent(PathfindingComponent.class).getNeighbours(current);
            entity.getComponent(PathfindingComponent.class).setNeighbours(neighbours);
            entity.getComponent(PathfindingComponent.class).setCurrent(current);
            Point next = selectNextNode(current, neighbours, end, path);
            if (next == null) {
                return path;
            }
            path.add(next);
        }
        return path;
    }

    private Point selectNextNode(Point current, List<Point> neighbours, Point end, List<Point> path) {
        if (neighbours.isEmpty()) {
            return current;
        }
        double minDistance = current.distanceTo(end);
        int minIdx = -1;
        for (int i = 0; i < neighbours.size(); i++) {
            if (end.distanceTo(neighbours.get(i)) < minDistance && notBackTracking(neighbours.get(i), path)) {
                minDistance = end.distanceTo(neighbours.get(i));
                minIdx = i;
            }
        }
        if (minIdx == -1) {
            return current;
        }
        return neighbours.get(minIdx);
    }

    private boolean notBackTracking(Point point, List<Point> path) {
        if (path == null) {
            return true;
        }
        boolean found = path.contains(point);
        return !found;
    }
}
