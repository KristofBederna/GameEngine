package inf.elte.hu.gameengine_javafx.Systems;

import inf.elte.hu.gameengine_javafx.Components.PathfindingComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.CentralMassComponent;
import inf.elte.hu.gameengine_javafx.Components.PhysicsComponents.VelocityComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.StateComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Core.Architecture.GameSystem;
import inf.elte.hu.gameengine_javafx.Core.EntityHub;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;

import java.util.*;

/**
 * The PathfindingSystem is responsible for calculating and managing the movement path of entities
 * using pathfinding algorithms. It updates the movement of entities with a `PathfindingComponent`
 * based on their current position and target destination.
 */
public class PathfindingSystem extends GameSystem {

    /**
     * Starts the pathfinding system, setting it as active.
     */
    @Override
    public void start() {
        this.active = true;
    }

    /**
     * Updates the pathfinding system. For each entity with a `PathfindingComponent`, it calculates
     * the entity's movement path and updates the entity's position towards its target.
     */
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
                if (!pathfindingComponent.getPath().isEmpty()) {
                    Point node = pathfindingComponent.getPath().getFirst();
                    Point position = new Point(entity.getComponent(CentralMassComponent.class).getCentralX(),
                            entity.getComponent(CentralMassComponent.class).getCentralY());
                    entity.getComponent(VelocityComponent.class).getVelocity().moveTowards(node, entity);

                    double deltaX = node.getX() - position.getX();
                    double deltaY = node.getY() - position.getY();

                    if (Math.abs(deltaX) >= Math.abs(deltaY)) {
                        if (deltaX > 0) {
                            entity.getComponent(StateComponent.class).setCurrentState("right");
                        } else {
                            entity.getComponent(StateComponent.class).setCurrentState("left");
                        }
                    } else {
                        if (deltaY > 0) {
                            entity.getComponent(StateComponent.class).setCurrentState("down");
                        } else {
                            entity.getComponent(StateComponent.class).setCurrentState("up");
                        }
                    }


                    if (position.compareCoordinates(node)) {
                        pathfindingComponent.getPath().removeFirst();
                        if (pathfindingComponent.getPath().isEmpty()) {
                            entity.getComponent(StateComponent.class).setCurrentState("idle");
                        }
                    }
                }

                if (pathfindingComponent.getPath().isEmpty()) {
                    entity.getComponent(VelocityComponent.class).stopMovement();
                }
            }
        }
    }

    /**
     * Selects a path from the start point to the end point using the A* algorithm.
     * The algorithm calculates the shortest path considering the neighbors of each point.
     *
     * @param start The start point of the pathfinding.
     * @param end The end point of the pathfinding.
     * @param entity The entity for which the pathfinding is being calculated.
     * @return A list of points representing the path from start to end.
     */
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

    /**
     * Reconstructs the path from the start to the end based on the `cameFrom` map.
     *
     * @param cameFrom The map that tracks the best previous point for each point.
     * @param current The current point from which the path is being reconstructed.
     * @return A list of points representing the reconstructed path from start to end.
     */
    private List<Point> reconstructPath(Map<Point, Point> cameFrom, Point current) {
        List<Point> path = new LinkedList<>();
        while (cameFrom.containsKey(current)) {
            path.add(0, current);
            current = cameFrom.get(current);
        }
        return path;
    }
}
