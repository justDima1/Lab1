package Tests;

import model.buildings.Gates;
import model.units.Angel;
import model.units.Unit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GatesTest {

    @Test
    void testGatesCreation() {
        Gates gates = new Gates();
        assertNotNull(gates, "Gates should not be null after creation");
        assertEquals("Врата", gates.getName(), "Gates name should be 'Врата'");
        assertEquals(1000, gates.getCost(), "Gates cost should be 1000");
        assertEquals(8, gates.getGemCost(), "Gates gem cost should be 8");
        assertEquals(1, gates.getLevel(), "Gates level should be 1");
    }

    @Test
    void testGetAvailableUnits() {
        Gates gates = new Gates();
        List<Class<? extends Unit>> availableUnits = gates.getAvailableUnits();
        assertNotNull(availableUnits, "Available units should not be null");
        assertFalse(availableUnits.isEmpty(), "Available units should not be empty");
        assertTrue(availableUnits.contains(Angel.class), "Available units should contain Angel class");
    }

    @Test
    void testGetRecruitCost() {
        Gates gates = new Gates();
        int angelCost = gates.getRecruitCost(Angel.class);
        assertEquals(100, angelCost, "Angel recruit cost should be 100");
    }
}
/*testGatesCreation(): Проверяет, что ворота создаются корректно, с заданными именем,
стоимостью, стоимостью в гемах и уровнем.
testGetAvailableUnits(): Проверяет, что метод getAvailableUnits() возвращает список доступных
для найма юнитов, и что в этом списке есть класс Angel.
testGetRecruitCost(): Проверяет, что метод getRecruitCost() возвращает правильную стоимость найма ангела.
 */