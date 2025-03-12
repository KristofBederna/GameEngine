package inf.elte.hu.gameengine_javafx.Components.WorldComponents;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;
import inf.elte.hu.gameengine_javafx.Entities.TileEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;
import inf.elte.hu.gameengine_javafx.Misc.Config;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.Chunk;
import inf.elte.hu.gameengine_javafx.Misc.MapClasses.World;

import java.util.ArrayList;
import java.util.List;

public class WorldDataComponent extends Component {
    private World world = new World();

    public WorldDataComponent() {}

    public WorldDataComponent(World world) {
        this.world = world;
    }

    public World getMapData() {
        return world;
    }


    public void setMapData(int x, int y, Chunk tileEntities) {
        this.world.clear();
        this.world.addChunk(x, y, tileEntities);
    }

//    public void setElement(int x, int y, TileEntity tileEntity) {
//        if (x < 0 || y < 0 || y >= world.size() || x >= world.get(y).size()) {
//            return;
//        }
//        world.get(y).set(x, tileEntity);
//    }

    public TileEntity getElement(int x, int y) {
        return world.getElementAt(x, y);
    }

    public TileEntity getElement(Point point) {
        return world.getElementAt(point);
    }


    public void clear() {
        world.clear();
    }


    @Override
    public String getStatus() {
        return "";
    }
}
