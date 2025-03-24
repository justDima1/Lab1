package model.buildings;

public class BuildingFactory {
    public static Building createBuilding(BuildingType type) {
        switch (type) {
            case TAVERN:
                return new Tavern();
            case STABLE:
                return new Stable();
            case GUARD_POST:
                return new GuardPost();
            case CROSSBOW_TOWER:
                return new CrossbowTower();
            case ARMORY:
                return new Armory();
            case ARENA:
                return new Arena();
            case CATHEDRAL:
                return new Cathedral();
            case GATES:
                return new Gates();
            case TOWN_HALL:
                return new TownHall();
            default:
                throw new IllegalArgumentException("Unknown building type: " + type);
        }
    }
}