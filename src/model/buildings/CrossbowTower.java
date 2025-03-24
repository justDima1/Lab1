package model.buildings;

import model.units.Unit;
import model.units.Archer;

import java.util.Arrays;
import java.util.List;

public class CrossbowTower extends Building implements RecruitBuilding {
    public CrossbowTower() {
        super(BuildingType.CROSSBOW_TOWER.getName(), BuildingType.CROSSBOW_TOWER.getCostGold());
    }

    @Override
    public List<Class<? extends Unit>> getAvailableUnits() {
        return Arrays.asList(Archer.class);
    }

    @Override
    public int getRecruitCost(Class<? extends Unit> unitClass) {
        if (unitClass.equals(Archer.class)) {
            return 15;
        }
        return 0;
    }

    @Override
    public String toString() {
        return getName();
    }
}