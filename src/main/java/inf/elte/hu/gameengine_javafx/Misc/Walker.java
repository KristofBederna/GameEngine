package inf.elte.hu.gameengine_javafx.Misc;

import inf.elte.hu.gameengine_javafx.Components.TileValueComponent;
import inf.elte.hu.gameengine_javafx.Components.WorldComponents.WorldDataComponent;
import inf.elte.hu.gameengine_javafx.Entities.WorldEntity;
import inf.elte.hu.gameengine_javafx.Maths.Geometry.Point;

import java.util.ArrayList;
import java.util.Random;

public class Walker {
    private int x;
    private int y;
    private ArrayList<Walker> walkers;
    private WorldEntity world;

    public Walker(int x, int y, WorldEntity world, ArrayList<Walker> walkers) {
        this.x = x;
        this.y = y;
        this.world = world;
        this.walkers = walkers;
    }

    public void walk() {
        Random r = new Random();
        while (getFilledPercentage() < WalkerConfig.stopPercentage) {
            if (Config.wallTiles.contains(world.getComponent(WorldDataComponent.class).getMapData().getElementAt(new Point(y*Config.scaledTileSize +Config.scaledTileSize /2, x*Config.scaledTileSize +Config.scaledTileSize /2)).getComponent(TileValueComponent.class).getTileValue())) {
                changeDirection();
                continue;
            }

            placeTile();
            if (r.nextInt(10) % WalkerConfig.moduloToMultiply == 0) {
                multiply();
            } else if (r.nextInt(10) % WalkerConfig.moduloToDie == 0) {
                die();
            } else if (r.nextInt(10) % WalkerConfig.moduloToTeleport == 0) {
                teleport();
            }
            changeDirection();
        }
    }

    private void teleport() {
        Random r = new Random();
        this.x = r.nextInt(WalkerConfig.minX, WalkerConfig.maxX);
        this.y = r.nextInt(WalkerConfig.minY, WalkerConfig.maxY);
    }

    public int getFilledPercentage() {
        int filledTiles = 0;
        int totalTiles = WalkerConfig.maxX * WalkerConfig.maxY;

        for (int i = 0; i < WalkerConfig.maxX; i++) {
            for (int j = 0; j < WalkerConfig.maxY; j++) {
                if (Config.wallTiles.contains(world.getComponent(WorldDataComponent.class).getMapData().getElementAt(new Point(j*Config.scaledTileSize +Config.scaledTileSize /2, i*Config.scaledTileSize +Config.scaledTileSize /2)).getComponent(TileValueComponent.class).getTileValue())) {
                    filledTiles++;
                }
            }
        }

        return (filledTiles * 100) / totalTiles;
    }

    private void changeDirection() {
        Random random = new Random();
        int direction = random.nextInt(4);

        switch (direction) {
            case 0 -> { if (y < WalkerConfig.maxY) y++; } // Move Up
            case 1 -> { if (y > 0) y--; } // Move Down
            case 2 -> { if (x > 0) x--; } // Move Left
            case 3 -> { if (x < WalkerConfig.maxX) x++; } // Move Right
        }
    }

    private void placeTile() {
        this.world.getComponent(WorldDataComponent.class).getMapData().setElementAt(new Point(x*Config.scaledTileSize, y*Config.scaledTileSize), WalkerConfig.placeTileNumber);
        this.world.getComponent(WorldDataComponent.class).getMapData().getSavedChunks().get(new Tuple<>(Math.floorDiv(x, Config.chunkWidth), Math.floorDiv(y, Config.chunkHeight))).setElement(x % Config.chunkWidth, y % Config.chunkHeight, WalkerConfig.placeTileNumber);
    }

    private void multiply() {
        if (walkers.size() >= WalkerConfig.maxWalkers) {
            return;
        }
        Walker walker = new Walker(this.x, this.y, this.world, this.walkers);
        walkers.add(walker);
    }

    private void die() {
        if (walkers.size() <= 1) {
            return;
        }
        walkers.removeLast();
    }
}
