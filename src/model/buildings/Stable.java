package model.buildings;

public class Stable extends Building {
    public Stable() {
        super("Конюшня", 500);
    }

    @Override
    public String toString() {
        return getName();
    }
}
