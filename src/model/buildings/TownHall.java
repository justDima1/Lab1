package model.buildings;

public class TownHall extends Building {
    private int level;
    private int goldIncome;
    private int upgradeCost;
    public TownHall() {
        super("TownHall", 0);
        this.level = 1;
        this.goldIncome = 250;
        this.upgradeCost = 500;
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
        if (level < 2) {
            level++;
            goldIncome = 500;
            upgradeCost += 200;
        } else {
            System.out.println("TownHall is at maximum level.");
        }
    }
    @Override
    public String toString() {
        return "Кремль" + " (Уровень: " + level + ", Доход: " + goldIncome + ")";
    }
}