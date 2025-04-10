package model.units;

import java.util.ArrayList;
import java.util.List;

public class Army {
    private final List<Unit> units;

    public Army() {
        this.units = new ArrayList<>();
    }

    public List<Unit> getUnits() {
        return units;
    }

    public void addUnit(Unit unit) {
        units.add(unit);
        unit.setArmy(this); // Устанавливаем связь с армией
    }

    public void removeUnit(Unit unit) {
        //System.out.println("Размер армии перед удалением: " + units.size());
        this.units.remove(unit);
        //System.out.println("Удаляемый юнит: " + unit);
        System.out.println("Размер армии после удаления: " + units.size());
    }

    public boolean isEmpty() {
        return units.isEmpty();
    }
}