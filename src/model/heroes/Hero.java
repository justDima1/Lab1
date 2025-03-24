package model.heroes;

import model.units.Archer;
import model.units.Army;
import model.units.Pikeman;
import model.units.Swordsman;

public class Hero {
    private int x;
    private int y;
    private int gold;
    private int gems;
    private Army army;
    private String name;
    private int stableBonusTurnsLeft;
    private int movesLeft = 5;

    public Hero(int x, int y) {
        this.x = x;
        this.y = y;
        this.gold = 1000;
        this.gems = 10;
        this.army = new Army();
        for (int i = 0; i < 10; i++) {
            army.addUnit(new Archer());
        }
        for (int i = 0; i < 5; i++) {
            army.addUnit(new Swordsman());
        }
        for (int i = 0; i < 8; i++) {
            army.addUnit(new Pikeman());
        }
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getStableBonusTurnsLeft() {
        return stableBonusTurnsLeft;
    }

    public void setStableBonusTurnsLeft(int stableBonusTurnsLeft) {
        this.stableBonusTurnsLeft = stableBonusTurnsLeft;
    }
    public boolean hasStableBonus() {
        return stableBonusTurnsLeft > 0;
    }
    public void decreaseStableBonusTurns() {
        if (stableBonusTurnsLeft > 0) {
            stableBonusTurnsLeft--;
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getGems() {
        return gems;
    }

    public void setGems(int gems) {
        this.gems = gems;
    }

    public Army getArmy() {
        return army;
    }

    public void setArmy(Army army) {
        this.army = army;
    }
    public int getMovesLeft() {
        return movesLeft;
    }

    public void setMovesLeft(int movesLeft) {
        this.movesLeft = movesLeft;
    }
}