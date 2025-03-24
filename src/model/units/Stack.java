package model.units;

public class Stack {
    private Class<? extends Unit> unitType;
    private int size;

    public Stack(Class<? extends Unit> unitType, int size) {
        this.unitType = unitType;
        this.size = size;
    }

    public Class<? extends Unit> getUnitType() {
        return unitType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalAttack() {
        try {
            Unit unit = unitType.getDeclaredConstructor().newInstance();
            return unit.getAttack() * size; // Суммарный урон стека
        } catch (Exception e) {
            System.out.println("Ошибка при создании юнита: " + e.getMessage());
            return 0;
        }
    }

    public void takeDamage(int damage) {
        try {
            Unit unit = unitType.getDeclaredConstructor().newInstance();
            int unitHealth = unit.getHealth();
            int unitsToKill = damage / unitHealth;
            if (damage % unitHealth != 0) unitsToKill++;

            size -= unitsToKill;
            if(size < 0) size = 0;
        } catch (Exception e) {
            System.out.println("Ошибка при создании юнита: " + e.getMessage());
        }
    }

    public int getHealth() {
        try {
            Unit unit = unitType.getDeclaredConstructor().newInstance();
            return unit.getHealth() * size;
        } catch (Exception e) {
            System.out.println("Ошибка при создании юнита: " + e.getMessage());
            return 0;
        }
    }
}