package inf.elte.hu.gameengine_javafx.Misc;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public static int tileSize = 100;
    public static double gameCanvasWidth = 1000;
    public static double gameCanvasHeight = 1000;
    public static String windowTitle = "Game Engine";
    public static double EPSILON = 1e-9;
    public static int chunkWidth = 16;
    public static int chunkHeight = 16;
    public static int loadDistance = 2;
    public static double drag = 0.001;
    public static double friction = 0.001;
    public static boolean renderDebugMode = false;
    public static boolean fullScreenMode = false;
    public static List<Integer> wallTiles = new ArrayList<>(List.of(1,2,3,4,5,6,7,8,0));
}
