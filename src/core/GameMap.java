package core;

import entities.TileEntity;

public class GameMap {
    private final int width;
    private final int height;
    private final TileEntity[][] mapData;

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.mapData = new TileEntity[height][width];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public TileEntity getElement(int x, int y) {
        return mapData[y][x];
    }

    public void setElement(int x, int y, TileEntity tile) {
        mapData[y][x] = tile;
    }

    public TileEntity[][] getMapData() {
        return mapData;
    }
}
