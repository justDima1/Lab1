package Tests;

import model.units.Angel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AngelTest {

    @Test
    void testAngelCreation() {
        Angel angel = new Angel();
        assertNotNull(angel, "Angel should not be null after creation");
        assertEquals(300, angel.getHealth(), "Angel health should be 200");
        assertEquals(200, angel.getAttack(), "Angel attack should be 30");
        assertEquals(100, angel.getCost(), "Angel cost should be 300");
    }
}
//testAngelCreation(): Проверяет, что ангел создается корректно, с заданными здоровьем, атакой и стоимостью.