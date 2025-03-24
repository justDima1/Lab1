package model.buildings;

import model.units.Unit;

import java.util.List;

public interface RecruitBuilding {
    List<Class<? extends Unit>> getAvailableUnits();

    int getRecruitCost(Class<? extends Unit> unitClass); // Стоимость найма одного юнита
}