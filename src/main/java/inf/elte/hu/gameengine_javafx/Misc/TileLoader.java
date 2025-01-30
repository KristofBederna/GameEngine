package inf.elte.hu.gameengine_javafx.Misc;

import java.util.HashMap;

public class TileLoader {
    private static HashMap<Integer, String> tilePaths = new HashMap<>();

    public void addTilePath(Integer value, String path) {
        tilePaths.put(value, path);
    }
    public String getTilePath(Integer value) {
        return tilePaths.get(value);
    }
    public Integer getTileValue(String path) {
        for (HashMap.Entry<Integer, String> entry : tilePaths.entrySet()) {
            if (entry.getValue().equals(path)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
