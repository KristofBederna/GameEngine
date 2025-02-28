package inf.elte.hu.gameengine_javafx.Components;

import inf.elte.hu.gameengine_javafx.Components.HitBoxComponents.HitBoxComponent;
import inf.elte.hu.gameengine_javafx.Components.PropertyComponents.CentralMassComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.MapMeshComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.WorldDataComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.WorldDimensionComponent;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;
import inf.elte.hu.gameengine_javafx.Core.Architecture.Entity;
import inf.elte.hu.gameengine_javafx.Entities.WorldEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Misc.Globals;

import java.util.ArrayList;
import java.util.List;

public class PathfindingComponent extends Component {
    private Point start;
    private Point end;
    private Point current;
    private List<Point> neighbours;
    private List<Point> path;

    public PathfindingComponent(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public Point getStart() {
        return start;
    }
    public Point getEnd() {
        return end;
    }
    public List<Point> getPath() {
        return path;
    }

    public List<Point> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<Point> neighbours) {
        this.neighbours = neighbours;
    }

    public Point getCurrent() {
        return current;
    }

    public void setCurrent(Point current) {
        this.current = current;
    }

    public void setPath(List<Point> path) {
        this.path = path;
    }

    public void setDone() {
        this.path = null;
        this.start = null;
        this.end = null;
    }

    @Override
    public String getStatus() {
        return "";
    }

    public void setEnd(Point point) {
        end = point;
    }

    public void resetPathing(Entity entity) {
        path = null;
        neighbours = null;
        start = new Point(entity.getComponent(CentralMassComponent.class).getCentralX(), entity.getComponent(CentralMassComponent.class).getCentralY());
    }

    public List<Point> getNeighbours(Point current) {
        List<Point> neighbours = new ArrayList<>();
        MapMeshComponent mapMeshComponent = WorldEntity.getInstance().getComponent(MapMeshComponent.class);

        int currentX = (int) Math.floor(current.getX() / Globals.tileSize);
        int currentY = (int) Math.floor(current.getY() / Globals.tileSize);

        int worldWidth = (int) WorldEntity.getInstance().getComponent(WorldDimensionComponent.class).getWorldWidth();
        int worldHeight = (int) WorldEntity.getInstance().getComponent(WorldDimensionComponent.class).getWorldHeight();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue; // Skip the current node
                }

                int neighbourX = currentX + i;
                int neighbourY = currentY + j;


                if (neighbourX < 0 || neighbourX >= worldWidth || neighbourY < 0 || neighbourY >= worldHeight) {
                    continue; // Skip out-of-bounds neighbors
                }

                Point neighbour = mapMeshComponent.getMapCoordinate(neighbourX, neighbourY);
                if (neighbour == null) {
                    continue; // Skip if there's no valid point in the map mesh
                }
                neighbours.add(neighbour);
            }
        }
        return neighbours;
    }
}
