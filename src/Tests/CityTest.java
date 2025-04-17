package Tests;

import model.City;
import model.buildings.Building;
import model.buildings.Gates;
import model.buildings.TownHall;
import model.units.Unit;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CityTest {
    private Building Gates;
    private City playerCity;

    @Test
    void testCityCreation() {
        City city = new City("TestCity", 1, 2);
        assertNotNull(city, "City should not be null after creation");
        assertEquals("TestCity", city.getName(), "City name should be 'TestCity'");
        assertEquals(1, city.getX(), "City X coordinate should be 1");
        assertEquals(2, city.getY(), "City Y coordinate should be 2");
        assertFalse(city.getBuildings().isEmpty(), "City should have at least one building (TownHall) after creation");
    }

    @Test
    void testGetGoldIncome() {
        City city = new City("TestCity", 1, 2);
        int expectedIncome = 250;
        assertEquals(expectedIncome, city.getGoldIncome(), "Initial gold income should be 250");
    }

    @Test
    void testAddBuilding() {
        City city = new City("TestCity", 1, 2);
        Gates gates = new Gates();
        city.addBuilding(Gates);
        assertTrue(city.getBuildings().contains(Gates), "City should contain the added building");
    }

    @Test
    void testHasTownHall() {
        City city = new City("TestCity", 1, 2);
        assertTrue(city.hasTownHall(), "City should have a TownHall");
    }
}
/*testCityCreation(): Проверяет, что город создается корректно, с заданными именем, координатами и наличием ратуши.
testGetGoldIncome(): Проверяет, что метод getGoldIncome() возвращает правильное значение дохода города.
testAddBuilding(): Проверяет, что можно добавить здание в город и что оно будет содержаться в списке зданий города.
testHasTownHall(): Проверяет, что город имеет ратушу.*/