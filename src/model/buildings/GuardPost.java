package model.buildings;

import model.units.Unit;
import model.units.Pikeman;

import java.util.Arrays;
import java.util.List;

public class GuardPost extends Building implements RecruitBuilding {
    public GuardPost() {
        super(BuildingType.GUARD_POST.getName(), BuildingType.GUARD_POST.getCostGold());
    }

    @Override
    public List<Class<? extends Unit>> getAvailableUnits() {
        return Arrays.asList(Pikeman.class);
    }

    @Override
    public int getRecruitCost(Class<? extends Unit> unitClass) {
        if (unitClass.equals(Pikeman.class)) {
            return 15; // Стоимость найма Pikeman
        }
        return 0; // Юнит не поддерживается в этом здании
    }

    @Override
    public String toString() {
        return getName();
    }
}