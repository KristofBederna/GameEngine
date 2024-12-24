package core;

public class GameMap {
    private final int width;
    private final int height;
    private final int[][] mapData;

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.mapData = new int[height][width];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getElement(int x, int y) {
        return mapData[y][x];
    }

    public void setElement(int x, int y, int value) {
        mapData[y][x] = value;
    }

    public int[][] getMapData() {
        return mapData;
    }
}
