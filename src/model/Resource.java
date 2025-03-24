package model;

public abstract class Resource {
    private String name;
    private int amount;

    public Resource(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public void subtractAmount(int amount) {
        this.amount -= amount;
    }
}