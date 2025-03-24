package model.map;

import java.util.Random;

public class GameMap {
    private TerrainType[][] terrain;
    private String[][] map;
    private int width;
    private int height;
    private int castleX;
    private int castleY;
    private int enemyCastleX;
    private int enemyCastleY;
    private Random random = new Random();

    public GameMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.terrain = new TerrainType[width][height];
        this.map = new String[width][height];

        // Заполняем карту типами местности и инициализируем map
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                terrain[i][j] = random.nextDouble() < 0.7 ? TerrainType.GRASS : TerrainType.SWAMP;
                map[i][j] = "."; // Initialize map with empty tiles
            }
        }

        // Размещаем замки (фиксированное положение)
        castleX = 0;
        castleY = 0;
        enemyCastleX = width - 1;
        enemyCastleY = height - 1;

        placeResources();
        placeObstacles();
        placeFixedMine();
        createDiagonalPath();
    }

    public int collectResources(int x, int y) {
        if (map[x][y].equals("G")) {
            map[x][y] = "."; // Убираем золото с карты
            return 500;
        } else if (map[x][y].equals("Z")) {
            return 5;
        }
        return 0; // Ничего не собрали
    }

    private void createDiagonalPath() {
        for (int i = 0; i < Math.min(width, height); i++) {
            map[i][i] = "+";
        }
    }

    private void placeResources() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (map[i][j].equals(".")) {
                    double rand = random.nextDouble();
                    if (rand < 0.1) {
                        map[i][j] = "G"; // Золото
                    }
                }
            }
        }
    }

    private void placeObstacles() {
        int trees = 3;
        int stones = 2;

        // Размещаем деревья
        while (trees > 0) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if (map[x][y].equals(".")) {
                map[x][y] = "T";
                trees--;
            }
        }

        // Размещаем камни
        while (stones > 0) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if (map[x][y].equals(".")) {
                map[x][y] = "X";
                stones--;
            }
        }
    }

    private void placeFixedMine() {
        // Размещаем шахту в фиксированном положении (третья клетка сверху, третья справа)
        int mineX = 2;
        int mineY = width - 3;

        // Проверяем, что шахта не выходит за границы карты
        if (mineX >= 0 && mineX < height && mineY >= 0 && mineY < width) {
            map[mineX][mineY] = "Z"; // Шахта
        }
    }
    public String checkResources(int x, int y) {
        if (map[x][y].equals("G")) {
            return "G";
        } else if (map[x][y].equals("Z")) {
            return "Z";
        }
        return null; // No resource
    }
    public TerrainType[][] getTerrain() {
        return terrain;
    }

    public String[][] getMap() {
        return map;
    }

    public int getCastleX() {
        return castleX;
    }

    public int getCastleY() {
        return castleY;
    }

    public int getEnemyCastleX() {
        return enemyCastleX;
    }

    public int getEnemyCastleY() {
        return enemyCastleY;
    }
}