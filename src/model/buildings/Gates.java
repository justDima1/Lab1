package model.buildings;

import model.units.Unit;
import model.units.Angel; // Assuming an Angel class exists
import java.util.Arrays;
import java.util.List;

public class Gates extends Building implements RecruitBuilding {
    private int level;
    private int gemCost;

    public Gates() {
        super("Врата", 1000);
        this.gemCost = 8;
        this.level = 1;
    }

    public int getLevel() {
        return level;
    }

    public int getGemCost() { // Добавляем getter для стоимости в гемах
        return gemCost;
    }
    @Override
    public List<Class<? extends Unit>> getAvailableUnits() {
        return Arrays.asList(Angel.class);
    }

    @Override
    public int getRecruitCost(Class<? extends Unit> unitClass) {
        if (unitClass.equals(Angel.class)) {
            return 100; // 100 gold for an Angel
        }
        return 0;
    }
}