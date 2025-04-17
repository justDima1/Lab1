package main;

import java.io.Serializable;

public class HeroData implements Serializable {
    private int x;
    private int y;
    private int health;

    public HeroData(int x, int y, int health) {
        this.x = x;
        this.y = y;
        this.health = health;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }
}