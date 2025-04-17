package model.map;

import controller.GameController;
import model.heroes.Hero;
import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                terrain[i][j] = random.nextDouble() < 0.6? TerrainType.GRASS : TerrainType.SWAMP;
                map[i][j] = ".";
            }
        }

        castleX = 0;
        castleY = 0;
        enemyCastleX = width - 1;
        enemyCastleY = height - 1;

        placeResources();
        placeObstacles();
        placeFixedMine();
        createDiagonalPath();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(width).append(";").append(height).append("\n");
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                sb.append(terrain[i][j]).append(";");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    public void loadMapDataFromString(String mapData) {
        String[] lines = mapData.split("\n");
        String[] dimensions = lines[0].split(";");
        this.width = Integer.parseInt(dimensions[0]);
        this.height = Integer.parseInt(dimensions[1]);
        this.terrain = new TerrainType[width][height];

        int lineIndex = 1;
        for (int i = 0; i < width; i++) {
            String[] cellValues = lines[lineIndex++].split(";");
            for (int j = 0; j < height; j++) {
                int ordinal = Integer.parseInt(cellValues[j]);
                terrain[i][j] = TerrainType.values()[ordinal];
            }
        }
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
        return null;
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

    public void setMap(String[][] map) {
        this.map = map;
    }

    public void initializeMap() {
    }
    public String convertMapToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(width).append(",").append(height).append("\n");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sb.append(terrain[i][j]).append(",");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void convertStringToMap(String data) {
        String[] lines = data.split("\n");
        String[] dimensions = lines[0].split(",");
        width = Integer.parseInt(dimensions[0]);
        height = Integer.parseInt(dimensions[1]);
        terrain = new TerrainType[height][width];

        for (int i = 0; i < height; i++) {
            String[] terrainValues = lines[i + 1].split(",");
            for (int j = 0; j < width; j++) {
                terrain[i][j] = TerrainType.valueOf(terrainValues[j]);
            }
        }
    }

    public void setTerrain(TerrainType[][] terrain) {
    }
}