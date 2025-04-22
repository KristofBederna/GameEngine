package inf.elte.hu.gameengine_javafx.Misc;

import inf.elte.hu.gameengine_javafx.Components.PathfindingComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;

import java.util.*;

public class Pathfinding {

    /**
     * Selects a path from the start point to the end point using the A* algorithm.
     * The algorithm calculates the shortest path considering the neighbors of each point.
     *
     * @param entity The entity for which the pathfinding is being calculated.
     * @return A list of points representing the path from start to end.
     */
    public static ArrayList<Point> selectPath(Entity entity) {
        Point start = entity.getComponent(PathfindingComponent.class).getStart();
        Point end = entity.getComponent(PathfindingComponent.class).getEnd();

        if (start.distanceTo(end) < 5) {
            return new ArrayList<>(List.of(start));
        }

        Map<Point, Point> cameFrom = new HashMap<>();
        Map<Point, Double> gScore = new HashMap<>();
        gScore.put(start, 0.0);

        // fScore = gScore + heuristic (Euclidean distance)
        Map<Point, Double> fScore = new HashMap<>();
        fScore.put(start, start.distanceTo(end));

        PriorityQueue<Point> openSet = new PriorityQueue<>(
                Comparator.comparingDouble(p -> fScore.getOrDefault(p, Double.MAX_VALUE))
        );
        Set<Point> openSetContents = new HashSet<>();

        openSet.add(start);
        openSetContents.add(start);

        Set<Point> closedSet = new HashSet<>();

        while (!openSet.isEmpty()) {
            Point current = openSet.poll();
            openSetContents.remove(current);

            if (current.compareCoordinates(end)) {
                return reconstructPath(cameFrom, current);
            }

            closedSet.add(current);

            List<Point> neighbors = entity.getComponent(PathfindingComponent.class).getNeighbours(current);
            for (Point neighbor : neighbors) {
                if (closedSet.contains(neighbor)) continue;

                double tentativeG = gScore.getOrDefault(current, Double.MAX_VALUE) + current.distanceTo(neighbor);

                if (tentativeG < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeG);
                    fScore.put(neighbor, tentativeG + neighbor.distanceTo(end));

                    if (!openSetContents.contains(neighbor)) {
                        openSet.add(neighbor);
                        openSetContents.add(neighbor);
                    }
                }
            }
        }

        return new ArrayList<>();
    }

    /**
     * Reconstructs the path from the start to the end based on the `cameFrom` map.
     *
     * @param cameFrom The map that tracks the best previous point for each point.
     * @param current The current point from which the path is being reconstructed.
     * @return A list of points representing the reconstructed path from start to end.
     */
    private static ArrayList<Point> reconstructPath(Map<Point, Point> cameFrom, Point current) {
        ArrayList<Point> path = new ArrayList<>();
        path.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(0, current);
        }
        return path;
    }
}
