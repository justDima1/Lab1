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
        this.units.add(unit);
    }

    public void removeUnit(Unit unit) {
        this.units.remove(unit);
    }

    public boolean isEmpty() {
        return units.isEmpty();
    }
}