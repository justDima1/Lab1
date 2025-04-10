package model.units;

public class Unit {
    private int attack;
    private int defense;
    private int health;
    private int cost;
    private String name;
    private Army army;

    public Unit(int attack, int defense, int health, int cost) {
        this.attack = attack;
        this.defense = defense;
        this.health = health;
        this.cost = cost;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
    public String getName() {
        return name;
    }
    public int getCost() {
        return cost;
    }
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
    }
    public Army getArmy() {
        return army;
    }

    public void setArmy(Army army) {
        this.army = army;
    }

    public void setAttack(int i) {

    }
}