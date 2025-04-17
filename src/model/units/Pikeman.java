package model.units;
import java.io.Serializable;
public class Pikeman extends Unit implements Serializable {
    private static final long serialVersionUID = 1L;


    public Pikeman() {
        super(10, 8, 12, 20);
    }
}