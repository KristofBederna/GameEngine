package inf.elte.hu.gameengine_javafx.Systems;

import inf.elte.hu.gameengine_javafx.Components.PathfindingComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.CentralMassComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;

import java.util.*;

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
            if (start == null || end == null) {
                continue;
            }
            if (pathfindingComponent.getPath() == null) {
                pathfindingComponent.setPath(selectPath(start, end, entity));
            } else {
                if (entity.getComponent(VelocityComponent.class) == null) {
                    System.out.println("Entity has no velocity component");
                    continue;
                }
                if (!pathfindingComponent.getPath().isEmpty()) {
                    Point node = pathfindingComponent.getPath().getFirst();
                    Point position = new Point(entity.getComponent(CentralMassComponent.class).getCentralX(),
                            entity.getComponent(CentralMassComponent.class).getCentralY());
                    entity.getComponent(VelocityComponent.class).getVelocity().moveTowards(node, entity);
                    if (position.compareCoordinates(node)) {
                        pathfindingComponent.getPath().removeFirst();
                    }
                }
                if (pathfindingComponent.getPath().isEmpty()) {
                    entity.getComponent(VelocityComponent.class).stopMovement();
                }
            }
        }
    }

    private List<Point> selectPath(Point start, Point end, Entity entity) {
        List<Point> path = new ArrayList<>();
        Map<Point, Point> cameFrom = new HashMap<>();
        PriorityQueue<Point> openSet = new PriorityQueue<>(Comparator.comparingDouble(p -> p.distanceTo(end)));
        Set<Point> closedSet = new HashSet<>();
        Map<Point, Double> gScore = new HashMap<>();

        openSet.add(start);
        gScore.put(start, 0.0);

        while (!openSet.isEmpty()) {
            Point current = openSet.poll();
            if (current.compareCoordinates(end)) {
                return reconstructPath(cameFrom, current);
            }

            closedSet.add(current);
            List<Point> neighbours = entity.getComponent(PathfindingComponent.class).getNeighbours(current);

            for (Point neighbor : neighbours) {
                if (closedSet.contains(neighbor)) continue;

                double tentativeGScore = gScore.getOrDefault(current, Double.MAX_VALUE) + current.distanceTo(neighbor);

                if (!gScore.containsKey(neighbor) || tentativeGScore < gScore.get(neighbor)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    if (!openSet.contains(neighbor)) openSet.add(neighbor);
                }
            }
        }
        return path;
    }

    private List<Point> reconstructPath(Map<Point, Point> cameFrom, Point current) {
        List<Point> path = new LinkedList<>();
        while (cameFrom.containsKey(current)) {
            path.add(0, current);
            current = cameFrom.get(current);
        }
        return path;
    }
}