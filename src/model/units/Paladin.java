package model.units;
import java.io.Serializable;
public class Paladin extends Unit implements Serializable {
    private static final long serialVersionUID = 1L;

    public Paladin() {
        super( 25, 30, 30, 80);
    }
}