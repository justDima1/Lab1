package model;

import java.util.Random;

public class BattleMap {
    private int width;
    private int height;
    private String[][] map;

    public BattleMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new String[height][width];
        generateMap();
    }

    private void generateMap() {
        Random random = new Random();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // Generate random terrain (grass, trees, etc.)
                if (random.nextInt(10) < 2) {
                    map[i][j] = "T"; // Tree
                } else {
                    map[i][j] = "."; // Grass
                }
            }
        }
    }

    public String[][] getMap() {
        return map;
    }

    public void displayMap() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }
}
