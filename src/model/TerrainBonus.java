package model;

import model.map.TerrainType;

public class TerrainBonus {

    public static int getMoveBonus(TerrainType terrainType) {
        switch (terrainType) {
            case GRASS:
                return 1; // Бонус на траве (+1 ход)
            case SWAMP:
                return -1; // Штраф на болоте (-1 ход)
            default:
                return 0; // Нет бонуса на других типах местности
        }
    }
}