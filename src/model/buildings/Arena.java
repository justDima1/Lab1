package model.buildings;

import model.units.Unit;
import model.units.Cavalrist;
import java.util.Arrays;
import java.util.List;

public class Arena extends Building implements RecruitBuilding {

    public Arena() {
        super("Арена", 800);
    }

    @Override
    public List<Class<? extends Unit>> getAvailableUnits() {
        return Arrays.asList(Cavalrist.class);
    }

    @Override
    public int getRecruitCost(Class<? extends Unit> unitClass) {
        if (unitClass.equals(Cavalrist.class)) {
            return 25;
        }
        return 0;
    }

    @Override
    public String toString() {
        return getName();
    }
}