    package model.map;

    public class Tile {
        private final TerrainType terrainType;

        public Tile(TerrainType terrainType) {
            this.terrainType = terrainType;
        }

        public TerrainType getTerrainType() {
            return terrainType;
        }
    }