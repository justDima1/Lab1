package model.buildings;

public enum BuildingType {
    TAVERN("Таверна", 500),
    STABLE("Конюшня", 500),
    GUARD_POST("Сторожевой Пост", 400),
    CROSSBOW_TOWER("Башня Арбалетчиков", 500),
    ARMORY("Оружейная", 600),
    ARENA("Арена", 800),
    CATHEDRAL("Собор", 1000),
    GATES("Врата", 1000, 8),
    TOWN_HALL("Ратуша", 0);

    private final String name;
    private final int costGold;
    private final int costGems;

    BuildingType(String name, int costGold) {
        this(name, costGold, 0);
    }

    BuildingType(String name, int costGold, int costGems) {
        this.name = name;
        this.costGold = costGold;
        this.costGems = costGems;
    }

    public String getName() {
        return name;
    }

    public int getCostGold() {
        return costGold;
    }

    public int getCostGems() {
        return costGems;
    }
}