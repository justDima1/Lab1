package model.units;
import java.io.Serializable;
public class Angel extends Unit implements Serializable {

    private static final long serialVersionUID = 1L;
    public Angel() {
        super( 200, 30, 300, 100);
    }
}