package model.units;

import java.io.Serializable;

public class UnitData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String unitType;
    private int count; // Нет необходимости сохранять здоровье, только тип и количество

    public UnitData(String unitType, int count) {
        this.unitType = unitType;
        this.count = count;
    }

    public String getUnitType() {
        return unitType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}