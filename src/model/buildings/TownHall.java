package model.buildings;

public class TownHall extends Building {
    private int level;
    private int goldIncome;
    private int upgradeCost;

    public TownHall() {
        super("TownHall", 0);
        this.level = 1;
        this.goldIncome = 250;
        this.upgradeCost = 500; // Cost to upgrade to level 2
    }

    public int getLevel() {
        return level;
    }

    public int getGoldIncome() {
        return goldIncome;
    }

    public int getUpgradeCost() {
        return upgradeCost;
    }

    public void upgrade() {
        if (level < 2) { // Maximum level of 3
            level++;
            goldIncome = 500; // Increase income by 100 per level
            upgradeCost += 200; // Increase upgrade cost
        } else {
            System.out.println("TownHall is at maximum level.");
        }
    }

    @Override
    public String toString() {
        return "Кремль" + " (Уровень: " + level + ", Доход: " + goldIncome + ")";
    }
}