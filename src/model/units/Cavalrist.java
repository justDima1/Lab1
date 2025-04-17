package model.units;
import java.io.Serializable;
public class Cavalrist extends Unit implements Serializable {
    private static final long serialVersionUID = 1L;

    public Cavalrist() {
        super(15, 20, 25, 70);
    }
}