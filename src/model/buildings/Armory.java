package model.buildings;

import model.units.Unit;
import model.units.Swordsman;

import java.util.Arrays;
import java.util.List;

public class Armory extends Building implements RecruitBuilding {
    public Armory() {
        super("Оружейная", 600);
    }

    @Override
    public List<Class<? extends Unit>> getAvailableUnits() {
        return Arrays.asList(Swordsman.class);
    }

    @Override
    public int getRecruitCost(Class<? extends Unit> unitClass) {
        if (unitClass.equals(Swordsman.class)) {
            return 20; // Стоимость найма Swordsman
        }
        return 0;
    }

    @Override
    public String toString() {
        return getName();
    }
}