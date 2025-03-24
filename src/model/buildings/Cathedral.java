package model.buildings;

import model.units.Unit;
import model.units.Paladin;
import java.util.Arrays;
import java.util.List;

public class Cathedral extends Building implements RecruitBuilding {

    public Cathedral() {
        super("Собор", 1000);
    }

    @Override
    public List<Class<? extends Unit>> getAvailableUnits() {
        return Arrays.asList(Paladin.class);
    }

    @Override
    public int getRecruitCost(Class<? extends Unit> unitClass) {
        if (unitClass.equals(Paladin.class)) {
            return 30;
        }
        return 0;
    }

    @Override
    public String toString() {
        return getName();
    }
}