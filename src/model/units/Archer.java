package model.units;

import java.io.Serializable;

public class Archer extends Unit implements Serializable {
    private static final long serialVersionUID = 1L;

    public Archer() {
        super(5, 5, 10, 25); // attack, defense, health
    }
}