package inf.elte.hu.gameengine_javafx.Components.WorldComponents;

import inf.elte.hu.gameengine_javafx.Core.Architecture.Component;
import inf.elte.hu.gameengine_javafx.Entities.TileEntity;

import java.util.ArrayList;
import java.util.List;

public class WorldDataComponent extends Component {
    private final List<List<TileEntity>> tileEntities = new ArrayList<>();

    public WorldDataComponent() {

    }

    public WorldDataComponent(List<List<TileEntity>> tileEntities) {
        this.tileEntities.addAll(tileEntities);
    }

    public List<List<TileEntity>> getMapData() {
        return tileEntities;
    }


    public void setMapData(List<List<TileEntity>> tileEntities) {
        this.tileEntities.clear();
        this.tileEntities.addAll(tileEntities);
    }

    public void addRow(List<TileEntity> row) {
        tileEntities.add(row);
    }

    public void setElement(int x, int y, TileEntity tileEntity) {
        if (x < 0 || y < 0 || y >= tileEntities.size() || x >= tileEntities.get(y).size()) {
            return;
        }
        tileEntities.get(y).set(x, tileEntity);
    }

    public TileEntity getElement(int x, int y) {
        if (x < 0 || y < 0 || y >= tileEntities.size() || x >= tileEntities.get(y).size()) {
            return null;
        }
        return tileEntities.get(y).get(x);
    }

    public void clear() {
        tileEntities.clear();
    }


    @Override
    public String getStatus() {
        return "";
    }
}
